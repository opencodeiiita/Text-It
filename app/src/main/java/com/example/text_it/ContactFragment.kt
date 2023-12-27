package com.example.text_it

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class ProfileEntry(
    val image: String,
    val name: String,
    val email :String,
)

class ContactAdapter(private val ContactList: List<ProfileEntry>):
    RecyclerView.Adapter<ContactAdapter.ViewHolder>()
{
    companion object{
        private var prev  : Char? = null
    }
    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val image : ShapeableImageView = item.findViewById(R.id.circularImageView1)
        val name : TextView = item.findViewById(R.id.textView11)
        val mot : TextView = item.findViewById(R.id.textView12)
        val head : TextView = item.findViewById(R.id.headingContact)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {


        val item : View = LayoutInflater.from(parent.context).inflate(R.layout.contact_entry, parent, false)

        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        val contact = ContactList[position]


        holder.name.text = contact.name
        holder.mot.text = "This is Fun"
        Log.d("Hel", contact.image)
        Glide.with(holder.itemView.context)
            .load(contact.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.image)

        if(position == 0)
        {
            Log.d("helcat", contact.name)
        }
        if (position == 0 || ContactList[position - 1].name[0] != ContactList[position].name[0]) {
            holder.head.text = ContactList[position].name[0].toString()
            holder.head.visibility = View.VISIBLE
        } else {
            holder.head.text = ""
            holder.head.visibility = View.GONE // Reset the visibility
        }
    }

    override fun getItemCount(): Int {
        return ContactList.size
    }

}
class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val db = Firebase.firestore

        val ContactList = mutableListOf<ProfileEntry>()

        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val profileData = document.data
                    val profileEntry = ProfileEntry(
                        profileData["photo"] as String,
                        profileData["name"] as String,
                        profileData["emailId"] as String
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
