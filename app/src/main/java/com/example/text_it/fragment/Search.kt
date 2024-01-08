package com.example.text_it.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.text_it.R
import com.google.firebase.firestore.FirebaseFirestore

class Search : Fragment() {

    private lateinit var adapter: CallSearchAdapter

    private val callList = mutableListOf<CallInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCallList)
        adapter = CallSearchAdapter(callList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchDataFromFirebase()

        val crossButton: ImageView = view.findViewById(R.id.imageButtonCross)
        crossButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val editTextSearch: EditText = view.findViewById(R.id.editTextSearch)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCallList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun filterCallList(query: String) {
        val filteredList = mutableListOf<CallInfo>()
        for (call in callList) {
            if (call.name.contains(query, ignoreCase = true)) {
                filteredList.add(call)
            }
        }
        adapter.updateList(filteredList)
    }

    private fun fetchDataFromFirebase() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("USERS").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.data["name"].toString()
                    val call = CallInfo(name)
                    callList.add(call)
                }
                adapter.updateList(callList)
            }
            .addOnFailureListener { exception ->
                Log.d("Search", "Error getting documents: ", exception)
            }
    }
}

class CallSearchAdapter(private var callList: List<CallInfo>) :
    RecyclerView.Adapter<CallSearchAdapter.CallViewHolder>() {
    fun updateList(newList: List<CallInfo>) {
        callList = newList
        notifyDataSetChanged()
    }

    class CallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_call_item, parent, false)
        return CallViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = callList[position]
        holder.textViewName.text = call.name
    }

    override fun getItemCount(): Int {
        return callList.size
    }
}
