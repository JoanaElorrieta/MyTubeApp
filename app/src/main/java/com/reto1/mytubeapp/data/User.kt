package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: Int,
    val name: String,
    val last_name: String,
    val email: String,
    val password: String,
): Parcelable