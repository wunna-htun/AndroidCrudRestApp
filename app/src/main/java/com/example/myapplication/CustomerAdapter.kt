package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_list.view.*

class CustomerAdapter (var customerItemList:List<Customer>, val clickListener: (Customer) ->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount()= customerItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as UserViewHolder).bind(customerItemList[position],clickListener)
    }

    class UserViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){

        fun bind(customer :Customer, clickListener: (Customer) -> Unit){
            itemView.tv_user_item_name.text=customer.name
            itemView.tv_user_id.text=customer.id.toString()
            itemView.setOnClickListener{clickListener(customer)}
        }

    }
}