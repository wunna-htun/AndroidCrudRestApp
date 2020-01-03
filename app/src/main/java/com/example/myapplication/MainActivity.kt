package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val tag : String = MainActivity::class.java.simpleName
    private lateinit var adapter: CustomerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Create","Create")

        rv_parts.layoutManager  = LinearLayoutManager(this)

        adapter = CustomerAdapter(listOf(),{ customerItem:Customer-> UserItemClicked(customerItem)})

        Log.d("adapter","${adapter.itemCount}")

        rv_parts.adapter=adapter

        loadPartsAndUpdateList()



    }


    override fun onResume() {
        super.onResume()
        Log.d("Resume","resume")
        loadPartsAndUpdateList()

    }





    private fun addPart(partItem: Customer) {
        GlobalScope.launch(Dispatchers.Main) {
            val webResponse = CustomerAccess.customerApi.addPartAsync(partItem).await()
            Log.d(tag, "Add success: ${webResponse.isSuccessful}")
            loadPartsAndUpdateList()
        }
    }



    private fun loadPartsAndUpdateList() {
        // Launch Kotlin Coroutine on Android's main thread
        // Note: better not to use GlobalScope, see:
        // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/index.html
        // An even better solution would be to use the Android livecycle-aware viewmodel
        // instead of attaching the scope to the activity.
        Log.d("call list","list")
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Execute web request through coroutine call adapter & retrofit
                val webResponse = CustomerAccess.customerApi.getPartsAsync().await()

                if (webResponse.isSuccessful) {

                    Log.d("message","web response")
                    // Get the returned & parsed JSON from the web response.
                    // Type specified explicitly here to make it clear that we already
                    // get parsed contents.
                    val partList: List<Customer>? = webResponse.body()
                    Log.d(tag, partList.toString())
                    // Assign the list to the recycler view. If partsList is null,
                    // assign an empty list to the adapter.
                    adapter.customerItemList = partList ?: listOf()
                    // Inform recycler view that data has changed.
                    // Makes sure the view re-renders itself
                    adapter.notifyDataSetChanged()
                } else {
                    // Print error information to the console
                    Log.e(tag, "Error ${webResponse.code()}")
                    Toast.makeText(this@MainActivity, "Error ${webResponse.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                // Error with network request
                Log.e(tag, "Exception " + e.printStackTrace())
                Toast.makeText(this@MainActivity, "Exception ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun UserItemClicked(customerItem : Customer) {
        // Test code to add a new item to the list
        // Will be replaced with UI function soon
        //val newPart = PartData(Random.nextLong(0, 999999), "Infrared sensor")
        //addPart(newPart)
        //return

        Toast.makeText(this, "Clicked: ${customerItem.name}", Toast.LENGTH_LONG).show()

        // Launch second activity, pass part ID as string parameter
        val showDetailActivityIntent = Intent(this, UserDetailsActivity::class.java)
        //showDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, partItem.id.toString())
        showDetailActivityIntent.putExtra("ItemId", customerItem.id)
        showDetailActivityIntent.putExtra("ItemName", customerItem.name)
        startActivity(showDetailActivityIntent)
    }


}
