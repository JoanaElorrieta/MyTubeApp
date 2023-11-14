package com.reto1.mytubeapp.ui.user

import android.annotation.SuppressLint
import com.reto1.mytubeapp.MyTube
import com.reto1.mytubeapp.R
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatCheckBox
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.remote.RemoteUserDataSource
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.utils.Resource
import java.util.Locale
import java.util.regex.Pattern

class LogInActivity : AppCompatActivity() {
    private val USER_REQUEST_CODE = 1
    private val USER_UPDATE_CODE = 2
    private lateinit var userAdapter: UserAdapter
    private val userRepository = RemoteUserDataSource()
    private lateinit var rememberMeCheckBox: AppCompatCheckBox

    private val viewModel: UserViewModel by viewModels { UserViewModelFactory(userRepository) }
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        userAdapter = UserAdapter()
        rememberMeCheckBox= findViewById(R.id.rememberMe)
        rememberMeCheckBox.buttonTintList = ColorStateList.valueOf(Color.RED)

        if(MyTube.userPreferences.getUser()!=null){
            findViewById<TextView>(R.id.email).text= MyTube.userPreferences.getUser()!!.email
        }
        findViewById<Button>(R.id.register).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, USER_REQUEST_CODE)
        }
        findViewById<Button>(R.id.forgot).setOnClickListener {
            val intent = Intent(this, ChangePass::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, USER_UPDATE_CODE)
            finish()
        }
        findViewById<Button>(R.id.login).setOnClickListener {
            var email = findViewById<EditText>(R.id.email).text.toString()
            email= lowerCaseEmail(email)
            val password = findViewById<EditText>(R.id.password).text.toString()
            if(checkData()){
                viewModel.onSearchUser(email, password)
            }

        }
        findViewById<Button>(R.id.notNow).setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
        viewModel.found.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val user = viewModel.found.value

                    if (user != null) {
                        val accessToken = user.data?.accessToken
                        Log.i("Login", "" + accessToken)
                        if (accessToken != null) {
                            MyTube.userPreferences.saveAuthToken(accessToken)
                            viewModel.getUserInfo("Bearer $accessToken")
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                        .show()
                }
                Resource.Status.LOADING -> {

                }
            }
        }

        viewModel.user.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val userResource = viewModel.user.value
                    if (userResource != null && userResource.status == Resource.Status.SUCCESS) {
                        val user = userResource.data
                        Log.i("checkbox", "" + rememberMeCheckBox.isChecked)
                        if (user != null && rememberMeCheckBox.isChecked) {
                            MyTube.userPreferences.saveUser(user)
                            MyTube.userPreferences.saveRememberMeState(rememberMeCheckBox.isChecked)
                        } else if (user != null && !rememberMeCheckBox.isChecked) {
                            MyTube.userPreferences.saveUser(user)
                            MyTube.userPreferences.saveRememberMeState(false)
                        }
                    }
                    val intent = Intent(this, SongActivity::class.java)
                    startActivity(intent)
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                        .show()
                }

                Resource.Status.LOADING -> {

                }
            }
        }
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
        }else if (requestCode == USER_UPDATE_CODE && resultCode == RESULT_OK){
            val email=data?.getStringExtra("email") ?: ""
            val password=data?.getStringExtra("password") ?: ""
            Log.i("ViewModel",""+email)
            findViewById<EditText>(R.id.email).setText(email)
            findViewById<EditText>(R.id.password).setText(password)
        }
    }


    private fun checkData(): Boolean {
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
    private fun lowerCaseEmail(input: String): String {
        return input.lowercase(Locale.ROOT)
    }
}
