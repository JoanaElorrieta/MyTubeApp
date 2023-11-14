package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Int = 0,
    val title: String,
    val author: String,
    val url: String,
    var favorite: Int = 0,
    var views: Int = 0
) : Parcelable