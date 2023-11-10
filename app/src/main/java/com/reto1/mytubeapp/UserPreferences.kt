package com.reto1.mytubeapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.User

class UserPreferences() {
    private val sharedPreferences: SharedPreferences by lazy{
        MyTube.context.getSharedPreferences(MyTube.context.packageName, Context.MODE_PRIVATE)
    }
    companion object {
        const val USER_TOKEN="user_token"
        const val USER_INFO="user_info"
    }
    fun saveAuthToken(token:String){
        val editor=sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    fun fetchAuthToken():String?{
        return sharedPreferences.getString(USER_TOKEN,null)
    }

    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString(USER_INFO, userJson)
        editor.apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_INFO, null)
        if (userJson != null) {
            val gson = Gson()
            return gson.fromJson(userJson, User::class.java)
        }
        return null
    }
}