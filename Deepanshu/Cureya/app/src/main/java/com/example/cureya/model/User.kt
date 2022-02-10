package com.example.cureya.model

import java.util.*

data class User(
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    val password: String?,
    val joinedCureya : Date = Date()
)
