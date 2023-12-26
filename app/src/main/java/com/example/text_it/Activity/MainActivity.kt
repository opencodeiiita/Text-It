package com.example.text_it.Activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

data class Status(
    val statusPhoto: String = "",
    val statusUploadTime: Timestamp? = null,
    val username: String = ""
)

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var statusAdapter: StatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirestore()
        statusAdapter = StatusAdapter(mutableListOf())
        setupRecyclerView()
        getStatusFromFirestore()
    }

    private fun initFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = statusAdapter
    }

    private fun getStatusFromFirestore() {
        db.collection("status").get()
            .addOnSuccessListener { documents ->
                val statusList = mutableListOf<Status>()

                for (document in documents) {
                    val status = document.toObject(Status::class.java)
                    statusList.add(status)
                }

                updateAdapterWithStatusList(statusList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun updateAdapterWithStatusList(statusList: List<Status>) {
        statusAdapter.setStatusList(statusList)
    }
}

class StatusAdapter(private var dataSet: MutableList<Status>) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_status_bar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = dataSet.size

    fun setStatusList(newList: List<Status>) {
        dataSet.clear()
        dataSet.addAll(newList)
        notifyDataSetChanged()
    }
}
