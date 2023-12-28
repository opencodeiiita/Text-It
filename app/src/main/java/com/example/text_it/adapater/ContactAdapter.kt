package com.example.text_it.adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.text_it.R
import com.example.text_it.dataClass.ProfileEntry
import com.google.android.material.imageview.ShapeableImageView


class ContactAdapter(private val ContactList: List<ProfileEntry>):
    RecyclerView.Adapter<ContactAdapter.ViewHolder>()
{
    companion object{
        private var prev  : Char? = null
    }
    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val image : ShapeableImageView = item.findViewById(R.id.circularImageView1)
        val name : TextView = item.findViewById(R.id.textView11)
        val mot : TextView = item.findViewById(R.id.textView12)
        val head : TextView = item.findViewById(R.id.headingContact)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val item : View = LayoutInflater.from(parent.context).inflate(R.layout.contactlayout, parent, false)

        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = ContactList[position]


        holder.name.text = contact.name
        holder.mot.text = "This is Fun"
        Log.d("Hel", contact.image)
        Glide.with(holder.itemView.context)
            .load(contact.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)

        if(position == 0)
        {
            Log.d("helcat", contact.name)
        }
        if (position == 0 || ContactList[position - 1].name[0] != ContactList[position].name[0]) {
            holder.head.text = ContactList[position].name[0].toString()
            holder.head.visibility = View.VISIBLE
        } else {
            holder.head.text = ""
            holder.head.visibility = View.GONE // Reset the visibility
        }
    }

    override fun getItemCount(): Int {
        return ContactList.size
    }

}