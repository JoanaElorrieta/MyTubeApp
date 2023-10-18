package com.reto1.mytubeapp.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.song.SongActivity

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        findViewById<Button>(R.id.back).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }
}