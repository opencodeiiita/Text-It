package com.example.text_it.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.text_it.R

class SettingsHelp : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helpBackBtn = view.findViewById<ImageButton>(R.id.helpBack)
        val fragmentManager = requireActivity().supportFragmentManager


        helpBackBtn?.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Setting())
                .addToBackStack(null)
                .commit()
        }
    }
}