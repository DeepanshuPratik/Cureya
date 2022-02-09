package com.example.cureya.community.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class User(
    val userId: String = "",
    val photoUrl: String = "",
) : Parcelable