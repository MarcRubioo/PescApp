package com.marcr.pescapp.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentLoginBinding
import com.marcr.pescapp.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val viewModel: ViewModelPerfil by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}