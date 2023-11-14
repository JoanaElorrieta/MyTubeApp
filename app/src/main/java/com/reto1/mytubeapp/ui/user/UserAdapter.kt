package com.reto1.mytubeapp.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reto1.mytubeapp.R
import com.reto1.mytubeapp.data.User

class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.register_activity, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        private val textViewSubtitle1 = itemView.findViewById<TextView>(R.id.textViewAuthor)

        fun bind(user: User) {
            textViewTitle.text = user.name
            textViewSubtitle1.text = user.lastName
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return (oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.lastName == newItem.lastName && oldItem.email == newItem.email && oldItem.password == newItem.password)
        }
    }
}
