package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite (
    val id_song: Int,
    val id_user: Int,
   val favorite: Boolean,
): Parcelable