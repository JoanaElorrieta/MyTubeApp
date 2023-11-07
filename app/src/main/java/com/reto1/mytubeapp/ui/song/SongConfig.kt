package com.reto1.mytubeapp.ui.song

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.remote.RemoteSongDataSource
import com.reto1.mytubeapp.databinding.ConfigSongBinding
import com.reto1.mytubeapp.utils.Resource

class SongConfig : AppCompatActivity() {

    private lateinit var songAdapter: SongAdapter
    private val songRepository = RemoteSongDataSource()

    private val viewModel: SongViewModel by viewModels { SongViewModelFactory(songRepository) }

    private var song = Song("", "", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ConfigSongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var someChanges = false

        fun onEmployeesListClickItem(song: Song) {

            this.song = song

            binding.songInputTitle.setText(song.title)
            binding.songInputAuthor.setText(song.author)
            binding.songInputUrl.setText(song.url)

        }

        songAdapter = SongAdapter(::onEmployeesListClickItem,viewModel)

        binding.songsList.adapter = songAdapter

        viewModel.items.observe(this) {
            Log.i("PruebasDia1", "ha ocurrido un cambio en la lista")
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        songAdapter.submitList(it.data)
                        someChanges = true
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
                    someChanges = true
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
                    someChanges = true
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
                    someChanges = true
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    // de momento
                }
            }
        }

        binding.songInputUrl.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.songInputUrl.setText("https://")
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.create -> {

                    if (binding.songInputTitle.text.toString() != "" &&
                        binding.songInputAuthor.text.toString() != "" &&
                        binding.songInputUrl.text.toString().matches("^https://.*".toRegex())
                    ) {

                        viewModel.onAddSong(
                            binding.songInputTitle.text.toString(),
                            binding.songInputAuthor.text.toString(),
                            binding.songInputUrl.text.toString()
                        )
                        binding.songInputTitle.text.clear()
                        binding.songInputAuthor.text.clear()
                        binding.songInputUrl.text.clear()
                        true
                    } else {

                        Toast.makeText(
                            this,
                            "Debe cumplir con los requisitos de creación",
                            Toast.LENGTH_LONG
                        ).show()
                        false

                    }
                }

                R.id.delete -> {
                    if (song.id != 0) {
                        viewModel.onDeleteSong(song.id)
                        binding.songInputTitle.text.clear()
                        binding.songInputAuthor.text.clear()
                        binding.songInputUrl.text.clear()
                        true
                    } else {
                        Toast.makeText(
                            this,
                            "Debe seleccionar una cancion a eliminar",
                            Toast.LENGTH_LONG
                        ).show()
                        false
                    }
                }

                R.id.edit -> {
                    if (song.id != 0) {
                        if (binding.songInputTitle.text.toString() != "" &&
                            binding.songInputAuthor.text.toString() != "" &&
                            binding.songInputUrl.text.toString().matches("^https://.*".toRegex())
                        ) {
                            if (song.title == binding.songInputTitle.text.toString() &&
                                song.author == binding.songInputAuthor.text.toString() &&
                                song.url == binding.songInputUrl.text.toString()
                            ) {
                                viewModel.onUpdateSong(
                                    song.id,
                                    binding.songInputTitle.text.toString(),
                                    binding.songInputAuthor.text.toString(),
                                    binding.songInputUrl.text.toString()
                                )
                                binding.songInputTitle.text.clear()
                                binding.songInputAuthor.text.clear()
                                binding.songInputUrl.text.clear()
                                true
                            } else {
                                Toast.makeText(
                                    this,
                                    "Debe editar al menos un campo",
                                    Toast.LENGTH_LONG
                                ).show()
                                false
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Debe cumplir con los requisitos de edición",
                                Toast.LENGTH_LONG
                            ).show()
                            false
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Debe seleccionar una cancion a editar",
                            Toast.LENGTH_LONG
                        ).show()
                        false
                    }
                }

                R.id.back -> {
                    val intent = Intent(this, SongActivity::class.java)
                    intent.putExtra("someChanges", someChanges)
                    setResult(RESULT_OK, intent)
                    finish()
                    true
                }

                R.id.logOut -> {
                    true
                }

                else -> false // Manejo predeterminado para otros elementos
            }
        }

    }
}