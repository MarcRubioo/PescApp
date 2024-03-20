package com.marcr.pescapp.registre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentRegistreBinding

class FragmentRegistre : Fragment() {
    private lateinit var binding: FragmentRegistreBinding
    private val viewModel: ViewModelRegistre by viewModels()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegistreBinding.inflate(inflater)

        setup()

        return binding.root
    }

    private fun setup(){
        binding.btnLoginNext.setOnClickListener{
            if (binding.editTextUser.text.isNotBlank() && binding.editTextEmail2.text.isNotEmpty() && binding.editTextEdat.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty() && binding.editTextConfirmPassword.text.isNotEmpty()){
                if(binding.editTextPassword.text.toString() == (binding.editTextConfirmPassword.text.toString())){
                    viewModel.registerUser(requireContext(), binding.editTextEmail2.text.toString(), binding.editTextUser.text.toString(), binding.editTextPassword.text.toString(), binding.editTextEdat.text.toString(), "") { success ->
                        if (success) {
                            findNavController().navigate(R.id.action_fragmentRegistre_to_fragmentLogin, null)
                        } else {
                            Toast.makeText(requireContext(), "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(binding.root.context, "Las contraseñas no coinciden ", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(binding.root.context, "Llena todos los campos ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}