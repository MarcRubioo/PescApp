package com.marcr.pescapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.buscador.BuscadorFragment
import com.marcr.pescapp.data.User
import com.marcr.pescapp.databinding.ItemUsersearchBinding

class UserSearchViewHolder(private var binding: ItemUsersearchBinding, private val listener: BuscadorFragment) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.nameProfileSearch.text = user.name
        Glide.with(binding.imageItemSearch.context).load(user.img).into(binding.imageItemSearch)

        // Handle item click
        binding.root.setOnClickListener {
            listener.onUserClick(user.email)
        }
    }
}

