package com.reto1.mytubeapp.ui.song

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.remote.RemoteSongDataSource
import com.reto1.mytubeapp.utils.Resource
import com.reto1.mytubeapp.databinding.SongActivityBinding

class SongActivity : AppCompatActivity(){

private lateinit var songAdapter: SongAdapter
private val songRepository = RemoteSongDataSource()

private val viewModel: SongViewModel by viewModels { SongViewModelFactory(songRepository) }

    private lateinit var song : Song
    private var isFragmentVisible = false
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    val binding = SongActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    fun onEmployeesListClickItem(song: Song) {

        this.song = song

        binding.songInputTitle.setText(song.title)
        binding.songInputAuthor.setText(song.author)
        binding.songInputUrl.setText(song.url)

    }

    songAdapter = SongAdapter(::onEmployeesListClickItem)

    binding.songsList.adapter = songAdapter

    viewModel.items.observe(this) {
        Log.i("PruebasDia1", "ha ocurrido un cambio en la lista")
        when (it.status) {
            Resource.Status.SUCCESS -> {
                if (!it.data.isNullOrEmpty()) {
                    songAdapter.submitList(it.data)
                    Log.i("PruebasDia1", "ha entrado")
                }
            }

            Resource.Status.ERROR -> {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

            Resource.Status.LOADING -> {
                // de momento
            }
        }
    }

    viewModel.created.observe(this) {
        when (it.status) {
            Resource.Status.SUCCESS -> {
                viewModel.updateSongList()
            }

            Resource.Status.ERROR -> {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

            Resource.Status.LOADING -> {
                // de momento
            }
        }
    }

    viewModel.updated.observe(this) {
        when (it.status) {
            Resource.Status.SUCCESS -> {
                viewModel.updateSongList()
            }

            Resource.Status.ERROR -> {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

            Resource.Status.LOADING -> {
                // de momento
            }
        }
    }
    viewModel.deleted.observe(this) {
        when (it.status) {
            Resource.Status.SUCCESS -> {
                viewModel.updateSongList()
            }

            Resource.Status.ERROR -> {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

            Resource.Status.LOADING -> {
                // de momento
            }
        }
    }

    binding.filter.setOnClickListener {
        if (isFragmentVisible) {
            // Si el Fragment ya está visible, quitarlo
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_song)
            if (fragment != null) {
                transaction.remove(fragment)
                transaction.commit()
            }
            isFragmentVisible = false
        } else {
            // Si el Fragment no está visible, mostrarlo
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = SongFragment()
            transaction.replace(R.id.fragment_song, fragment)
            transaction.commit()
            isFragmentVisible = true
        }
    }

    binding.addSong.setOnClickListener {
        viewModel.onAddSong(
            binding.songInputTitle.text.toString(),
            binding.songInputAuthor.text.toString(),
            binding.songInputUrl.text.toString()
        )
        binding.songInputTitle.text.clear()
        binding.songInputAuthor.text.clear()
        binding.songInputUrl.text.clear()
    }

    binding.updateSong.setOnClickListener {
        viewModel.onUpdateSong(
            song.id,
            binding.songInputTitle.text.toString(),
            binding.songInputAuthor.text.toString(),
            binding.songInputUrl.text.toString()
        )
        binding.songInputTitle.text.clear()
        binding.songInputAuthor.text.clear()
        binding.songInputUrl.text.clear()
    }
    binding.deleteSong.setOnClickListener {
        viewModel.onDeleteSong(
            song.id,
        )
        binding.songInputTitle.text.clear()
        binding.songInputAuthor.text.clear()
        binding.songInputUrl.text.clear()
    }



}

}