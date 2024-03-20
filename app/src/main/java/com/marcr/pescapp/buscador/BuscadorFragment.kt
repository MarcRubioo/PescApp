package com.marcr.pescapp.buscador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentBuscadorBinding
import com.marcr.pescapp.databinding.FragmentLoginBinding
import com.marcr.pescapp.databinding.FragmentPerfilBinding
import com.marcr.pescapp.perfil.ViewModelPerfil

class BuscadorFragment : Fragment() {
    private lateinit var binding: FragmentBuscadorBinding
    private val viewModel: ViewModelBuscador by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBuscadorBinding.inflate(inflater)
        return binding.root
    }
}