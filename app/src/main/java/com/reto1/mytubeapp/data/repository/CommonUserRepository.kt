package com.reto1.mytubeapp.data.repository

import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.utils.Resource

interface CommonUserRepository {
    suspend fun getUserByMail(email: String, password:String) : Resource<User>
    suspend fun createUser(user: User) : Resource<Void>
    suspend fun updateUser(email: String, user: User) : Resource<Integer>

}