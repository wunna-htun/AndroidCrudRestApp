package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class UserDetailsActivity : AppCompatActivity() {
    private val tag : String = UserDetailsActivity::class.java.simpleName

    private var originalItemId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // Save the original item ID
        originalItemId = intent.getStringExtra("ItemId")
        // Parse the parameters out of the intent and assign the values
        // to the UI elements.
        tv_item_id.text = originalItemId.toString()
        et_item_name.setText(intent.getStringExtra("ItemName"))

        // Set click listeners for the button to delete this element
        bt_delete.setOnClickListener {
            deleteCustomer(originalItemId)
            var intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        // Set click listeners for the button to update this element
        bt_update.setOnClickListener {
            updateCusomter(originalItemId,
                Customer(originalItemId, et_item_name.text.toString(),"Mial@com"))

            var intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun deleteCustomer(itemId : String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val webResponse = CustomerAccess.customerApi.deletePartAsync(itemId).await()
                Log.e(tag, "Delete success: ${webResponse.isSuccessful}")
                Log.d("WebResponse","$webResponse")
                Toast.makeText(applicationContext, "Deleted: $itemId: ${webResponse.isSuccessful}", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                // Error with network request
                Log.e(tag, "Exception " + e.printStackTrace())
                Toast.makeText(this@UserDetailsActivity, "Exception ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun updateCusomter(originalItemId: String, newItem: Customer) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val webResponse = CustomerAccess.customerApi.updatePartAsync(originalItemId, newItem).await()
                Log.e(tag, "Update success: ${webResponse.isSuccessful}")
                Toast.makeText(applicationContext, "Updated: $originalItemId: ${webResponse.isSuccessful}", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                // Error with network request
                Log.e(tag, "Exception " + e.printStackTrace())
                Toast.makeText(this@UserDetailsActivity, "Exception ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
