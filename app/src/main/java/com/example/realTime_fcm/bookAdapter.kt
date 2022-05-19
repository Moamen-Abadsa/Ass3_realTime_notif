package com.example.realTime_fcm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_book.view.*
import java.util.ArrayList

class booksAdapter(val activity: Activity, val data:ArrayList<Book>):RecyclerView.Adapter<booksAdapter.viewHolder>() {
    class viewHolder (item:View):RecyclerView.ViewHolder(item){
      var name=item.M_name
        var auther=item.M_Auther
        var year=item.textView5
        var price=item.textView6
        var image=item.imageView4
        var but=item.button2
        var MyRate=item.MyRate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
      val root=LayoutInflater.from(activity).inflate(R.layout.item_book,parent,false)
        return viewHolder(root)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.name.text=data[position].name
        holder.auther.text=data[position].auther
        holder.year.text=data[position].date
        holder.price.text=data[position].price.toString()
        Glide.with(activity).load(data[position].image).into(holder.image)
        holder.MyRate.rating=data[position].rate.toFloat()
        holder.but.setOnClickListener {
            val i=Intent(activity,Edit_book::class.java)
            i.putExtra("book",data[position])
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}