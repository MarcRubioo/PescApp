package com.marcr.pescapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcr.pescapp.buscador.BuscadorFragment
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemPostBinding
import com.marcr.pescapp.principalsPosts.principalPostsFragment

class PostAdapter(private val context: Context, private val postList: List<Post>, private val listener: principalPostsFragment) : RecyclerView.Adapter<PostViewHolder>() {

    interface OnItemClickListener {
        fun onLikeClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return PostViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = postList[position]
        holder.bind(item)




        holder.binding.imgLike.setOnClickListener {
            val id = item.id
            listener.onLikeClick(id)
        }
    }
}