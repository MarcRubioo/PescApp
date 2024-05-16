package com.marcr.pescapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marcr.pescapp.R
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.ItemPostBinding
import com.marcr.pescapp.principalsPosts.principalPostsFragment

class PostAdapter(
    private val context: Context,
    private val postList: MutableList<Post>,
    private val listener: principalPostsFragment,
    private val email: String
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.email.text = post.email
            binding.category.text = post.categoryPost
            binding.site.text = post.sitePost
            Glide.with(binding.imagePost.context).load(post.imagePost).into(binding.imagePost)
            binding.title.text = post.titlePost
            binding.numLikes.text = post.likes.size.toString()

            if (post.likes.contains(email)) {
                binding.imgLike.setBackgroundResource(R.drawable.silike)
            } else {
                binding.imgLike.setBackgroundResource(R.drawable.nolike)
            }

            binding.imgLike.setOnClickListener {
                listener.onLikeClick(post.id, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
    }

    fun updatePost(position: Int, post: Post) {
        postList[position] = post
        notifyItemChanged(position)
    }
}
