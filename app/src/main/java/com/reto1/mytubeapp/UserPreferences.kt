package com.reto1.mytubeapp

import android.content.Context
import android.content.SharedPreferences

class UserPreferences() {
    private val sharedPreferences: SharedPreferences by lazy{
        MyTube.context.getSharedPreferences(MyTube.context.packageName, Context.MODE_PRIVATE)
    }
    companion object {
        const val USER_TOKEN="user_token"
    }
    fun saveAuthToken(token:String){
        val editor=sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    fun fetchAuthToken():String?{
        return sharedPreferences.getString(USER_TOKEN,null)
    }
}