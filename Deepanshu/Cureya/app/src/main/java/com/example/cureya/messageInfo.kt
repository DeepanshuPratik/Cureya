package com.example.cureya

import androidx.annotation.Keep

@Keep
data class messageInfo (
    val sender:String, var message: String
)