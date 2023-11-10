package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.remote.RetrofitClient
import com.reto1.mytubeapp.utils.Resource

interface CommonSongRepository {
    suspend fun getSongs() : Resource<List<Song>>
    suspend fun getSongsFavoriteViews(idUser: Int) : Resource<List<Song>>
    suspend fun createSong(song: Song) : Resource<Void>
    suspend fun updateSong(id: Int, song: Song) : Resource<Void>
    suspend fun deleteSong(id: Int) : Resource<Integer>
    suspend fun getSongById(id: Int) : Resource<Song>
    suspend fun updateNumberViews(idUser: Int, idSong: Int): Resource<Void>
    suspend fun createFavorite(idUser: Int, idSong: Int): Resource<Void>
    suspend fun deleteFavorite(idUser: Int, idSong: Int): Resource<Integer>
    suspend fun insertNumberViews(idUser: Int, idSong: Int): Resource<Void>
    suspend fun selectNumberViews(idUser: Int, idSong: Int): Resource<Integer>
}