package com.example.text_it.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R


class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val regBut: Button = findViewById(R.id.buttonRegister)
        backButton.setOnClickListener {
            startActivity(
                Intent(
                    this, page1::class.java
                )
            )
        }

        regBut.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterUser, baseHomeActivity::class.java
                )
            )
        }
    }
}
