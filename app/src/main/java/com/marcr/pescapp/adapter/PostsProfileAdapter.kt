package com.marcr.pescapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemProfilePostBinding
import com.marcr.pescapp.perfil.PerfilFragment


class PostsProfileAdapter(private val context: Context, private val userPostList: List<Post> ) : RecyclerView.Adapter<PostsProfileViewHolder>() {

        interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsProfileViewHolder {
        val layoutInflater = ItemProfilePostBinding.inflate(LayoutInflater.from(context), parent, false)
        return PostsProfileViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = userPostList.size

    override fun onBindViewHolder(holder: PostsProfileViewHolder, position: Int) {
        val item = userPostList[position]
        holder.bind(item)
    }
}