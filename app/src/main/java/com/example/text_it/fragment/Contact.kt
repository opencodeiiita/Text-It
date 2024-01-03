package com.example.text_it.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.example.text_it.adapater.ContactAdapter
import com.example.text_it.dataClass.ProfileEntry
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Contact : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val db = Firebase.firestore

        val ContactList = mutableListOf<ProfileEntry>()

        db.collection("USERS")
            .get()
            .addOnSuccessListener { documents ->
                Log.d(documents.toString(), "DocumentSnapshot successfully read!")
                for (document in documents) {
                    val profileData = document.data
                    val profileEntry = ProfileEntry(
                        profileData["profileImage"] as String,
                        profileData["name"] as String,
                        profileData["email"] as String
                    )
                    ContactList.add(profileEntry)
                }
                ContactList.sortBy { it.name }
                val recycler = view.findViewById<RecyclerView>(R.id.recycle)
                val adapter = ContactAdapter(ContactList)

                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(view.context)
            }
        return view
    }
}
