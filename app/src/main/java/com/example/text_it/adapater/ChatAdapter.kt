package com.example.text_it.adapater

import ChatMessage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

class ChatAdapter(private val Chats: MutableList<ChatMessage>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>()
{

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val date : TextView = item.findViewById(R.id.messageDate)
        val message : TextView = item.findViewById(R.id.messageContent)
        val time : TextView = item.findViewById(R.id.messageTime)
    }

    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        if(Chats[position].sender == currentUser)
        {
            return 0
        } else {
            return 1
        }
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
        val cur_message = Chats[position]
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

        if(position != 0 && dateFormat.format(Chats[position - 1].date) == dateFormat.format(Chats[position].date))
        {
            holder.date.visibility = View.GONE
        }
        else
        {
            holder.date.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return Chats.size
    }

    fun addMessage(roomId: String, message: ChatMessage) {
        val realtimeDB: DatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().reference
        realtimeDB.child("rooms").child(roomId).child("messages").push().setValue(message).addOnSuccessListener {
            updateMessageList(roomId)
        }
    }

    fun updateMessageList(roomId: String) {
        Chats.clear()
        val realtimeDB: DatabaseReference = FirebaseDatabase.getInstance().reference
        realtimeDB.child("rooms/$roomId").child("messages").get().addOnSuccessListener { dataSnapshot ->
            val messagesMap = dataSnapshot.value as? HashMap<String, Any>
            messagesMap?.let { map ->
                val newMessageList: MutableList<ChatMessage> = mutableListOf()
                map.forEach { (_, value) ->
                    val message = value as HashMap<String, Any>
                    val messageText = message["message"] as String
                    val sender = message["sender"] as String
                    val dateData = message["date"] as HashMap<String, Any>
                    val dateInMillis = dateData["time"] as Long
                    val chatMessage = ChatMessage(messageText, sender, Date(dateInMillis))
                    newMessageList.add(chatMessage)
                }
                val sortedMessages = newMessageList.sortedBy { it.date }
                Chats.addAll(sortedMessages)
                notifyDataSetChanged()
            }
        }
    }

}