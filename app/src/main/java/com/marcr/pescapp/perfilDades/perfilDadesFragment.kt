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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcr.pescapp.MainActivity
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentPerfilDadesBinding

class perfilDadesFragment : Fragment() {
    private lateinit var binding: FragmentPerfilDadesBinding
    private val viewModel: ViewModelPerfilDades by viewModels()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_PROFILE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == perfilDadesFragment.IMAGE_PICK_PROFILE) {
            data?.data?.let { uri ->
                binding.imageProfile.setImageURI(uri)
                viewModel.setImageUri(uri)
            }
        }
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, perfilDadesFragment.IMAGE_PICK_PROFILE)
        }

        if (userLog != null) {
            viewModel.getUserData(userLog.email!!, requireContext()) { user ->
                user?.let {
                    binding.editPerfilEmail.setText(user.email)
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
                && binding.editPerfilEdat.text.isNotEmpty()) {

                if (userLog != null) {
                    viewModel.modifyDataUser(requireContext(), userLog.email.toString(), binding.editPerfilNom.text.toString(), null, binding.editPerfilEdat.text.toString()) { success ->
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


        binding.btnEliminar.setOnClickListener{
            if (userLog != null) {
                viewModel.deleteUser(requireContext(), userLog.email.toString()) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Usuario Eliminado!", Toast.LENGTH_SHORT).show()
                        showHome()
                    } else {
                        Toast.makeText(requireContext(), "Problemas al eliminar el Usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.imageView3.setOnClickListener{

            findNavController().navigate(R.id.action_perfilDadesFragment_to_perfilFragment2, null)

        }

        return binding.root
    }
    private fun showHome() {
        val homeIntent = Intent(requireContext(), MainActivity::class.java).apply {}
        startActivity(homeIntent)
    }

}


