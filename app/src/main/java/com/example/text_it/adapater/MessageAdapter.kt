package com.example.text_it.adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.dataClass.MessageEntry
import com.example.text_it.fragment.Chat
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class MessageAdapter (private val Messages: MutableList<MessageEntry>): RecyclerView.Adapter<MessageAdapter.ViewHolder>()
{
    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val item: View = item.findViewById(R.id.constraintLayoutMessageItem)
        val sender : TextView = item.findViewById(R.id.textViewSenderName)
        val time : TextView = item.findViewById(R.id.textViewTime)
        val lastMessage: TextView = item.findViewById(R.id.textViewLastMessage)
        val profileImage: ShapeableImageView = item.findViewById(R.id.shapeableImageViewProfile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.msg_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur_message = Messages[position]
        holder.sender.text = cur_message.senderName
        holder.time.text = TimeAgo.using(cur_message.date.time)
        holder.lastMessage.text = cur_message.lastMessage
        Glide.with(holder.itemView.context)
            .load(cur_message.profileImage)
            .into(holder.profileImage)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val otherUserid = cur_message.userId

        holder.item.setOnClickListener {
            val roomId = createRoomIfNotExist(currentUserId!!, otherUserid)
            val chatFragment = Chat(roomId, otherUserid)
            val transaction = it.context as androidx.fragment.app.FragmentActivity
            transaction.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, chatFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return Messages.size
    }

    fun setMessageList(newList: List<MessageEntry>) {
        Messages.clear()
        Messages.addAll(newList)
        notifyDataSetChanged()
    }

    fun getRoomId(currentUserId: String, otherUserId: String): String {
        val roomIds = mutableListOf<String>()
        roomIds.add(currentUserId + otherUserId)
        roomIds.add(otherUserId + currentUserId)
        roomIds.sort()
        return roomIds[0]
    }

    fun createRoomIfNotExist(currentUserId: String, otherUserId: String): String {
        val roomId = getRoomId(currentUserId, otherUserId)
        val realtimeDB = FirebaseDatabase.getInstance()

        realtimeDB.getReference("rooms/$roomId").get().addOnSuccessListener {
            if (!it.exists()) {
                realtimeDB.getReference("rooms/$roomId").setValue(hashMapOf(
                    "messages" to listOf<String>()
                ))
            }
        }

        return roomId
    }

}