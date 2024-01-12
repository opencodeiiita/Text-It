package com.example.text_it.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R

// #1

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val iv_logo: ImageView = findViewById(R.id.iv_logo)
        iv_logo.alpha = 0f

        requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.CALL_PHONE,
                ), 1
            )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED && grantResults[1] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val iv_logo: ImageView = findViewById(R.id.iv_logo)
            iv_logo.animate().setDuration(SPLASH_TIME_OUT).alpha(1f).withEndAction {
                startActivity(Intent(this, LandingUser::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        } else {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
