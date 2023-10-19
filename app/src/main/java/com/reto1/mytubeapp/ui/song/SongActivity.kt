package com.reto1.mytubeapp.ui.song
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.reto1.mytubeapp.data.repository.remote.RemoteSongDataSource
import com.reto1.mytubeapp.utils.Resource
import com.reto1.mytubeapp.databinding.SongActivityBinding

class SongActivity : ComponentActivity(){

private lateinit var songAdapter: SongAdapter
private val songRepository = RemoteSongDataSource()

private val viewModel: SongViewModel by viewModels { SongViewModelFactory(songRepository) }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    val binding = SongActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    songAdapter = SongAdapter()

    binding.songsList.adapter = songAdapter

    viewModel.items.observe(this, Observer {
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
    })

    viewModel.created.observe(this, Observer {
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
    })

    viewModel.updated.observe(this, Observer {
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
    })
    viewModel.deleted.observe(this, Observer {
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
    })

//    binding.addSong.setOnClickListener() {
//        viewModel.onAddSong(
//            binding.departmentInputName.text.toString(),
//            binding.departmentInputCity.text.toString()
//        )
//        binding.departmentInputId.text.clear()
//        binding.departmentInputName.text.clear()
//        binding.departmentInputCity.text.clear()
//    }
//    binding.updateDepartment.setOnClickListener() {
//        viewModel.onUpdateSong(
//            binding.departmentInputId.text.toString().toInt(),
//            binding.departmentInputName.text.toString(),
//            binding.departmentInputCity.text.toString()
//        )
//        binding.departmentInputId.text.clear()
//        binding.departmentInputName.text.clear()
//        binding.departmentInputCity.text.clear()
//    }
//    binding.deleteSong.setOnClickListener() {
//        viewModel.onDeleteSong(
//            binding.departmentInputId.text.toString().toInt(),
//            binding.departmentInputName.text.toString(),
//            binding.departmentInputCity.text.toString()
//        )
//        binding.departmentInputId.text.clear()
//        binding.departmentInputName.text.clear()
//        binding.departmentInputCity.text.clear()
//    }



}

}