package com.example.text_it.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.text_it.R

class LandingUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_user)

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                android.Manifest.permission.CAMERA
            ), 1)
        }

        val signupButton: Button = findViewById(R.id.signupButton)
        val loginButton: LinearLayout = findViewById(R.id.loginLayout)

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != android.content.pm.PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}