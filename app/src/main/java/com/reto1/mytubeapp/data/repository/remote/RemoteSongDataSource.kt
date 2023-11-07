package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.CommonSongRepository

class RemoteSongDataSource : BaseDataSource(), CommonSongRepository {
    override suspend fun getSongs() = getResult {
        RetrofitClient.apiInterface.getSongs()
    }

    override suspend fun createSong(song: Song) = getResult {
        RetrofitClient.apiInterface.createSong(song)
    }

    override suspend fun updateSong(id:Int, song: Song) = getResult {
        RetrofitClient.apiInterface.updateSong(id,song)
    }
    override suspend fun deleteSong(id:Int) = getResult {
        RetrofitClient.apiInterface.deleteSong(id)
    }

    override suspend fun getSongById(id: Int) = getResult {
        RetrofitClient.apiInterface.getSongById(id)
    }
    override suspend fun updateNumberViews(idUser:Int, idSong:Int) = getResult {
        RetrofitClient.apiInterface.updateNumberViews(idUser, idSong)
    }
}