package com.example.text_it.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class Onboarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val editBtn: ImageButton = findViewById(R.id.imageButtonEdit)
        val profileImage : ShapeableImageView = findViewById(R.id.imageViewProfile)
        val nextBtn: Button = findViewById(R.id.buttonNext)
        val backBtn: ImageButton = findViewById(R.id.backButton)

        val storage = Firebase.storage
        val storageRef = storage.reference
        val user = Firebase.auth.currentUser

        if (user?.photoUrl != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .into(profileImage)
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected media URI $uri")
                val imageRef = storageRef.child("images/${uri.lastPathSegment}")
                val uploadTask = imageRef.putFile(uri)
                uploadTask.addOnFailureListener {
                    Log.d("PhotoPicker", "Upload failed")
                    Log.d("PhotoPicker", it.toString())
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    Toast.makeText(this, "Upload success", Toast.LENGTH_SHORT).show()
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(this)
                            .load(uri)
                            .into(profileImage)
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build()
                        val db = FirebaseFirestore.getInstance()
                        val userRef = db.collection("USERS").document(user!!.uid)
                        userRef.update("profileImage", uri.toString())
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("PhotoPicker", "User profile updated.")
                                }
                            }
                    }
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        editBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        backBtn.setOnClickListener {
            finish()
        }

        nextBtn.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this, baseHomeActivity::class.java
                )
            )
        }
    }
}