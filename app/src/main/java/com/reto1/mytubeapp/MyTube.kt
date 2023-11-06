package com.reto1.mytubeapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration

class MyTube : Application() {
    companion object {
        lateinit var context:Context
        lateinit var userPreferences:UserPreferences
    }
   override fun onCreate(){
       super.onCreate()
       context=this
       userPreferences=UserPreferences()
   }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}