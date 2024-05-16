package com.marcr.pescapp.principalsPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.adapter.PostAdapter
import com.marcr.pescapp.data.Post
import com.marcr.pescapp.databinding.FragmentPrincipalPostsBinding

class principalPostsFragment : Fragment() {
    private lateinit var binding: FragmentPrincipalPostsBinding
    private val viewModel: ViewModelPrincipalPosts by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrincipalPostsBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()
        val manager = LinearLayoutManager(requireContext())
        binding.recyclerPosts.layoutManager = manager

        binding.imageBuscador.setOnClickListener {
            findNavController().navigate(R.id.action_principalPostsFragment2_to_buscadorFragment, null)
        }

        viewModel.getPosts()
        viewModel.posts.observe(viewLifecycleOwner) { llistaPost ->
            adapter = PostAdapter(requireContext(), llistaPost.toMutableList(), this, auth.currentUser?.email.toString())
            binding.recyclerPosts.adapter = adapter
        }

        viewModel.likeResult.observe(viewLifecycleOwner) { (post, position) ->
            if (post != null && position != null) {
                adapter.updatePost(position, post)
            }
        }

        return binding.root
    }

    fun onLikeClick(postId: String, position: Int) {
        viewModel.toggleLike(postId, auth.currentUser?.email.toString(), position)
    }
}
