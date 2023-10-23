package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.utils.Resource

interface CommonUserRepository {
    suspend fun getUserByMail(email: String) : Resource<User>
    suspend fun createUser(user: User) : Resource<Integer>
    suspend fun updateUser(email: String, user: User) : Resource<Integer>

}