package com.reto1.mytubeapp.ui.user

import android.annotation.SuppressLint
import com.reto1.mytubeapp.MyTube
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatCheckBox
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.remote.RemoteUserDataSource
import com.reto1.mytubeapp.databinding.LoginActivityBinding
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.utils.Resource
import java.util.Locale
import java.util.regex.Pattern

class LogInActivity : AppCompatActivity() {
    private val USER_REQUEST_CODE = 1
    private lateinit var userAdapter: UserAdapter
    private val userRepository = RemoteUserDataSource()
    private lateinit var rememberMeCheckBox: AppCompatCheckBox
    private lateinit var binding: LoginActivityBinding

    private val viewModel: UserViewModel by viewModels { UserViewModelFactory(userRepository) }
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter()
        rememberMeCheckBox= binding.rememberMe
        rememberMeCheckBox.buttonTintList = ColorStateList.valueOf(Color.RED)
        //Pone el checkbox true o false segun lo guardado
        rememberMeCheckBox.isChecked= MyTube.userPreferences.getRememberMeState()
        //Pone pass y user si hay uno guardado
        if(rememberMeCheckBox.isChecked){
            binding.email.setText(MyTube.userPreferences.getUser()!!.email)
            binding.password.setText(MyTube.userPreferences.getPass())
        }else {
            MyTube.userPreferences.removeData()
        }


       binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, USER_REQUEST_CODE)
        }
        binding.forgot.setOnClickListener {
            val intent = Intent(this, ChangePass::class.java)
            startActivity(intent)
            finish()
        }
        binding.login.setOnClickListener {
            var email = binding.email.text.toString()
            email= lowerCaseEmail(email)
            val password = binding.password.text.toString()
            if(checkData()){
                viewModel.onSearchUser(email, password)
            }
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
                            viewModel.getUserInfo()
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
                            MyTube.userPreferences.savePass(binding.password.text.toString())
                        } else if (user != null && !rememberMeCheckBox.isChecked) {
                            MyTube.userPreferences.saveUser(user)
                            MyTube.userPreferences.saveRememberMeState(false)
                        }
                    }
                    val intent = Intent(this, SongActivity::class.java)
                    startActivity(intent)
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, "Los datos introducidos no son correctos", Toast.LENGTH_LONG)
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
                binding.email.setText(user.email)
                binding.password.setText(user.password)
            }
        }
    }

    private fun checkData(): Boolean {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_LONG).show()
            binding.email.setHintTextColor(Color.RED)
            binding.password.setHintTextColor(Color.RED)
            return false
        }
        val emailCorrecto = validarEmail(email)
        if (!emailCorrecto) {
            Toast.makeText(this, "El correo tiene un formato erróneo", Toast.LENGTH_LONG).show()
            binding.email.setTextColor(Color.RED)
            binding.password.setTextColor(Color.BLACK)

            binding.email.setHintTextColor(Color.BLACK)
            binding.password.setHintTextColor(Color.BLACK)
            return false
        }

        return if (password.length >= 8) {
            true
        } else {
            Toast.makeText(this, "La contraseña debe tener 8 caracteres o más", Toast.LENGTH_LONG
            ).show()
            binding.email.setTextColor(Color.BLACK)
            binding.password.setTextColor(Color.RED)

            binding.email.setHintTextColor(Color.BLACK)
            binding.password.setHintTextColor(Color.BLACK)
            false
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
