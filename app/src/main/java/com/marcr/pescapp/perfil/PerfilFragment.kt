package com.marcr.pescapp.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.adapter.PostsProfileAdapter
import com.marcr.pescapp.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val viewModel: ViewModelPerfil by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater)
        // Inflate the layout for this fragment

        binding.editTextEdit.setOnClickListener{
            findNavController().navigate(R.id.action_perfilFragment2_to_perfilDadesFragment, null)
        }

        auth = FirebaseAuth.getInstance()
        var userLog = auth.currentUser


        if (userLog != null) {
            viewModel.getUserProfile(userLog.email!!, requireContext()) { user ->
                user?.let {
                    binding.textViewName.setText(user.name)
                    binding.textViewDescription.setText(user.description)
                    binding.textViewFollowers.setText("Seguidores: "+user.followersList.size)
                    binding.textViewFollowing.setText("Seguiendo: "+user.followingList.size)

                    Glide.with(requireContext())
                        .load(user.img)
                        .into(binding.imageProfile)
                } ?: run {
                    Toast.makeText(binding.root.context, "Documento no encontrado ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val manager = LinearLayoutManager(requireContext())

        binding.recyclerPostProfile.layoutManager = manager


        viewModel.getUserPosts()

        viewModel.postProfile.observe(viewLifecycleOwner){llistaUserPosts->
            binding.recyclerPostProfile.adapter = PostsProfileAdapter(requireContext(), llistaUserPosts)
        }


        return binding.root
    }
}