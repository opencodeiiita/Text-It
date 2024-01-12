package com.example.text_it.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.text_it.R
import com.example.text_it.databinding.FragmentStatusViewBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Timestamp

class StatusView : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private val closeDelay = 5000L // 5 seconds delay

    private val closeRunnable = Runnable {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private lateinit var binding: FragmentStatusViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusViewBinding.inflate(layoutInflater)

        val selectedImageUri = arguments?.getString("selectedImageUri")
        val username = arguments?.getString("username")
        val timestamp = arguments?.getString("timestamp")

        if (!selectedImageUri.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(selectedImageUri)
                .into(binding.imageViewAddedStatus)
        }
        binding.textViewUsername.text = username

        val timestampHumanReadable: String = TimeAgo.using(timestamp!!.toLong() * 1000)
        binding.textViewTimestamp.text = timestampHumanReadable

        handler.postDelayed(closeRunnable, closeDelay)

        // Set a click listener for the Close button
        binding.buttonCloseStatus.setOnClickListener {
            handler.removeCallbacks(closeRunnable)
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        // Remove the callback when the view is destroyed
        handler.removeCallbacks(closeRunnable)
        super.onDestroyView()
    }
}