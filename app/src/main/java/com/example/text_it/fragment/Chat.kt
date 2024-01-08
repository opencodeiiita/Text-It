package com.example.text_it.fragment

import ChatMessage
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.IBinder.DeathRecipient
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.adapater.ChatAdapter
import com.example.text_it.adapater.ContactAdapter
import com.example.text_it.adapater.MessageAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Chat(roomId: String, recipientId: String) : Fragment() {

    private lateinit var realtimeDB: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private val roomId = roomId
    private val recipientId = recipientId

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val backBtn: ImageView = view.findViewById(R.id.back)
        backBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, Message())
            transaction.commit()
        }

        realtimeDB = FirebaseDatabase.getInstance().reference
        firestore = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        val profileImage: ShapeableImageView = view.findViewById(R.id.circularImageView1)
        val name: TextView = view.findViewById(R.id.textView10)

        firestore.collection("USERS").document(recipientId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name.setText(document.data?.get("name").toString())
                    Glide.with(requireContext())
                        .load(document.data?.get("profileImage").toString())
                        .into(profileImage)
                }
            }

        // Example messageList
//        val dateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.US)
//        val date1 = dateFormat.parse("January 01, 2024 10:59 PM")
//        val date2 = dateFormat.parse("January 02, 2024 11:15 AM")
//        val date3 = dateFormat.parse("January 03, 2024 02:30 PM")
//        val date4 = dateFormat.parse("January 04, 2024 02:30 PM")
//
//        val messageList = mutableListOf<ChatMessage>(
//            ChatMessage("Hello", currentUser!!, date2!!),
//            ChatMessage("Hi there", recipientId, date1!!),
//            ChatMessage("How are you?", currentUser, date1),
//            ChatMessage("I'm good, thanks!", recipientId, date3!!),
//            ChatMessage("What's up?", currentUser, date2),
//            ChatMessage("Not much", recipientId, date4!!),
//            ChatMessage(
//                "Like I had a big confusion, can you help me sort out?",
//                currentUser,
//                date4
//            ),
//            ChatMessage(
//                "I am not sure, I can try. I would recommend going to muffinBoy. That guy is great. Like I am trying to make this message long for testing and I don't know what else to type?",
//                currentUser,
//                date4
//            ),
//        )


        val messageList = mutableListOf<ChatMessage>()

        // send new message list to adapter.updateMessageList if data change
//        realtimeDB.child("rooms/$roomId").child("messages").addValueEventListener(object :
//            ValueEventListener {
//            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
//                val messages = snapshot.value as List<HashMap<String, Any>>
//                val newMessageList = mutableListOf<ChatMessage>()
//                for (message in messages) {
//                    val messageText = message["message"] as String
//                    val sender = message["sender"] as String
//                    val date = message["date"] as Long
//                    val chatMessage = ChatMessage(messageText, sender, Date(date))
//                    newMessageList.add(chatMessage)
//                }
//                val adapter = view.findViewById<RecyclerView>(R.id.messageRecyclerView).adapter as ChatAdapter
//                adapter.updateMessageList(newMessageList)
//            }
//
//            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
//        })


        val recycler = view.findViewById<RecyclerView>(R.id.messageRecyclerView)
        val adapter = ChatAdapter(messageList)


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

        send.setOnClickListener {
            if (messageBox.text.toString().trim().isNotEmpty()) {
                val message = ChatMessage(messageBox.text.toString(), currentUser!!, Date())
                adapter.addMessage(roomId, message)
                messageBox.text.clear()
            }
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        realtimeDB.child("rooms/$roomId").child("messages").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                adapter.updateMessageList(roomId)
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })

        return view
    }
}
