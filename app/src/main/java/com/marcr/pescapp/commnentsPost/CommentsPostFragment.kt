package com.marcr.pescapp.commnentsPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcr.pescapp.R
import com.marcr.pescapp.SharedVM
import com.marcr.pescapp.databinding.FragmentCommentsPostBinding


class CommentsPostFragment : Fragment() {


    private lateinit var binding: FragmentCommentsPostBinding
    private val viewModel: ViewModelCommentsPost by viewModels()
    private val sharedVM: SharedVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentsPostBinding.inflate(inflater)



        binding.imageGoBack.setOnClickListener{
            findNavController().navigate(R.id.action_commentsPostFragment_to_principalPostsFragment2, null)
        }

        val manager = LinearLayoutManager(requireContext())

        binding.recyclerUserSearch.layoutManager = manager


        return binding.root
    }

}