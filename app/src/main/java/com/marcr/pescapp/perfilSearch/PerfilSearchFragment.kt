package com.marcr.pescapp.perfilSearch

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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.SharedVM
import com.marcr.pescapp.adapter.PostsProfileAdapter
import com.marcr.pescapp.databinding.FragmentPerfilSearchBinding

class PerfilSearchFragment : Fragment() {
    private lateinit var binding: FragmentPerfilSearchBinding
    private val viewModel: ViewModelPerfilSearch by viewModels()
    private lateinit var auth: FirebaseAuth
    private val sharedVM: SharedVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilSearchBinding.inflate(inflater)

        binding.imageGoBack.setOnClickListener{
            findNavController().navigate(R.id.action_perfilSearchFragment_to_buscadorFragment, null)
        }


        var emailUserSearch: String? = null

        sharedVM.userSearch.observe(viewLifecycleOwner) { emailUserSearchValue ->
            emailUserSearch = emailUserSearchValue
            emailUserSearchValue?.let { email ->
                println("hola $email")

            }
        }

        auth = FirebaseAuth.getInstance()

        sharedVM.userSearch.observe(viewLifecycleOwner) { emailUserSearch ->
            emailUserSearch?.let { email ->
                println("hola $email")
                viewModel.getUserProfile(email, requireContext()) { user ->
                    user?.let {
                        binding.textViewName.text = user.name
                        binding.textViewDescription.text = user.description
                        binding.textViewFollowers.text = "Seguidores: " + user.followers
                        binding.textViewFollowing.text = "Siguiendo: " + user.following

                        Glide.with(requireContext())
                            .load(user.img)
                            .into(binding.imageProfile)
                    } ?: run {
                        Toast.makeText(binding.root.context, "Documento no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnseguir.setOnClickListener{
            println("Valor shared coger: "+sharedVM.userSearch.value.toString())
        }

        val manager = LinearLayoutManager(requireContext())
        binding.recyclerPostProfile.layoutManager = manager

        sharedVM.userSearch.value?.let { emailUserSearch ->
            viewModel.getUserPosts(emailUserSearch)
            viewModel.postProfile.observe(viewLifecycleOwner) { llistaUserPosts ->
                binding.recyclerPostProfile.adapter = PostsProfileAdapter(requireContext(), llistaUserPosts)
            }
        }

        return binding.root
    }
}
