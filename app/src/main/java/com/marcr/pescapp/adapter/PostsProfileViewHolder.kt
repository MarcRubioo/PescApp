package com.marcr.pescapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.data.User
import com.marcr.pescapp.databinding.ItemProfilePostBinding
import com.marcr.pescapp.databinding.ItemUsersearchBinding

class PostsProfileViewHolder(private var binding: ItemProfilePostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.category.text = post.categoryPost
        binding.site.text = post.sitePost
        Glide.with(binding.imagePost.context).load(post.imagePost).into(binding.imagePost)

    }
}