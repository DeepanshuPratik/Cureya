package com.example.cureya.model

import androidx.annotation.Keep

@Keep
data class messageInfo (
    val sender:String, var message: String
)