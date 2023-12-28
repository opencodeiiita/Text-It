package com.example.text_it.dataClass

import com.google.firebase.Timestamp

data class Status_dataclass(
    val statusPhoto: String = "",
    val statusUploadTime: Timestamp? = null,
    val username: String = ""
)