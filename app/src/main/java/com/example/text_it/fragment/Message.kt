package com.example.text_it.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.text_it.adapater.StatusAdapter
import com.example.text_it.dataClass.Status_dataclass
import com.example.text_it.databinding.FragmentMessageBinding
import com.google.firebase.firestore.FirebaseFirestore


class Message : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var statusAdapter: StatusAdapter
    private  lateinit var dataSet: MutableList<Status_dataclass>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMessageBinding.inflate(layoutInflater)

        initFirestore()
        dataSet = mutableListOf()
        statusAdapter = StatusAdapter(dataSet,context)
        setupRecyclerView()
        getStatusFromFirestore()

        return binding.root
    }


    private fun initFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupRecyclerView() {

        binding.statusRV?.setHasFixedSize(true)
        binding.statusRV?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.statusRV?.adapter = statusAdapter

    }

    private fun getStatusFromFirestore() {
        db.collection("status").get()
            .addOnSuccessListener { documents ->
                val statusList = mutableListOf<Status_dataclass>()

                for (document in documents) {
                    val status = document.toObject(Status_dataclass::class.java)
                    statusList.add(status)

                }
                updateAdapterWithStatusList(statusList)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun updateAdapterWithStatusList(statusList: List<Status_dataclass>) {
        statusAdapter.setStatusList(statusList)
    }


}

