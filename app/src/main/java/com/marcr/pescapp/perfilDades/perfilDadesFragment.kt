package com.marcr.pescapp.perfilDades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentPerfilBinding
import com.marcr.pescapp.databinding.FragmentPerfilDadesBinding
import com.marcr.pescapp.perfil.ViewModelPerfil

class perfilDadesFragment : Fragment() {
    private lateinit var binding: FragmentPerfilDadesBinding
    private val viewModel: ViewModelPerfilDades by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}