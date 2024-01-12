package com.example.text_it.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.text_it.Activity.LandingUser
import com.example.text_it.Activity.PhotoActivity
import com.example.text_it.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Setting : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        val profileImg = view?.findViewById<ShapeableImageView>(R.id.SettingsAvatar) as ShapeableImageView
        val name = view?.findViewById<TextView>(R.id.SettingsName)

        firestore.collection("USERS").document(currentUser!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name?.setText(document.data?.get("name").toString())
                    Glide.with(requireContext())
                        .load(document.data?.get("profileImage").toString())
                        .into(profileImg)
                }
            }
return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val profileButton = view.findViewById<ConstraintLayout>(R.id.profileBtn)
        val scanButton = view.findViewById<ImageButton>(R.id.scanBtn)
        val accountButton = view?.findViewById<ConstraintLayout>(R.id.accountBtn)
        val chatButton = view.findViewById<ConstraintLayout>(R.id.chatBtn)
        val notifButton = view.findViewById<ConstraintLayout>(R.id.notifBtn)
        val helpButton = view.findViewById<ConstraintLayout>(R.id.helpBtn)
        val storageButton = view.findViewById<ConstraintLayout>(R.id.storageBtn)
        val inviteButton = view.findViewById<ConstraintLayout>(R.id.inviteBtn)
        val signOutButton = view.findViewById<ConstraintLayout>(R.id.signOutBtn)

        val fragmentManager = requireActivity().supportFragmentManager


        profileButton.setOnClickListener{
            startActivity(
                android.content.Intent(
                    context, PhotoActivity::class.java
                )
            )
        }

        scanButton?.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsScan())
                .commit()
        }

        accountButton?.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsAccount())
                .commit()
        }

        chatButton.setOnClickListener {   fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsChat())
            .commit()
        }

        notifButton.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsNotif())
                .commit()
        }

        helpButton.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsHelp())
                .commit()
        }

        storageButton.setOnClickListener {   fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsStorage())
            .commit()
        }

        inviteButton.setOnClickListener {   fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsInvite())
            .commit()
        }

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
