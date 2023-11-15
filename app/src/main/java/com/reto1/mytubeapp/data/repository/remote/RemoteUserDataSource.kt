package com.reto1.mytubeapp.data.repository.remote

import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository

class RemoteUserDataSource: BaseDataSource(), CommonUserRepository {
    override suspend fun getUserByMail(email: String, password:String) = getResult{
        RetrofitClient.apiInterface.getUserByMail(email, password)
    }
    override suspend fun createUser(user: User) = getResult {
        RetrofitClient.apiInterface.createUser(user)
    }
    override suspend fun updateUser(authRequest: AuthRequest) = getResult {
        RetrofitClient.apiInterface.updateUser(authRequest)
    }
    override suspend fun signIn(user:User)= getResult{
        RetrofitClient.apiInterface.signIn(user)
    }
    override suspend fun login(authRequest: AuthRequest) = getResult{
        RetrofitClient.apiInterface.login(authRequest)
    }
    override suspend fun getUserInfo() = getResult{
        RetrofitClient.apiInterface.getUserInfo()
    }
}