package com.example.text_it.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.text_it.Activity.LandingUser
import com.example.text_it.R
import com.google.firebase.auth.FirebaseAuth

class Setting : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signOutButton = view.findViewById<ConstraintLayout>(R.id.signOutBtn)

        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()

        Toast.makeText(
            context,
            "SignOut Successful",
            Toast.LENGTH_SHORT,
        ).show()

        startActivity(
            android.content.Intent(
                context, LandingUser::class.java
            )
        )

    }


}
