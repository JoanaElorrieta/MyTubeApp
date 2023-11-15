package com.reto1.mytubeapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.reto1.mytubeapp.data.AuthRequest
import com.reto1.mytubeapp.data.User
import com.reto1.mytubeapp.data.repository.CommonUserRepository
import com.reto1.mytubeapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: CommonUserRepository
) : ViewModel() {

    private val _created = MutableLiveData<Resource<Integer>>()
    val created : LiveData<Resource<Integer>> get() = _created

    private val _found= MutableLiveData<Resource<User>>()
    val found : LiveData<Resource<User>> get() = _found

    private val _update= MutableLiveData<Resource<Void>>()
    val update : LiveData<Resource<Void>> get() = _update
    private val _user= MutableLiveData<Resource<User>>()
    val user : LiveData<Resource<User>> get() = _user

    private suspend fun createUser(user : User) : Resource<Integer> {
        return withContext(Dispatchers.IO) {
            userRepository.signIn(user)
        }
    }
    fun onCreateUser(user: User) {
        viewModelScope.launch {
            _created.value = createUser(user)
        }
    }
    private suspend fun searchUser(email:String, password:String) : Resource<User> {
        return withContext(Dispatchers.IO) {
            val user= AuthRequest(email,"12341234",password)
            userRepository.login(user)
        }
    }
    fun onSearchUser(email:String, password:String) {
        viewModelScope.launch {
            _found.value = searchUser(email,password)
        }
    }

    fun onUpdateUser(authRequest: AuthRequest) {
        viewModelScope.launch {
            _update.value = updateUser(authRequest)
        }
    }

    private suspend fun updateUser(authRequest: AuthRequest) : Resource<Void> {
        return withContext(Dispatchers.IO) {
            userRepository.updateUser(authRequest)
        }
    }
    fun getUserInfo(){
        viewModelScope.launch {
            _user.value = getInfo()
        }
    }
    private suspend fun getInfo() : Resource<User> {
        return withContext(Dispatchers.IO) {
            userRepository.getUserInfo()
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
