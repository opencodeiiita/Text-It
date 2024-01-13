package com.example.text_it.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.dataClass.CallInfo
import com.google.android.material.imageview.ShapeableImageView

class CallAdapter(private val context: Context, private val callList: MutableList<CallInfo>) : RecyclerView.Adapter<CallAdapter.CallViewHolder>() {

    // ViewHolder class to hold references to the views
    class CallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callTextView: TextView = itemView.findViewById(R.id.textViewName)
        val img: ShapeableImageView = itemView.findViewById(R.id.imageViewProfile)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_call_item, parent, false)

        return CallViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = callList[position]
        holder.callTextView.text = call.name
        Glide.with(context)
            .load(call.profileImage)
            .into(holder.img)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return callList.size
    }
}