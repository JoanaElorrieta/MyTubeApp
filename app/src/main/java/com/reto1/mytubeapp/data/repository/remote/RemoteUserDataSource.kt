package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository


class RemoteUserDataSource: BaseDataSource(), CommonUserRepository {
    override suspend fun getUserByMail(email: String) = getResult{
        RetrofitClient.apiInterface.getUserByMail(email)
    }

    override suspend fun createUser(user: User) = getResult {
        RetrofitClient.apiInterface.createUser(user)
    }

    override suspend fun updateUser(email: String, user: User) = getResult {
        RetrofitClient.apiInterface.updateUser(email, user)
    }
}