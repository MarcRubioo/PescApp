package com.marcr.pescapp.principalsPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcr.pescapp.R
import com.marcr.pescapp.adapter.PostAdapter
import com.marcr.pescapp.databinding.FragmentPrincipalPostsBinding

class principalPostsFragment : Fragment() {
    private lateinit var binding: FragmentPrincipalPostsBinding
    private val viewModel: ViewModelPrincipalPosts by viewModels()
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPrincipalPostsBinding.inflate(inflater)

        val manager = LinearLayoutManager(requireContext())

        binding.recyclerPosts.layoutManager = manager

        binding.imageBuscador.setOnClickListener {
            findNavController().navigate(R.id.action_principalPostsFragment2_to_buscadorFragment, null)
        }

        viewModel.getPosts()

        viewModel.posts.observe(viewLifecycleOwner){llistaPost->
            binding.recyclerPosts.adapter = PostAdapter(requireContext(), llistaPost)
        }

        return binding.root
    }
}