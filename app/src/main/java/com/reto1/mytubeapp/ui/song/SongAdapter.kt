package com.reto1.mytubeapp.ui.song

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reto1.mytubeapp.data.Song
import com.reto1.mytubeapp.databinding.ItemSongBinding

class SongAdapter(
    private val onClickListener: (Song) -> Unit
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
        fun bind(song: Song) {
            binding.textViewTitle.text = song.title
            binding.textViewSubtitle1.text = song.author
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
}