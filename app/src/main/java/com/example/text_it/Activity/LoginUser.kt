package com.example.text_it.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.text_it.R

class LoginUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val loginBut: Button = findViewById(R.id.buttonLogin)
        val backBut: ImageButton = findViewById(R.id.backButton)


        loginBut.setOnClickListener {
//            implement the logic for logging in the user using firebase
        }

        backBut.setOnClickListener {
            finish()
        }
    }
}