package com.example.cureya.community.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString(): String {
    val formatter = SimpleDateFormat("hh:mm, dd MMM", Locale.getDefault());
    return formatter.format(this)
}

const val defaultProfilePic = "https://firebasestorage.googleapis.com/v0/b/cureyadraft.appspot.com/o/static%2Fdefault_profile_pic.png?alt=media&token=a52249e9-4de2-4e67-8e73-fa71ff26f289"