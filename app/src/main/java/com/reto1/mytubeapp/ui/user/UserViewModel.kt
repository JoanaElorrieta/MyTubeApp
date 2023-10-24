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


}
class UserViewModelFactory(
    private val userRepository: CommonUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return UserViewModel(userRepository) as T
    }

}
