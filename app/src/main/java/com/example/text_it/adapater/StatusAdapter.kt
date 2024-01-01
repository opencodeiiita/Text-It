package com.example.text_it.adapater

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.example.text_it.dataClass.Status_dataclass
import com.google.android.material.imageview.ShapeableImageView


class StatusAdapter(private var dataSet: MutableList<Status_dataclass>) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profile: ShapeableImageView = view.findViewById<ShapeableImageView>(R.id.statusProfile)
        val name = view.findViewById<TextView>(R.id.statusName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // dummy view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.status_item, parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = dataSet[position]
        holder.profile.setImageURI(Uri.parse(currentItem.statusPhoto))
        holder.name.text = currentItem.username
    }

    override fun getItemCount(): Int = dataSet.size
    fun setStatusList(statusList: List<Status_dataclass>) {

    }

//    fun setStatusList(newList: List<Status_dataclass>) {
//        dataSet.clear()
//        dataSet.addAll(newList)
//        notifyDataSetChanged()
//    }
}