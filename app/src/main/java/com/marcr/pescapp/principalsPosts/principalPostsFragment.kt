package com.marcr.pescapp.principalsPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.SharedVM
import com.marcr.pescapp.adapter.PostAdapter
import com.marcr.pescapp.databinding.FragmentPrincipalPostsBinding

class principalPostsFragment : Fragment() {
    private lateinit var binding: FragmentPrincipalPostsBinding
    private val viewModel: ViewModelPrincipalPosts by viewModels()
    private lateinit var auth: FirebaseAuth

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

        viewModel.posts.observe(viewLifecycleOwner){llistaPost->
            binding.recyclerPosts.adapter = PostAdapter(requireContext(), llistaPost, this)
        }



        return binding.root
    }

    fun onLikeClick(id: String) {
        viewModel.checkIfIsLike(id, auth.currentUser?.email.toString())
        Toast.makeText(binding.root.context, "Like", Toast.LENGTH_SHORT).show()

        viewModel.isLike.observe(viewLifecycleOwner) { isLike ->
            if (isLike) {
                viewModel.RemoveLike(id, auth.currentUser?.email.toString())
                Toast.makeText(binding.root.context, "TieneLike", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.AddLike(id, auth.currentUser?.email.toString())
                Toast.makeText(binding.root.context, "NoLike", Toast.LENGTH_SHORT).show()

            }
        }
    }
}