package com.marcr.pescapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemPostBinding

class PostViewHolder(var binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.email.text = post.email
        binding.category.text = post.categoryPost
        binding.site.text = post.sitePost
        Glide.with(binding.imagePost.context).load(post.imagePost).into(binding.imagePost)
        binding.title.text = post.titlePost
        binding.numLikes.text = post.likes.size.toString()
    }
}