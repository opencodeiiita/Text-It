package com.example.text_it.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.Message
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.text_it.Call
import com.example.text_it.ContactFragment
import com.example.text_it.MessageFragment
import com.example.text_it.R
import com.example.text_it.Setting
import com.google.android.material.bottomnavigation.BottomNavigationView

// this is the activity where the bottom navigation code is there
// the job of this activity is to hold the bottom nav bar and fragments

class baseHomeActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        // Set the initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MessageFragment())
            .commit()

        // Handle bottom navigation item clicks
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
//            if (menuItem.isChecked) {
//                return@setOnNavigationItemSelectedListener false
//            }

//            for (i in 0 until bottomNavView.menu.size()) {
//                val ithItem = bottomNavView.menu.getItem(i)
//                ithItem.icon?.setTintList(getColorStateList(R.color.bottom_navbar_not_selected))
//            }

            when (menuItem.itemId) {
                R.id.message -> replaceFragment(MessageFragment())
                R.id.call -> replaceFragment(Call())
                R.id.contact -> replaceFragment(ContactFragment())
                R.id.setting -> replaceFragment(Setting())
            }

//            menuItem.icon?.setTintList(getColorStateList(R.color.bottom_navbar_selected))

            true
        }




    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}