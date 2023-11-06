package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.remote.RetrofitClient
import com.reto1.mytubeapp.utils.Resource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonUserRepository {
    suspend fun getUserByMail(email: String, password: String): Resource<User>
    suspend fun createUser(user: User): Resource<Void>
    suspend fun updateUser(email: String, password: String): Resource<Void>
    suspend fun updateNumberViews(idUser: Int, idSong: Int): Resource<Integer>
    suspend fun signIn(user: User): Resource<Integer>
    suspend fun login(authRequest: AuthRequest): Resource<User>
}