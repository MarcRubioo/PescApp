package com.marcr.pescapp.principalsPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcr.pescapp.adapter.PostAdapter
import com.marcr.pescapp.databinding.FragmentPrincipalPostsBinding

class principalPostsFragment : Fragment() {
    private lateinit var binding: FragmentPrincipalPostsBinding
    private val viewModel: ViewModelPrincipalPosts by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrincipalPostsBinding.inflate(inflater)

        val manager = LinearLayoutManager(requireContext())

        binding.recyclerPosts.layoutManager = manager

        viewModel.getPosts()

        viewModel.posts.observe(viewLifecycleOwner){llistaPost->
            binding.recyclerPosts.adapter = PostAdapter(requireContext(), llistaPost)
        }

        return binding.root
    }
}