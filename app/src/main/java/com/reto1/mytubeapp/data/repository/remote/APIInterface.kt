package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.Song
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIInterface {
    @GET("/songs")
    suspend fun getSongs(): Response<List<Song>>
    @POST("/songs")
    suspend fun createSong(@Body song: Song): Response<Integer>
    @PUT("/songs/{id}")
    suspend fun updateSong(@Path("id") id: Int, @Body song: Song): Response<Integer>
    @DELETE("/songs/{id}")
    suspend fun deleteSong(@Path("id") id: Int): Response<Integer>
    @GET("/songs/{id}")
    suspend fun getSongById(@Path("id") id: Int): Response<Song>
}