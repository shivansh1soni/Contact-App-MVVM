package com.example.contactappmvvm

import android.net.Uri

data class Contact(
    val image: Uri?,
    val name: String,
    val phone: String,
)