package com.marcr.pescapp.principalsPosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentLoginBinding

class principalPostsFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: ViewModelPrincipalPosts by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}