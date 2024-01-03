package com.example.text_it.adapater

import ChatMessage
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
import com.example.text_it.fragment.Message
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

class MessageAdapter(private val Messages: List<ChatMessage>):
    RecyclerView.Adapter<MessageAdapter.ViewHolder>()
{

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val date : TextView = item.findViewById(R.id.messageDate)
        val message : TextView = item.findViewById(R.id.messageContent)
        val time : TextView = item.findViewById(R.id.messageTime)
    }

    override fun getItemViewType(position: Int): Int {
        return Messages[position].sender
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        var item : View  = LayoutInflater.from(parent.context).inflate(R.layout.fragment_chat_item_me, parent, false)


        if(viewType == 1)
        {
            item = LayoutInflater.from(parent.context).inflate(R.layout.fragment_chat_item_them, parent, false)
        }

        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur_message = Messages[position]


        val today = Date()

        // Calculate yesterday's date in milliseconds (86400000 milliseconds in a day)
        val yesterdayTimeMillis = today.time - 86400000

        // Create a Date object for yesterday
        val yesterday = Date(yesterdayTimeMillis)

        // Get the date strings for today and yesterday
        val todayDateString = dateFormat.format(today)
        val yesterdayDateString = dateFormat.format(yesterday)

        if(dateFormat.format(cur_message.date) == todayDateString)
        {
            holder.date.text = "Today"
        }
        else if(dateFormat.format(cur_message.date) == yesterdayDateString)
        {
            holder.date.text = "Yesterday"
        }
        else
        {
            holder.date.text = dateFormat.format(cur_message.date)
        }

        holder.message.text = cur_message.message
        holder.time.text = timeFormat.format(cur_message.date)

        if(position != 0 && Messages[position - 1].date == Messages[position].date)
        {
            holder.date.visibility = View.GONE
        }
        else
        {
            holder.date.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return Messages.size
    }



}