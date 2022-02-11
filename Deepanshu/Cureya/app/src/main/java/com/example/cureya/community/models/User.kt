package com.example.cureya.community.models

import android.os.Parcelable
import com.example.cureya.community.utils.defaultProfilePic
import kotlinx.parcelize.Parcelize


@Parcelize
class User(
    var userId: String = "",
    val photoUrl: String = defaultProfilePic,
    val name : String = "null",
    val email : String = "null"
) : Parcelable