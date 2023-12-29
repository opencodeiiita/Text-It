package com.example.text_it.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class OTPVerification : AppCompatActivity() {

    private lateinit var pinView: PinView
    private lateinit var buttonVerify: Button
    private lateinit var textViewPhoneNo: TextView

    private lateinit var verificationId: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)

        pinView = findViewById(R.id.pinView)
        buttonVerify = findViewById(R.id.buttonVerify)
        textViewPhoneNo = findViewById(R.id.textViewPhoneNo)

        auth = FirebaseAuth.getInstance()

        verificationId = intent.getStringExtra("verificationId") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        textViewPhoneNo.text = phoneNumber

        buttonVerify.setOnClickListener {
            val otp = pinView.text.toString()

            if (otp.length == 6) {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithPhoneAuthCredential(credential, name, email, password)
            } else {
                Toast.makeText(this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, name: String, email: String, password: String) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Update user profile with name and email
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user!!.updateProfile(profileUpdates)
                    user!!.updateEmail(email)
                    user!!.updatePassword(password)
                    val db = FirebaseFirestore.getInstance()
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "uid" to user.uid,
                        "profileImage" to ""
                    )
                    db.collection("USERS").document(user.uid).set(userMap)
                    Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this, Onboarding::class.java
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}