package com.example.text_it.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.text_it.adapater.MessageAdapter
import com.example.text_it.adapater.StatusAdapter
import com.example.text_it.dataClass.MessageEntry
import com.example.text_it.dataClass.Status_dataclass
import com.example.text_it.databinding.FragmentMessageBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.Date


class Message : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var statusAdapter: StatusAdapter
    private lateinit var statusDataSet: MutableList<Status_dataclass>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageDataSet: MutableList<MessageEntry>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)
        initFirestore()

        statusDataSet = mutableListOf()
        statusAdapter = StatusAdapter(statusDataSet, context)

        messageDataSet = mutableListOf()
        messageAdapter = MessageAdapter(messageDataSet)

        val storageRef = Firebase.storage.reference
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected media URI $uri")
                    // Pass the URI to the PostStatus fragment
                    val fragment = PostStatus()
                    val bundle = Bundle()
                    bundle.putString("selectedImageUri", uri.toString())
                    fragment.arguments = bundle

                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(
                        com.example.text_it.R.id.fragmentContainer,
                        fragment
                    )
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                } else {
                    Log.w("PhotoPicker", "Image picker gave us a null image.")
                }
            }


        val addStatus: ShapeableImageView = binding.shapeableImageViewAddStatus
        addStatus.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        setupStatusRecyclerView()
        setupMessageRecyclerView()

        getStatusFromFirestore()
        getMessagesFromFirestore()
        return binding.root
    }

    private fun initFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupStatusRecyclerView() {

        binding.statusRV?.setHasFixedSize(true)
        binding.statusRV?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.statusRV?.adapter = statusAdapter

    }

    private fun getStatusFromFirestore() {
        db.collection("status").get()
            .addOnSuccessListener { documents ->
                val statusList = mutableListOf<Status_dataclass>()

                for (document in documents) {
                    val status = document.toObject(Status_dataclass::class.java)
                    statusList.add(status)

                }
                updateAdapterWithStatusList(statusList)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun updateAdapterWithStatusList(statusList: List<Status_dataclass>) {
        statusAdapter.setStatusList(statusList)
    }

    private fun setupMessageRecyclerView() {
        binding.msgRV?.setHasFixedSize(true)
        binding.msgRV?.layoutManager = LinearLayoutManager(context)
        binding.msgRV?.adapter = messageAdapter

    }

    private fun getMessagesFromFirestore() {
        db.collection("USERS").get()
            .addOnSuccessListener { documents ->
                val messageList = mutableListOf<MessageEntry>()

                for (document in documents) {
                    // set random date for now
                    val name = document.get("name") as String
                    val uid = document.id
                    val lastMessage = "Something"
                    val date = Date()
                    val profileImage = document.get("profileImage") as String
                    val message = MessageEntry(name, uid, lastMessage, date, profileImage)
                    messageList.add(message)
                }
                updateAdapterWithMessageList(messageList)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun updateAdapterWithMessageList(messageList: MutableList<MessageEntry>) {
        messageAdapter.setMessageList(messageList)
    }
}

