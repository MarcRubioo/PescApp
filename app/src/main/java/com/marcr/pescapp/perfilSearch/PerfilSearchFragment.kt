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

        viewModel.followResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Seguiendo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No lo pudistes seguir", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.unfollowResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Dejando de seguir", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No lo pudistes dejar de seguir", Toast.LENGTH_SHORT).show()
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
                        binding.textViewFollowers.text = "Seguidores: " + user.followersList.size
                        binding.textViewFollowing.text = "Siguiendo: " + user.followingList.size

                        Glide.with(requireContext())
                            .load(user.img)
                            .into(binding.imageProfile)

                        viewModel.checkIfUserIsFollower(user.email, auth.currentUser?.email.toString())

                        viewModel.isFollower.observe(viewLifecycleOwner) { isFollower ->
                            if (isFollower) {
                                binding.btnseguir.setText("Seguiendo")
                                binding.btnseguir.setOnClickListener{
                                    viewModel.unfollowUser(user.email, auth.currentUser?.email.toString())
                                }
                            } else {
                                binding.btnseguir.setText("Seguir")
                                binding.btnseguir.setOnClickListener{
                                    viewModel.followUser(user.email, auth.currentUser?.email.toString())
                                }
                            }
                        }



                    } ?: run {
                        Toast.makeText(binding.root.context, "Documento no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
