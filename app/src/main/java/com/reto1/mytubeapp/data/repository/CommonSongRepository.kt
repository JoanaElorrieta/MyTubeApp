package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.utils.Resource

interface CommonSongRepository {
    suspend fun getSongs() : Resource<List<Song>>
    suspend fun getSongsFavoriteViews() : Resource<List<Song>>
    suspend fun createSong(song: Song) : Resource<Void>
    suspend fun updateSong(id: Int, song: Song) : Resource<Void>
    suspend fun deleteSong(id: Int) : Resource<Integer>
    suspend fun getSongById(id: Int) : Resource<Song>
    suspend fun updateNumberViews(idSong: Int): Resource<Void>
    suspend fun createFavorite(idSong: Int): Resource<Void>
    suspend fun deleteFavorite(idSong: Int): Resource<Integer>
    suspend fun insertNumberViews(idSong: Int): Resource<Void>
    suspend fun selectNumberViews(idSong: Int): Resource<Integer>
}