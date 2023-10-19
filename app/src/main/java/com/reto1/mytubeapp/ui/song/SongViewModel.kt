package com.reto1.mytubeapp.ui.song

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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

    private val _created = MutableLiveData<Resource<Integer>>();
    val created : LiveData<Resource<Integer>> get() = _created;

    private val _updated = MutableLiveData<Resource<Integer>>();
    val updated : LiveData<Resource<Integer>> get() = _updated;

    private val _deleted = MutableLiveData<Resource<Integer>>();
    val deleted : LiveData<Resource<Integer>> get() = _deleted;


    init {
   updateSongList();
    }
    fun updateSongList() {
        viewModelScope.launch {
            val repoResponse = getSongsFromRepository();
            _items.value = repoResponse
        }
    }
    suspend fun getSongsFromRepository() : Resource<List<Song>> {
        return withContext(Dispatchers.IO) {
            songRepository.getSongs()
        }
    }
    fun onAddSong(id: Int, title: String, author: String, url:String) {
        val song = Song(id,title, author, url)
        viewModelScope.launch {
            _created.value = createSong(song)
        }
    }
    fun onUpdateSong(id: Int, title: String, author: String, url:String) {
        val song = Song(id,title, author, url)
        viewModelScope.launch {
            _updated.value = updateSong(id,song)
        }
    }
    fun onDeleteSong(id: Int, name: String, city: String) {
        viewModelScope.launch {
            _deleted.value = deleteSong(id)
        }
    }
    suspend fun createSong(song: Song): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.createSong(song)
        }
    }
    suspend fun updateSong(id:Int, song: Song): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.updateSong(id,song)
        }
    }
    suspend fun deleteSong(id:Int): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.deleteSong(id)
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