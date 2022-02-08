package com.example.cureya.chat.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateString(): String {
    val formatter = SimpleDateFormat("HH:mm dd-MMM");
    return formatter.format(this)
}