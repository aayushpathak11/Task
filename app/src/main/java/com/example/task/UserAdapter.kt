package com.example.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val c:Context,val userList:ArrayList<UserData>):RecyclerView.Adapter<UserAdapter.userViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item,parent,false)
        return userViewHolder(v)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {

        val newList = userList[position]
        holder.item.text=newList.itemName
        holder.price.text="\u20B9 "+newList.itemPrice
        holder.time.text=newList.itemTime
    }

    override fun getItemCount(): Int {

        return userList.size
    }
    public class userViewHolder(val v:View):RecyclerView.ViewHolder(v){
        val item = v.findViewById<TextView>(R.id.mItem)
        val price = v.findViewById<TextView>(R.id.mPrice)
        val time = v.findViewById<TextView>(R.id.mTime)
    }
}