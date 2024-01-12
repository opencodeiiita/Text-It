package com.example.text_it.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.databinding.FragmentUserProjectBinding

class UserProject : Fragment() {
    private lateinit var binding: FragmentUserProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val name = arguments?.getString("name")
////        val img = arguments?.getString("img")
//        val email = arguments?.getString("email")
//
//        binding.editTextText.text=name
//        binding.editTextText2.text= email
//        binding.textViewNameHeader1.text=name
//        binding.textViewNameHeader3.text=email

//        Glide.with(requireContext())
//            .load(img)
//            .circleCrop()
//            .into(binding.imageView3)
    }
}