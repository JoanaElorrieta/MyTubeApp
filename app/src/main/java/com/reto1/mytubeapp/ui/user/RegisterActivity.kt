package com.reto1.mytubeapp.ui.user


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.remote.RemoteUserDataSource
import com.reto1.mytubeapp.ui.song.SongActivity
import com.reto1.mytubeapp.utils.Resource
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private val userRepository = RemoteUserDataSource()

    private val viewModel: UserViewModel by viewModels { UserViewModelFactory(userRepository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        userAdapter = UserAdapter()

        viewModel.created.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    //    viewModel.createUser()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    // de momento
                }
            }
        })

        findViewById<Button>(R.id.back).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.register).setOnClickListener {
            var user=checkData()
            if (user!=null){
               viewModel.onCreateUser(user)
                val intent = Intent(this, SongActivity::class.java)
                startActivity(intent)
            }else{

            }
    }


}
//Este metodo valida el patrón de los correos
private fun validarEmail(email: String): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS

    return pattern.matcher(email).matches()
}

//Este metodo crea un usuario y si la contraseña es igual en ambos campos y el correo tiene
//Formato correcto lo pasa, si no lo pasa como null
fun checkData(): User? {
    val email = findViewById<EditText>(R.id.email).text.toString()
    val name = findViewById<EditText>(R.id.name).text.toString()
    val surname = findViewById<EditText>(R.id.surname).text.toString()
    val password = findViewById<EditText>(R.id.password).text.toString()
    val password2 = findViewById<EditText>(R.id.password2).text.toString()

    if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty() || password2.isEmpty()){
        Toast.makeText(this, "Ningún campo puede estar vacío", Toast.LENGTH_LONG).show()
        return null
    }
    val emailCorrecto=validarEmail(email)
    if (!emailCorrecto){
        Toast.makeText(this, "El correo tiene un formato erróneo", Toast.LENGTH_LONG).show()
    }

    if (password == password2 && emailCorrecto) {
        return User(name, surname, email, password)
    } else {
        Toast.makeText(this, "Las dos contraseñas no coinciden", Toast.LENGTH_LONG).show()
        return null
    }
}
}