package com.marcr.pescapp.perfilSearch

import ViewModelPerfilSearch
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
    private var emailUserSearch: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilSearchBinding.inflate(inflater)

        binding.imageGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_perfilSearchFragment_to_buscadorFragment, null)
        }

        auth = FirebaseAuth.getInstance()

        val manager = LinearLayoutManager(requireContext())
        binding.recyclerPostProfile.layoutManager = manager

        sharedVM.userSearch.observe(viewLifecycleOwner) { emailUserSearch ->
            emailUserSearch?.let { email ->
                this.emailUserSearch = email
                println("hola $email")
                viewModel.getUserProfile(email, requireContext()) { user ->
                    user?.let {
                        binding.textViewName.text = user.name
                        binding.textViewDescription.text = user.description
                        binding.textViewFollowers.text = "Seguidores: " + user.followersList.size
                        binding.textViewFollowing.text = "Siguiendo: " + user.followingList.size

                        Glide.with(requireContext())
                            .load(user.img)
                            .into(binding.imageProfile)

                        viewModel.checkIfUserIsFollower(user.email, auth.currentUser?.email.toString())

                        viewModel.isFollower.observe(viewLifecycleOwner) { isFollower ->
                            if (isFollower != null && emailUserSearch != null) {
                                if (isFollower) {
                                    binding.btnseguir.text = "Siguiendo"
                                    binding.btnseguir.setOnClickListener {
                                        viewModel.unfollowUser(emailUserSearch!!, auth.currentUser?.email.toString())
                                        binding.textViewFollowers.text = "Siguidores: " + (user.followersList.size.toInt()).toString()
                                        viewModel.checkIfUserIsFollower(user.email, auth.currentUser?.email.toString())
                                    }
                                } else {
                                    binding.btnseguir.text = "Seguir"
                                    binding.btnseguir.setOnClickListener {
                                        viewModel.followUser(emailUserSearch!!, auth.currentUser?.email.toString())
                                        binding.textViewFollowers.text = "Siguidores: " + (user.followersList.size.toInt()+1).toString()
                                        viewModel.checkIfUserIsFollower(user.email, auth.currentUser?.email.toString())
                                    }
                                }
                            }
                        }

                    } ?: run {
                        Toast.makeText(binding.root.context, "Documento no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        sharedVM.userSearch.value?.let { emailUserSearch ->
            viewModel.getUserPosts(emailUserSearch)
            viewModel.postProfile.observe(viewLifecycleOwner) { llistaUserPosts ->
                binding.recyclerPostProfile.adapter = PostsProfileAdapter(requireContext(), llistaUserPosts)
            }
        }



        viewModel.followResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Siguiendo", Toast.LENGTH_SHORT).show()
                binding.btnseguir.text = "Siguiendo"
            } else {
                Toast.makeText(requireContext(), "No pudiste seguir", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.unfollowResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Dejando de seguir", Toast.LENGTH_SHORT).show()
                binding.btnseguir.text = "Seguir"
            } else {
                Toast.makeText(requireContext(), "No pudiste dejar de seguir", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
