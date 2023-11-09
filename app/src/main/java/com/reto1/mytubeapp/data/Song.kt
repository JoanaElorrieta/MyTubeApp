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

/*
data class Song (val id: Int, val title: String, val author: String, val url: String, var views:Int
):
    Parcelable {
    constructor(
        title: String,
        author: String,
        url: String

    ): this(0, title, author, url,0)
    constructor(
        title: String,
        author: String,
        url: String,
        views: Int

    ): this(0, title, author, url,views)

}

 */