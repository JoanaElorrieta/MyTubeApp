package com.reto1.mytubeapp.ui.song

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.reto1.mytubeapp.MyTube
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.CommonSongRepository
import com.reto1.mytubeapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongViewModel(
    private val songRepository: CommonSongRepository
) : ViewModel() {
    
    private val _items = MutableLiveData<Resource<List<Song>>>()
    val items : LiveData<Resource<List<Song>>> get() = _items

    private val _created = MutableLiveData<Resource<Void>>()
    val created : LiveData<Resource<Void>> get() = _created

    private val _updated = MutableLiveData<Resource<Void>>()
    val updated : LiveData<Resource<Void>> get() = _updated

    private val _deleted = MutableLiveData<Resource<Integer>>()
    val deleted : LiveData<Resource<Integer>> get() = _deleted

    private val _updatedViews = MutableLiveData<Resource<Void>>()
    val updatedViews : LiveData<Resource<Void>> get() = _updatedViews
    private val _insertedViews= MutableLiveData<Resource<Void>>()
    val insertedViews : LiveData<Resource<Void>> get() = _insertedViews

    private val _updatedFavorites = MutableLiveData<Resource<Void>>()
    val updatedFavorites : LiveData<Resource<Void>> get() = _updatedFavorites

    private val _deletedFavorites = MutableLiveData<Resource<Integer>>()

    val deletedFavorites : LiveData<Resource<Integer>> get() = _deletedFavorites

    init { updateSongList() }
    fun updateSongList() {
        viewModelScope.launch {
            _items.value = getSongsFromRepository()
        }
    }

    private suspend fun getSongsFromRepository() : Resource<List<Song>> {
        return withContext(Dispatchers.IO) {
            val userId=MyTube.userPreferences.getUser()?.id
            if (userId != null) {
                songRepository.getSongsFavoriteViews()
            }else{
                songRepository.getSongs()
            }
        }
    }
    fun onAddSong(title: String, author: String, url:String) {
        val song = Song(0,title, author, url,0,0)
        viewModelScope.launch {
            _created.value = createSong(song)
        }
    }
    fun onUpdateSong(id: Int, title: String, author: String, url:String) {
        val song = Song(0,title, author, url,0,0)
        viewModelScope.launch {
            _updated.value = updateSong(id,song)
        }
    }
    fun onDeleteSong(id: Int) {
        viewModelScope.launch {
            _deleted.value = deleteSong(id)
        }
    }
    private suspend fun createSong(song: Song): Resource<Void> {
        return withContext(Dispatchers.IO) {
            songRepository.createSong(song)
        }
    }
    private suspend fun updateSong(id:Int, song: Song): Resource<Void> {
        return withContext(Dispatchers.IO) {
            songRepository.updateSong(id,song)
        }
    }
    private suspend fun deleteSong(id:Int): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.deleteSong(id)
        }
    }
    fun onUpdateViews(idSong:Int) {
        viewModelScope.launch {
            _updatedViews.value = updateViews(idSong)
        }
    }
    private suspend fun updateViews(idSong:Int): Resource<Void> {
        return withContext(Dispatchers.IO) {
            songRepository.updateNumberViews(idSong)
        }
    }

    fun onCreateFavorite(idSong:Int) {
        viewModelScope.launch {
            _updatedFavorites.value = createFavorite(idSong)
        }
    }
    private suspend fun createFavorite(idSong:Int): Resource<Void> {
        return withContext(Dispatchers.IO) {
            songRepository.createFavorite(idSong)
        }
    }

    fun onDeleteFavorite(idSong:Int) {
        viewModelScope.launch {
            _deletedFavorites.value = deleteFavorite(idSong)
        }
    }
    private suspend fun deleteFavorite(idSong:Int): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.deleteFavorite(idSong)
        }
    }

    fun onInsertViews(idSong:Int) {
        viewModelScope.launch {
            _insertedViews.value = insertViews(idSong)
        }
    }
    private suspend fun insertViews(idSong:Int): Resource<Void> {
        return withContext(Dispatchers.IO) {
            songRepository.insertNumberViews(idSong)
        }
    }
}

class SongViewModelFactory(
    private val songRepository: CommonSongRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SongViewModel(songRepository) as T
    }
}