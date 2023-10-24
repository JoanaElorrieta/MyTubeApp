package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.utils.Resource

interface CommonSongRepository {
    suspend fun getSongs() : Resource<List<Song>>
    suspend fun createSong(song: Song) : Resource<Void>
    suspend fun updateSong(id: Int, song: Song) : Resource<Void>
    suspend fun deleteSong(id: Int) : Resource<Integer>
    suspend fun getSongById(id: Int) : Resource<Song>

}