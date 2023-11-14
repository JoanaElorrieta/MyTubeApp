package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
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
    @GET("/songs/user/{id}")
    suspend fun getSongsFavoriteViews(@Path("id") id: Int): Response<List<Song>>
    @POST("/songs/{idUser},{idSong}/play")
    suspend fun insertNumberViews(@Path("idUser") idUser:Int, @Path("idSong") idSong:Int): Response<Void>
    @GET("/songs/{idUser},{idSong}/play")
    suspend fun selectNumberViews(@Path("idUser") idUser:Int, @Path("idSong") idSong:Int): Response<Integer>

    @GET("/users/{email},{password}")
    suspend fun getUserByMail(@Path("email") email: String, @Path("password") password: String): Response<User>
    @POST("/users")
    suspend fun createUser(@Body user: User): Response<Void>
    @PUT("/users/{email},{oldPassword},{password}")
    suspend fun updateUser(@Path("email") email:String, @Path("oldPassword") oldPassword:String, @Path("password") password:String): Response<Void>
    @PUT("/songs/{idUser},{idSong}/play")
    suspend fun updateNumberViews(@Path("idUser") idUser:Int, @Path("idSong") idSong:Int): Response<Void>
    @POST("/users/{idUser},{idSong}/favorite")
    suspend fun createFavorite(@Path("idUser") idUser:Int, @Path("idSong") idSong:Int): Response<Void>
    @DELETE("/users/{idUser},{idSong}/favorite")
    suspend fun deleteFavorite(@Path("idUser") idUser:Int, @Path("idSong") idSong:Int): Response<Integer>

    @POST("/auth/signup")
    suspend fun signIn(@Body user:User): Response<Integer>
    @POST("/auth/login")
    suspend fun login(@Header("Authorization") authorizationHeader: String, @Body authRequest: AuthRequest): Response<User>
    @GET("/auth/me")
    suspend fun getUserInfo(@Header("Authorization") authorizationHeader: String): Response<User>

}