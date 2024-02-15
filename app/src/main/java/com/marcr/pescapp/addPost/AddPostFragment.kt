package com.marcr.pescapp.addPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.marcr.pescapp.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private val viewModel: ViewModelAddPost by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}