package com.reto1.mytubeapp

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.ui.user.RegisterActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val USER_REQUEST_CODE = 1
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //espera y muestra login
        handler.postDelayed({ logIn() }, 3000)

        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, USER_REQUEST_CODE)
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
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == USER_REQUEST_CODE && resultCode == RESULT_OK) {
            val user = data?.getParcelableExtra<User>("user")
            Log.i("Main", ""+user)
            if (user != null) {
                findViewById<EditText>(R.id.email).setText(user.email)
                findViewById<EditText>(R.id.password).setText(user.password)
            }
        }
    }
    fun logIn() {
        findViewById<ImageView>(R.id.logoInicio).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.logoText).visibility = View.VISIBLE
        findViewById<TextView>(R.id.password).visibility = View.VISIBLE
        findViewById<TextView>(R.id.email).visibility = View.VISIBLE
        findViewById<Button>(R.id.login).visibility = View.VISIBLE
        findViewById<Button>(R.id.rememberMe).visibility = View.VISIBLE
        findViewById<Button>(R.id.register).visibility = View.VISIBLE
        findViewById<Button>(R.id.notNow).visibility = View.VISIBLE
    }


}
