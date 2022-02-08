package com.example.cureya.community.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString(): String {
    val formatter = SimpleDateFormat("hh:mm, dd MMM", Locale.getDefault());
    return formatter.format(this)
}