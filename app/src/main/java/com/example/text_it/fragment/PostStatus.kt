package com.example.text_it.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.dataClass.Status_dataclass
import com.example.text_it.databinding.FragmentPostStatusBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.Date

class PostStatus : Fragment() {
    private var status = Status_dataclass()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentPostStatusBinding.inflate(layoutInflater)

        val selectedImageUri = arguments?.getString("selectedImageUri")
        Log.d("PostStatus", "Selected media URI $selectedImageUri")

        // Load the image into the ImageView using Glide
        if (!selectedImageUri.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(selectedImageUri)
                .into(view.imageViewAddedStatus)
        }

        val currentUser = Firebase.auth.currentUser
        val storageRef = Firebase.storage.reference

        view.buttonAddStatus.setOnClickListener {
            val imageRef =
                storageRef.child("status/${currentUser!!.uid}-${System.currentTimeMillis()}.jpg")
            val uploadTask = imageRef.putFile(selectedImageUri!!.toUri())

            uploadTask.addOnSuccessListener {
                Log.d("PostStatus", "Successfully uploaded image: ${it.metadata?.path}")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("PostStatus", "File location: $uri")
                    addStatusToFirestore(uri.toString())
                }
            }.addOnFailureListener {
                Log.d("PostStatus", "Failed to upload image to storage: $it")
            }
        }

        return view.root
    }

    private fun addStatusToFirestore(statusPhoto: String) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser
        status.statusPhoto = statusPhoto
        status.username = currentUser!!.displayName ?: ""
        status.statusUploadTime = com.google.firebase.Timestamp(Date())

        db.collection("status")
            .add(status)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                Toast.makeText(context, "Status added successfully", Toast.LENGTH_SHORT).show()
                val fragment = Message()
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error writing document", e)
                Toast.makeText(context, "Failed to add status", Toast.LENGTH_SHORT).show()
            }
    }
}