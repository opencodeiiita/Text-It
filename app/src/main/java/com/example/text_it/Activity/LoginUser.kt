package com.example.text_it.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.text_it.R

class LoginUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                android.Manifest.permission.CAMERA
            ), 1)
        }

        val loginBut: Button = findViewById(R.id.buttonLogin)
        val regBut: Button = findViewById(R.id.buttonRegister)

        regBut.setOnClickListener {
            startActivity(
                Intent(
                    this, RegisterUser::class.java
                )
            )
        }

        loginBut.setOnClickListener {
//            implement the logic for logging in the user using firebase

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