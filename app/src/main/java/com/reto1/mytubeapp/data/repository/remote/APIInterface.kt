package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.ChangePasswordRequest
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.User
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
    suspend fun createSong(@Body song: Song): Response<Void>
    @PUT("/songs/{id}")
    suspend fun updateSong(@Path("id") id: Int, @Body song: Song): Response<Void>
    @DELETE("/songs/{id}")
    suspend fun deleteSong(@Path("id") id: Int): Response<Integer>
    @GET("/songs/{id}")
    suspend fun getSongById(@Path("id") id: Int): Response<Song>
    @GET("/songs/user")
    suspend fun getSongsFavoriteViews(): Response<List<Song>>
    @POST("/songs/{idSong}/play")
    suspend fun insertNumberViews(@Path("idSong") idSong:Int): Response<Void>
    @GET("/songs/{idSong}/play")
    suspend fun selectNumberViews(@Path("idSong") idSong:Int): Response<Integer>

    @POST("/users")
    suspend fun createUser(@Body user: User): Response<Void>
    @PUT("/users/update")
    suspend fun updateUser(@Body changePasswordRequest: ChangePasswordRequest): Response<Void>
    @PUT("/songs/{idSong}/play")
    suspend fun updateNumberViews(@Path("idSong") idSong:Int): Response<Void>
    @POST("/users/favorite/{idSong}")
    suspend fun createFavorite(@Path("idSong") idSong:Int): Response<Void>
    @DELETE("/users/favorite/{idSong}")
    suspend fun deleteFavorite(@Path("idSong") idSong:Int): Response<Integer>

    @POST("/auth/signup")
    suspend fun signIn(@Body user:User): Response<Integer>
    @POST("/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<User>
    @GET("/auth/me")
    suspend fun getUserInfo(): Response<User>

}