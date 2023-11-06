package com.reto1.mytubeapp.data.repository.remote

import android.util.Log
import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository
import com.reto1.mytubeapp.utils.Resource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


class RemoteUserDataSource: BaseDataSource(), CommonUserRepository {
    override suspend fun getUserByMail(email: String, password:String) = getResult{
        RetrofitClient.apiInterface.getUserByMail(email,password)
    }


    override suspend fun createUser(user: User) = getResult {
        RetrofitClient.apiInterface.createUser(user)
    }


    override suspend fun updateUser(email: String, password:String) = getResult {
        RetrofitClient.apiInterface.updateUser(email, password)
    }

    override suspend fun updateNumberViews(idUser:Int, idSong:Int) = getResult {
        RetrofitClient.apiInterface.updateNumberViews(idUser, idSong)
    }
    override suspend fun signIn(user:User)= getResult{
        RetrofitClient.apiInterface.signIn(user)
    }
    override suspend fun login(authRequest: AuthRequest)= getResult{

        RetrofitClient.apiInterface.login("Bearer: askfhsuifhw38y948twe98chw89r39583thguifcbil8vryiyrtv4tyw37ry3", authRequest)
    }
}