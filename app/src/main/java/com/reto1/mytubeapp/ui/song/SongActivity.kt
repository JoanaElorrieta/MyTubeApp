package com.reto1.mytubeapp.ui.song

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.reto1.mytubeapp.MyTube
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.data.repository.remote.RemoteSongDataSource
import com.reto1.mytubeapp.utils.Resource
import com.reto1.mytubeapp.databinding.SongActivityBinding
import java.util.Locale

class SongActivity : AppCompatActivity() {
    private val SONG_CHANGES_CODE = 1
    private lateinit var songAdapter: SongAdapter
    private val songRepository = RemoteSongDataSource()
    private val viewModel: SongViewModel by viewModels { SongViewModelFactory(songRepository) }
    private lateinit var song: Song
    private var currentFilterField: String? = null
    private lateinit var binding: SongActivityBinding
    private var isFavorite = true

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SongActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fun onSongListClickItem(song: Song) {
            this.song = song
        }

        fun capitalizeFirstLetter(input: String?): String? {
            if (input.isNullOrEmpty()) {
                return input
            }
            return input.substring(0, 1).uppercase(Locale.ROOT) + input.substring(1)
        }

        songAdapter = SongAdapter(
            ::onSongListClickItem,
            ::onPlayClickListener,
            ::onFavoriteClickListener
        )
        binding.songsList.adapter = songAdapter

        setSupportActionBar(binding.toolbarSongActivity)

        viewModel.items.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        val songs = it.data
                        if (binding.titulo.text == "Listado de canciones") {
                            viewModel.updateSongList()
                        } else {
                            songAdapter.filter(songs)
                        }
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

        viewModel.updatedFavorites.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    viewModel.updateSongList()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                }
            }
        }

        viewModel.deletedFavorites.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    viewModel.updateSongList()
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                }
            }
        }

        viewModel.updatedViews.observe(this) {
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

        viewModel.insertedViews.observe(this) {
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

        binding.toolbarSongActivity.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.FilterTitle -> {
                    changeFilterField("title")
                    binding.textViewFilter.text = "Filtrado por Título"
                    binding.editTextFilter.hint = "Titulo"

                    binding.textViewFilter.visibility = View.VISIBLE
                    binding.editTextFilter.visibility = View.VISIBLE

                    binding.editTextFilter.text.clear()
                    true
                }

                R.id.FilterAuthor -> {
                    changeFilterField("author")
                    binding.textViewFilter.text = "Filtrado por Autor"
                    binding.editTextFilter.hint = "Autor"

                    binding.textViewFilter.visibility = View.VISIBLE
                    binding.editTextFilter.visibility = View.VISIBLE

                    binding.editTextFilter.text.clear()
                    true
                }

                else -> false // Manejo predeterminado para otros elementos
            }
        }

        binding.bottomMenuSongActivity.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.back -> {
                    finish()
                    true
                }

                R.id.favorite -> {
                    isFavorite = !isFavorite

                    if (!isFavorite) {
                        binding.titulo.text = "Favoritas de " + capitalizeFirstLetter((MyTube.userPreferences.getUser()?.name))
                        item.setIcon(R.drawable.music_note)
                        item.title = "Canciones"
                        songAdapter.filter(null)

                    } else {
                        binding.titulo.text = "Listado de canciones"
                        viewModel.updateSongList()
                        item.setIcon(android.R.drawable.star_big_on)
                        item.title = "Favoritas"

                    }
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

        binding.editTextFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cuando cambie el texto, filtra la lista y actualiza el adaptador
                val searchText = s.toString().trim()
                filterSongList(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Observa los cambios en la lista de canciones en el ViewModel y actualiza el adaptador
        viewModel.items.observe(this) { resource ->
            val songs = resource.data
            songAdapter.submitList(songs)
        }
    }

    private fun filterSongList(searchText: String) {
        val filteredList = viewModel.items.value?.data?.filter { song ->
            when (currentFilterField ?: "title") {
                "title" -> song.title.contains(searchText, ignoreCase = true)
                "author" -> song.author.contains(searchText, ignoreCase = true)
                else -> false // Filtro no válido
            }
        }
        songAdapter.submitList(filteredList)
    }

    private fun changeFilterField(newField: String) {
        currentFilterField = newField
        binding.textViewFilter.text = newField
        // Luego, llama a filterSongList para aplicar el nuevo filtro
        val searchText = binding.editTextFilter.text.toString().trim()
        filterSongList(searchText)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_filter,menu)
        return true
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

    private fun onFavoriteClickListener(song: Song) {

        val userId = MyTube.userPreferences.getUser()?.id

        if (userId != null) {
            when (song.favorite) {
                1 -> viewModel.onDeleteFavorite(userId, song.id)
                0 -> viewModel.onCreateFavorite(userId, song.id)
            }
        }
    }

    private fun onPlayClickListener(song: Song) {
        val youtubeUrl = song.url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        intent.setPackage("com.android.chrome")
        try {
            val idUser = MyTube.userPreferences.getUser()?.id
            if (idUser != null && song.views > 0) {
                viewModel.onUpdateViews(idUser, song.id)
                startActivity(intent)

            }else if (idUser != null && song.views == 0){
                viewModel.onInsertViews(idUser, song.id)
                startActivity(intent)

            }else{
                startActivity(intent)
            }

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "No hay navegadores web instalados.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}