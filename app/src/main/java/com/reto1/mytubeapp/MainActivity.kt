package com.reto1.mytubeapp

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView


class MainActivity :  AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler.postDelayed({ logIn() }, 3000)


        }
    fun logIn(){
        Log.d("Etiqueta", "HOLA")
        findViewById<ImageView>(R.id.logoInicio).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.logoText).visibility = View.VISIBLE
        findViewById<TextView>(R.id.password).visibility = View.VISIBLE
        findViewById<TextView>(R.id.user).visibility = View.VISIBLE
        findViewById<Button>(R.id.login).visibility = View.VISIBLE
        findViewById<Button>(R.id.rememberMe).visibility = View.VISIBLE
        findViewById<Button>(R.id.register).visibility = View.VISIBLE
        findViewById<Button>(R.id.notNow).visibility = View.VISIBLE
    }
    }
