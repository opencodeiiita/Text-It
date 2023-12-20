package com.example.text_it.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.text_it.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val iv_logo: ImageView = findViewById(R.id.iv_logo)

        iv_logo.alpha = 0f
        iv_logo.animate().setDuration(3000).alpha(1f).withEndAction {
            val i = Intent(this, LoginUser::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        Handler().postDelayed({
            val i = Intent(this, LoginUser::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
