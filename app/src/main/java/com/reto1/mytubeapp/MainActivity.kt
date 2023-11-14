package com.reto1.mytubeapp

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import com.reto1.mytubeapp.ui.user.LogInActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //espera y muestra login
        handler.postDelayed({ logIn() }, 3000)

    }

    private fun logIn() {
       val intent=Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
