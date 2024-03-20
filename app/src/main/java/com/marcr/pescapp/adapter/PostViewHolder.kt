package com.marcr.pescapp.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemPostBinding

class PostViewHolder (private var binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root) {
    fun render(postModel: Post){
        binding.email.text = postModel.email
        binding.category.text = postModel.categoryPost
        binding.site.text = postModel.sitePost
        Glide.with(binding.imagePost.context).load(postModel.imagePost).into(binding.imagePost)
        binding.title.text = postModel.titlePost
    }
}