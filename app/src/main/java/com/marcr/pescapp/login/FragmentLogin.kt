package com.marcr.pescapp.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.marcr.pescapp.FragmentActivity
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentLoginBinding


class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: ViewModelLogin by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.textViewCrearCompteBTN.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentRegistre, null)
        }

        setup()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Inicio de sessión correcto", Toast.LENGTH_SHORT).show()
                showHome()
            } else {
                Toast.makeText(requireContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setup() {
        binding.btnLoginNext.setOnClickListener {
            if (binding.editTextUser.text.isNotBlank() && binding.editTextPassword.text.isNotEmpty()) {
                viewModel.loginUser(
                    requireContext(),
                    binding.editTextUser.text.toString(),
                    binding.editTextPassword.text.toString()
                )
            } else {
                Toast.makeText(requireContext(), "Llena todos los campos ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showHome() {
        val homeIntent = Intent(requireContext(), FragmentActivity::class.java).apply {}
        startActivity(homeIntent)
    }
}