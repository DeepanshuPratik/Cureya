package com.example.cureya.chatbot

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.util.*

object Time {

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun timeStamp(): String {

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}