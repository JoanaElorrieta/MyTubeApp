package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthRequest(
    val email: String,
    val password: String
) : Parcelable
