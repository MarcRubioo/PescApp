package com.marcr.pescapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemProfilePostBinding

class PostsProfileAdapter(
    private val context: Context,
    private val userPostList: List<Post>,
) : RecyclerView.Adapter<PostsProfileAdapter.PostsProfileViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class PostsProfileViewHolder(private val binding: ItemProfilePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.category.text = post.categoryPost
            binding.site.text = post.sitePost
            Glide.with(binding.imagePost.context).load(post.imagePost).into(binding.imagePost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsProfileViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemProfilePostBinding.inflate(layoutInflater, parent, false)
        return PostsProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = userPostList.size

    override fun onBindViewHolder(holder: PostsProfileViewHolder, position: Int) {
        val item = userPostList[position]
        holder.bind(item)
    }
}
