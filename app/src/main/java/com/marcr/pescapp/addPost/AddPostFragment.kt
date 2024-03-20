package com.marcr.pescapp.addPost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcr.pescapp.R
import com.marcr.pescapp.SharedVM
import com.marcr.pescapp.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private val viewModel: ViewModelAddPost by viewModels()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.data?.let { imageUri ->
                binding.imageSelector.setImageURI(imageUri)
                viewModel.setImageUri(imageUri)
            }
        }
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)

        }

        binding.btnAddPost.setOnClickListener {
            if (binding.editTextPublicaion.text.isNotBlank()
                && binding.editTextLugar.text.isNotEmpty()
                && binding.spinnerCategory.selectedItem != "Categoria") {

                if (userLog != null) {

                    viewModel.addPostViewModel(requireContext(), userLog.email.toString(), binding.editTextPublicaion.text.toString(), binding.editTextLugar.text.toString(), binding.spinnerCategory.selectedItem.toString()) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Post Creado!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_addPostFragment2_to_principalPostsFragment2)
                        } else {
                            Toast.makeText(requireContext(), "Problemas al crear el Post", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(binding.root.context, "Problemas al crear el Pots ", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}