package com.reto1.mytubeapp.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reto1.mytubeapp.data.repository.remote.RemoteUserDataSource
import com.reto1.mytubeapp.databinding.ChangePassBinding
import com.reto1.mytubeapp.utils.Resource
import java.util.regex.Pattern

class ChangePass : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private val userRepository = RemoteUserDataSource()
    private val viewModel: UserViewModel by viewModels { UserViewModelFactory(userRepository) }
    private lateinit var binding: ChangePassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter()

        viewModel.update.observe(this) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "Contraseña modificada con exito", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG)
                        .show()
                    Log.i("Prueba", "" + resource.message)
                }
                Resource.Status.LOADING -> {
                }
            }
        }

        binding.update.setOnClickListener {

            if(checkData()){
                val email = binding.email.text.toString()
                val oldPassword = binding.oldPassword.toString()
                val password = binding.password.text.toString()

                viewModel.onUpdateUser(email,oldPassword,password)
            }
        }

        binding.back.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    //Este metodo valida el patrón de los correos
    private fun validarEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    //Este metodo crea un usuario y si la contraseña es igual en ambos campos y el correo tiene
    //Formato correcto lo pasa, si no lo pasa como null
    private fun checkData(): Boolean{
        val email = binding.email.text.toString()
        val oldPassword = binding.oldPassword.text.toString()
        val password = binding.password.text.toString()
        val password2 = binding.password2.text.toString()

        if (email.isEmpty() || oldPassword.isEmpty()  || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_LONG).show()
            return false
        }
        val emailCorrecto = validarEmail(email)
        if (!emailCorrecto) {
            Toast.makeText(this, "El correo tiene un formato erróneo", Toast.LENGTH_LONG).show()
            return false
        }

        if (password == password2) {
            if (password.length >= 8) {
                return true
            } else {
                Toast.makeText(
                    this,
                    "La nueva contraseña debe tener 8 caracteres o más",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        } else {
            Toast.makeText(this, "Las dos contraseñas no coinciden", Toast.LENGTH_LONG).show()
            return false
        }
    }
}
