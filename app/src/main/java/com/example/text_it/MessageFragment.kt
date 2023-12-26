package com.example.text_it

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

data class Status(
    val statusPhoto: String = "",
    val statusUploadTime: Timestamp? = null,
    val username: String = ""
)

class MessageFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var statusAdapter: StatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFirestore()
        statusAdapter = StatusAdapter(mutableListOf())
        setupRecyclerView()
        getStatusFromFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }


    private fun initFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupRecyclerView() {
        // val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        // recyclerView.adapter = statusAdapter
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
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
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
        // dummy view
        val view = View(parent.context)
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