package com.reto1.mytubeapp

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.remote.RemoteUserDataSource
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.ui.user.RegisterActivity
import com.reto1.mytubeapp.ui.user.UserAdapter
import com.reto1.mytubeapp.ui.user.UserViewModel
import com.reto1.mytubeapp.ui.user.UserViewModelFactory
import com.reto1.mytubeapp.utils.Resource
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val USER_REQUEST_CODE = 1
    private lateinit var userAdapter: UserAdapter
    private val userRepository = RemoteUserDataSource()

    private val viewModel: UserViewModel by viewModels { UserViewModelFactory(userRepository) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userAdapter = UserAdapter()

        //espera y muestra login
        handler.postDelayed({ logIn() }, 3000)

        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, USER_REQUEST_CODE)
        }
        findViewById<Button>(R.id.login).setOnClickListener {
            var email = findViewById<EditText>(R.id.email).text.toString()
            var password = findViewById<EditText>(R.id.password).text.toString()
            if(checkData()){
                viewModel.onSearchUser(email, password)
            }


        }
        findViewById<Button>(R.id.notNow).setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
        viewModel.found.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val intent = Intent(this, SongActivity::class.java)
                    startActivity(intent)
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, "El login no es correcto", Toast.LENGTH_LONG)
                        .show()
                }

                Resource.Status.LOADING -> {

                }
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == USER_REQUEST_CODE && resultCode == RESULT_OK) {
            val user = data?.getParcelableExtra<User>("user")
            Log.i("Main", "" + user)
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

    fun checkData(): Boolean {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_LONG).show()
            return false
        }
        val emailCorrecto = validarEmail(email)
        if (!emailCorrecto) {
            Toast.makeText(this, "El correo tiene un formato erróneo", Toast.LENGTH_LONG).show()
            return false
        }


        if (password.length >= 8) {
            return true
        } else {
            Toast.makeText(
                this,
                "La contraseña debe tener 8 caracteres o más",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

    }

    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        return pattern.matcher(email).matches()
    }

}
