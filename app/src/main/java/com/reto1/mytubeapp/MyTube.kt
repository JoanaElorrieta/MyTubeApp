package com.reto1.mytubeapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyTube : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        lateinit var userPreferences:UserPreferences
    }
   override fun onCreate(){
       super.onCreate()
       context=this
       userPreferences=UserPreferences()
   }

}