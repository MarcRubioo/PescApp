package com.marcr.pescapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcr.pescapp.buscador.BuscadorFragment
import com.marcr.pescapp.data.User
import com.marcr.pescapp.databinding.ItemUsersearchBinding



class UserSearchAdapter(private val context: Context, private val userSearchList: List<User>, private val listener: BuscadorFragment) : RecyclerView.Adapter<UserSearchViewHolder>() {

    interface OnUserClickListener {
        fun onUserClick(email: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val layoutInflater = ItemUsersearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserSearchViewHolder(layoutInflater, listener)
    }

    override fun getItemCount(): Int = userSearchList.size

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        val item = userSearchList[position]
        holder.bind(item)
    }
}