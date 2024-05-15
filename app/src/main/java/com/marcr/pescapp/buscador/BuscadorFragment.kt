package com.marcr.pescapp.buscador

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
import com.marcr.pescapp.adapter.UserSearchAdapter
import com.marcr.pescapp.databinding.FragmentBuscadorBinding

class BuscadorFragment : Fragment() {
    private lateinit var binding: FragmentBuscadorBinding
    private val viewModel: ViewModelBuscador by viewModels()
    private val sharedVM: SharedVM by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBuscadorBinding.inflate(inflater)



        binding.imageGoBack.setOnClickListener{
            findNavController().navigate(R.id.action_buscadorFragment_to_principalPostsFragment2, null)
        }

        val manager = LinearLayoutManager(requireContext())

        binding.recyclerUserSearch.layoutManager = manager


        viewModel.getUserSearch()

        viewModel.usersSearch.observe(viewLifecycleOwner){llistaUserSearch->
            binding.recyclerUserSearch.adapter = UserSearchAdapter(requireContext(), llistaUserSearch, this)
        }

        return binding.root
    }


    fun onUserClick(email: String) {
        sharedVM.setUserSearch(email)
        println(email)
        println("Valor shared: "+sharedVM.userSearch.value.toString())
        findNavController().navigate(R.id.action_buscadorFragment_to_perfilSearchFragment)
    }


}