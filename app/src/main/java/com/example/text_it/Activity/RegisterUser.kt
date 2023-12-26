package com.example.text_it.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        auth = FirebaseAuth.getInstance()

        val backButton: ImageButton = findViewById(R.id.backButton)
        val regBut: Button = findViewById(R.id.buttonRegister)

        val name: EditText = findViewById(R.id.editTextName)
        val email: EditText = findViewById(R.id.editTextEmail)
        val password: EditText = findViewById(R.id.editTextPassword)
        val confirmPassword: EditText = findViewById(R.id.editTextConfirmPassword)

        backButton.setOnClickListener {
            startActivity(
                Intent(
                    this, page1::class.java
                )
            )
        }

        regBut.setOnClickListener {
            var doLogin = false
            if (password.text.toString() == confirmPassword.text.toString()) {
                doLogin = true
            } else {
                Toast.makeText(
                    baseContext, "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (name.text.toString() == "" || email.text.toString() == "" || password.text.toString() == "" || confirmPassword.text.toString() == "") {
                Toast.makeText(
                    baseContext, "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                doLogin = false
            }
            if (doLogin) {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                        }
                    }
                    .addOnFailureListener(this) { exception ->
                        Toast.makeText(
                            baseContext, "Registration failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                val user = auth.currentUser
                user?.updateProfile(
                    user.let {
                        val profileUpdates = it.displayName.let { it1 ->
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(name.text.toString())
                                .build()
                        }
                        profileUpdates
                    }
                )?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                        Toast.makeText(
                            baseContext, "User successfully registered",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@RegisterUser, baseHomeActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}
