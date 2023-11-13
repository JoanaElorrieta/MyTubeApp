package com.reto1.mytubeapp.ui.song

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.databinding.ItemSongBinding

class SongAdapter(
    private val onClickListener: (Song) -> Unit,
    private val onPlayClickListener: (Song) -> Unit,
    private val onFavoriteClickListener: (Song) -> Unit,
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

    fun filter(listSongs: List<Song>?) {
        if(listSongs != null) {
            val filteredSongs = listSongs.filter { it.favorite == 1 }
            submitList(filteredSongs.toList())
        }else {
            val filteredSongs = currentList.filter { it.favorite == 1 }
            Log.i("Prueba", "Current Before $currentList")
            Log.i("Prueba", "Filtered $filteredSongs")
            submitList(filteredSongs.toList())
            Log.i("Prueba", "Current Before $currentList")
        }
    }

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(song: Song) {
                binding.textViewTitle.text = song.title
                binding.textViewAuthor.text = song.author
                binding.textViewViews.text = song.views.toString()

                if (song.favorite == 1) {
                    binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_on)
                } else {
                    binding.imageViewFavorite.setImageResource(android.R.drawable.star_big_off)
                }

                binding.imageViewPlay.setOnClickListener {
                    onPlayClickListener(song)
                }

                binding.imageViewFavorite.setOnClickListener {
                    onFavoriteClickListener(song)
                }
            }
        }
    }

    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return (oldItem.id == newItem.id && oldItem.title == newItem.title &&
                    oldItem.author == newItem.author && oldItem.url == newItem.url &&
                    oldItem.views == newItem.views && oldItem.favorite == newItem.favorite)
        }

    }
