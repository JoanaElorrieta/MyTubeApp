package com.reto1.mytubeapp

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.ui.user.RegisterActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler.postDelayed({ logIn() }, 3000)

        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.login).setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.notNow).setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }

    fun logIn(){
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
