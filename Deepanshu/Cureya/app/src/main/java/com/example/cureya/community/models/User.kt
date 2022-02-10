package com.example.cureya.community.models

import android.os.Parcelable
import com.example.cureya.community.utils.defaultProfilePic
import kotlinx.parcelize.Parcelize


@Parcelize
class User(
    val userId: String = "",
    val photoUrl: String = defaultProfilePic,
) : Parcelable