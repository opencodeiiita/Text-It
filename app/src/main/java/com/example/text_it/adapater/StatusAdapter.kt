package com.example.text_it.adapater

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.dataClass.Status_dataclass
import com.example.text_it.fragment.StatusView
import com.google.android.material.imageview.ShapeableImageView


class StatusAdapter(private var dataSet: MutableList<Status_dataclass>,var context: Context?) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // dummy view
        val view = LayoutInflater.from(context).inflate(R.layout.status_item, parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = dataSet[position]
        context?.let { Glide.with(it).load(currentItem.statusPhoto).into(holder.profile) }
        holder.name?.text = currentItem.username

        holder.itemView.setOnClickListener {
            val fragment = StatusView()
            val bundle = Bundle()
            bundle.putString("selectedImageUri", currentItem.statusPhoto)
            bundle.putString("username", currentItem.username)
            bundle.putString("timestamp", currentItem.statusUploadTime!!.seconds.toString())
            fragment.arguments = bundle

            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val profile: ShapeableImageView = view.findViewById(R.id.statusProfile)
        val name: TextView? = view.findViewById(R.id.statusName)
    }



    fun setStatusList(newList: List<Status_dataclass>) {
        dataSet.clear()
        dataSet.addAll(newList)
        notifyDataSetChanged()
    }
}