package com.example.text_it.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker

class UserNameActivity:AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)


        auth = Firebase.auth

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        val username: EditText = findViewById(R.id.editTextUsername)
        val phoneNumber: EditText = findViewById(R.id.editTextPhone)

        val next : Button = findViewById<Button>(R.id.buttonContinue)
        val ccp: CountryCodePicker = findViewById(R.id.ccp)

        ccp.registerCarrierNumberEditText(phoneNumber)



        next.setOnClickListener {
            if (username.text.toString().trim() == "" ) {
                Toast.makeText(
                    baseContext, "Please enter your username",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            if(phoneNumber.text.toString().trim() == "")
            {
                Toast.makeText(
                    baseContext, "Please enter your Phone Number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!ccp.isValidFullNumber) {
                Toast.makeText(
                    baseContext, "Please enter a valid phone number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()
            val currentUser = auth.currentUser
            val user = hashMapOf(
                "username" to username.text.toString(),
                "phone" to ccp.fullNumberWithPlus
            )

            db.collection("USERS").document(currentUser!!.uid).update(user as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(
                        baseContext, "User Details Saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, PhotoActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        baseContext, "User Details Couldn't be saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            startActivity(Intent(this, PhotoActivity::class.java))
        }
    }
}