package com.reto1.mytubeapp.data.repository.remote

import android.util.Log
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository
import com.reto1.mytubeapp.utils.Resource
import retrofit2.http.Path


class RemoteUserDataSource: BaseDataSource(), CommonUserRepository {
    override suspend fun getUserByMail(email: String, password:String) = getResult{
        RetrofitClient.apiInterface.getUserByMail(email,password)
    }


    override suspend fun createUser(user: User) = getResult {
        RetrofitClient.apiInterface.createUser(user)
    }

    override suspend fun updateUser(email: String, user: User) = getResult {
        RetrofitClient.apiInterface.updateUser(email, user)
    }

    override suspend fun updateNumberViews(idUser:Int, idSong:Int) = getResult {
        RetrofitClient.apiInterface.updateNumberViews(idUser, idSong)
    }
}