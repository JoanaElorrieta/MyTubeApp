package com.reto1.mytubeapp.ui.song

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reto1.mytubeapp.MyTube
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.databinding.ItemSongBinding
import com.reto1.mytubeapp.utils.Resource

class SongAdapter(
    private val onClickListener: (Song) -> Unit,
    private val onPLayClickListener: (Song) -> Unit
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    private var lastSelectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding =
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        val song = getItem(position)
        holder.bind(song)

        if (position == lastSelectedPosition) {
            // Establece el color de fondo para el elemento seleccionado
            holder.itemView.setBackgroundColor(Color.RED)
        } else {
            // Establece el color de fondo para otros elementos (por defecto)
            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            onClickListener(song)

            val previousSelectedPosition = lastSelectedPosition
            lastSelectedPosition = holder.adapterPosition

            //Notificamos de la actualizacion del ultimo y primer elemento
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(lastSelectedPosition)

        }
    }

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isFavorite = false

        private fun isSongIdInList(songIdToCheck: Int, songList: List<Song>): Boolean {
            for (song in songList) {
                if (song.id == songIdToCheck) {
                    return true // El ID de la canción se encontró en la lista
                }
            }
            return false // El ID de la canción no se encontró en la lista
        }

        fun bind(song: Song) {
            binding.textViewTitle.text = song.title
            binding.textViewAuthor.text = song.author

            val songList = MyTube.userPreferences.getUser()?.listSongFavs ?: emptyList()

            if (isSongIdInList(song.id, songList)) {
                binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_on)
            } else {
                binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_off)
            }

            binding.imageViewFavorite.setOnClickListener {

                isFavorite = !isFavorite // Cambiar el estado

                if (isFavorite) {

                    val userId = MyTube.userPreferences.getUser()?.id
                    if (userId != null) {
                        binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_on)
                        viewModel.onCreateFavorite(userId ,song.id)
                        val listSongFavs = MyTube.userPreferences.loadFavoriteSongs()
                        val listaActualizada = listSongFavs.toMutableList()
                        listaActualizada.add(song)
                        MyTube.userPreferences.saveFavoriteSongs(listaActualizada)
                    }

                } else {

                    val userId = MyTube.userPreferences.getUser()?.id
                    if (userId != null) {
                        binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_off)
                        viewModel.onDeleteFavorite(userId ,song.id)
                        val listSongFavs = MyTube.userPreferences.loadFavoriteSongs()
                        val listaActualizada = listSongFavs.filter { it.id != song.id }
                        MyTube.userPreferences.saveFavoriteSongs(listaActualizada)
                    }
                }
            }

            binding.imageViewPlay.setOnClickListener {
                val youtubeUrl = song.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                intent.setPackage("com.android.chrome")
                try {
                    itemView.context.startActivity(intent)
                    val idUser=MyTube.userPreferences.getUser()?.id
                    if (idUser != null) {
                        viewModel.onUpdateViews(idUser,song.id)
                    }
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(itemView.context, "No hay navegadores web instalados.", Toast.LENGTH_SHORT).show()
                }
        fun bind(song: Song) {
            binding.textViewTitle.text = song.title
            binding.textViewAuthor.text = song.author

            if (song.views != null) {
                binding.textViewViews.text = song.views.toString()
            }

            if (song.favorite==1) {
                binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_on)
            } else {
                binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_off)
            }

            binding.imageViewPlay.setOnClickListener {
                onPLayClickListener(song)
            }
        }
    }
}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {

    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return (oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.author == newItem.author && oldItem.url == newItem.url)
    }

}
