package com.reto1.mytubeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val listSongFavs: List<Song>,
    val views: List<Integer>
): Parcelable {
    constructor(
        name: String,
        lastName: String,
        email: String,
        password: String
    ) : this(
        id = 0, // valor por defecto
        name = name,
        lastName = lastName,
        email = email,
        password = password,
        listSongFavs = emptyList(),
        views = emptyList()
    )
}
