package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangePasswordRequest(
    val email: String,
    val oldPassword: String,
    val password: String
):Parcelable
