package com.example.text_it.dataClass

import java.util.Date

data class MessageEntry(
    val senderName: String,
    val userId: String,
    val lastMessage: String,
    val date: Date,
    val profileImage: String,
)
