package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.ChangePasswordRequest
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.utils.Resource

interface CommonUserRepository {
    suspend fun createUser(user: User): Resource<Void>
    suspend fun updateUser(changePasswordRequest: ChangePasswordRequest): Resource<Void>

    suspend fun signIn(user: User): Resource<Integer>
    suspend fun login(authRequest: AuthRequest): Resource<User>
    suspend fun getUserInfo(): Resource<User>
}