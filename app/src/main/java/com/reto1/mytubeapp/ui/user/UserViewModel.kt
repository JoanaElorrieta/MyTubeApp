package com.reto1.mytubeapp.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository
import com.reto1.mytubeapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _created = MutableLiveData<Resource<Void>>()
    val created : LiveData<Resource<Void>> get() = _created

    private val _found= MutableLiveData<Resource<User>>()
    val found : LiveData<Resource<User>> get() = _found

    private val _update= MutableLiveData<Resource<Void>>()
    val update : LiveData<Resource<Void>> get() = _update

    suspend fun createUser(user : User) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.createUser(user)
        }
    }
    fun onCreateUser(user: User) {
        viewModelScope.launch {
            _created.value = createUser(user)
        }
    }
    suspend fun searchUser(email:String, password:String) : Resource<User> {
        return withContext(Dispatchers.IO) {
            userRepository.getUserByMail(email, password)
        }
    }
    fun onSearchUser(email:String, password:String) {
        viewModelScope.launch {
            _found.value = searchUser(email,password)

        }
    }
    suspend fun updateUser(email:String, password:String) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.updateUser(email, password)
        }
    }
    fun onUpdateUser(email: String, password:String) {
        viewModelScope.launch {
            _update.value = updateUser(email,password)
            Log.i("ViewModel",""+_update.value)
        }
    }


}
class UserViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserViewModel(userRepository) as T
    }

}
