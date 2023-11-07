package com.reto1.mytubeapp.ui.song

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
            binding.textViewAuthor.text = song.author
            binding.imageViewFavorite.setOnClickListener {
                // Cuando se hace clic en el ImageButton, muestra la información en el log
                Log.d("SongAdapter", "Song Title: ${song.title}, Author: ${song.author}, URL: ${song.url}")
            }
            binding.imageViewPlay.setOnClickListener {
                val youtubeUrl = song.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                intent.setPackage("com.android.chrome")
                try {
                    itemView.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                    itemView.context.startActivity(browserIntent)
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
}