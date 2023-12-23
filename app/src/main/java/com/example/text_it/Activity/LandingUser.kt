package com.example.text_it.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.text_it.R

class LandingUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_user)

        val signupButton: Button = findViewById(R.id.signupButton)
        val loginButton: TextView = findViewById(R.id.textViewLogin)

        signupButton.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this, RegisterUser::class.java
                )
            )
        }

        loginButton.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this, LoginUser::class.java
                )
            )
        }


    }
}