package com.marcr.pescapp.addPost

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private val viewModel: ViewModelAddPost by viewModels()
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        viewModel.setImageUri(it)
        imageUri = it
        binding.imageSelector.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(inflater)
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()
        var userLog = auth.currentUser

        binding.imageSelector.setOnClickListener{
            resultLauncher.launch("image/*")
        }

        binding.btnAddPost.setOnClickListener {
            if (binding.editTextPublicaion.text.isNotBlank()
                && imageUri != null
                && binding.editTextLugar.text.isNotEmpty()
                && binding.spinnerCategory.selectedItem != "Categoria") {

                if (userLog != null) {

                    viewModel.addPostViewModel(requireContext(), viewModel.generarCodigoAleatorio() ,userLog.email.toString(), binding.editTextPublicaion.text.toString(), binding.editTextLugar.text.toString(), binding.spinnerCategory.selectedItem.toString()) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Post Creado!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_addPostFragment2_to_principalPostsFragment2)
                        } else {
                            Toast.makeText(requireContext(), "Problemas al crear el Post", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(binding.root.context, "Rellena todo los campos ", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}