package com.example.text_it.dataClass

import com.google.firebase.Timestamp

data class Status_dataclass(
    var statusPhoto: String = "",
    var statusUploadTime: Timestamp? = null,
    var username: String = ""
)