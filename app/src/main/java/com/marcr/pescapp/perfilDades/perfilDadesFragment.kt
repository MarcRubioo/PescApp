package com.marcr.pescapp.perfilDades

import android.app.Activity
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcr.pescapp.FragmentActivity
import com.marcr.pescapp.MainActivity
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentPerfilDadesBinding

class perfilDadesFragment : Fragment() {
    private lateinit var binding: FragmentPerfilDadesBinding
    private val viewModel: ViewModelPerfilDades by viewModels()
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        viewModel.setImageUri(it)
        imageUri = it
        binding.imageProfile.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerfilDadesBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()
        var userLog = auth.currentUser


        binding.imageProfile.setOnClickListener{
            resultLauncher.launch("image/*")
        }

        if (userLog != null) {
            viewModel.getUserData(userLog.email!!, requireContext()) { user ->
                user?.let {
                    binding.editPerfilEmail.setText(user.email)
                    binding.editBio.setText(user.description)
                    binding.editPerfilNom.setText(user.name)
                    binding.editPerfilEdat.setText(user.age)

                    Glide.with(requireContext())
                        .load(user.img)
                        .into(binding.imageProfile)
                } ?: run {
                    Toast.makeText(binding.root.context, "Documento no encontrado ", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.btnGuardar.setOnClickListener {
            if (binding.editPerfilEmail.text.isNotBlank()
                && binding.editPerfilNom.text.isNotEmpty()
                && binding.editPerfilEdat.text.isNotEmpty()
                && binding.editBio.text.isNotEmpty()
                ) {

                if (userLog != null) {
                    viewModel.modifyDataUser(requireContext(), userLog.email.toString(), binding.editPerfilNom.text.toString(), null, binding.editPerfilEdat.text.toString(), binding.editBio.text.toString()) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Datos Modificados!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_perfilDadesFragment_to_perfilFragment2, null)
                        } else {
                            Toast.makeText(requireContext(), "Problemas al modificar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(binding.root.context, "Problemas al modificar los datos", Toast.LENGTH_SHORT).show()
            }
        }


        binding.textViewCerrarSession.setOnClickListener {
            viewModel.logoutUser(requireContext()) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Sesión Cerrada", Toast.LENGTH_SHORT).show()
                    goLogin()

                } else {
                    Toast.makeText(requireContext(), "Problemas al cerrar la sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.imageView3.setOnClickListener{

            findNavController().navigate(R.id.action_perfilDadesFragment_to_perfilFragment2, null)

        }

        return binding.root
    }
    private fun goLogin() {
        val homeIntent = Intent(requireContext(), MainActivity::class.java).apply {}
        startActivity(homeIntent)
    }

}


