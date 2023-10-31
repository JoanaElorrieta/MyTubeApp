package com.reto1.mytubeapp.ui.song

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.remote.RemoteSongDataSource
import com.reto1.mytubeapp.utils.Resource
import com.reto1.mytubeapp.databinding.SongActivityBinding

class SongActivity : AppCompatActivity() {

    private val SONG_CHANGES_CODE = 1
    private lateinit var songAdapter: SongAdapter
    private val songRepository = RemoteSongDataSource()
    private val viewModel: SongViewModel by viewModels { SongViewModelFactory(songRepository) }
    private lateinit var song: Song
    private var isToolbarVisible = false

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SongActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun onEmployeesListClickItem(song: Song) {

            this.song = song

        }

        songAdapter = SongAdapter(::onEmployeesListClickItem)

        binding.songsList.adapter = songAdapter

        val toolbar = findViewById<Toolbar>(R.id.toolbar_song_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

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

        val topFilterMenu = findViewById<BottomNavigationView>(R.id.toolbar_song_activity)

        topFilterMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.FilterTitle -> {

                    true
                }

                R.id.FilterAuthor -> {

                    true
                }

                else -> false // Manejo predeterminado para otros elementos
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_menu_song_activity)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.back -> {
                    finish()
                    true
                }

                R.id.configMenu -> {

                    val intent = Intent(this, SongConfig::class.java)
                    startActivityForResult(intent, SONG_CHANGES_CODE)
                    true
                }

                else -> false // Manejo predeterminado para otros elementos
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id. -> {
//                // Manejar la selección del filtro de título
//                true
//            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SONG_CHANGES_CODE && resultCode == RESULT_OK) {

            val someChanges = data?.getBooleanExtra("someChanges", false)

            if (someChanges == true) {
                viewModel.updateSongList()
            }

        }

    }

}