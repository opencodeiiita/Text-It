package com.example.text_it.fragment

import ChatMessage
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.example.text_it.adapater.ContactAdapter
import com.example.text_it.adapater.MessageAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Chat : Fragment() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val dateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.US)
        val date1 = dateFormat.parse("January 01, 2024 10:59 PM")
        val date2 = dateFormat.parse("January 02, 2024 11:15 AM")
        val date3 = dateFormat.parse("January 03, 2024 02:30 PM")
        val date4 = dateFormat.parse("January 04, 2024 02:30 PM")




        val messageList = mutableListOf<ChatMessage>(
            ChatMessage("Hello", 0, date2!!),
            ChatMessage("Hi there", 1, date1!!),
            ChatMessage("How are you?", 0, date1),
            ChatMessage("I'm good, thanks!", 1, date3!!),
            ChatMessage("What's up?", 0, date2),
            ChatMessage("Not much", 1, date4!!),
            ChatMessage("Like I had a big confusion, can you help me sort out?", 0, date4),
            ChatMessage("I am not sure, I can try. I would recommend going to muffinBoy. That guy is great. Like I am trying to make this message long for testing and I don't know what else to type?", 0, date4),


        )

        // Sort the messageList by date in increasing order
        val sortedMessages = messageList.sortedBy { it.date }



        val recycler = view.findViewById<RecyclerView>(R.id.messageRecyclerView)
        val adapter = MessageAdapter(messageList)


        val messageBox = view.findViewById<EditText>(R.id.messageBox)
        val send = view.findViewById<ImageView>(R.id.imageView9)


        messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (messageBox.text.toString().trim().isNotEmpty()) {
                    send.setImageResource(R.drawable.send)
                } else {
                    send.setImageResource(R.drawable.microphone)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (messageBox.text.toString().trim().isNotEmpty()) {
                    send.setImageResource(R.drawable.send)
                } else {
                    send.setImageResource(R.drawable.microphone)
                }
            }
        })

        send.setOnClickListener{
            if(messageBox.text.toString().trim().isNotEmpty())
            {
                val message = ChatMessage(messageBox.text.toString(), 0, Date())
                // handling code for sending

                messageBox.text.clear()
            }
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        return view
    }
}
