package com.marcr.pescapp.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.FragmentActivity
import com.marcr.pescapp.ProviderType
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentLoginBinding


class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: ViewModelLogin by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

        binding.textViewCrearCompteBTN.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_fragmentRegistre, null)
        }

        setup()

        return binding.root
    }

    private fun setup(){
        binding.btnLoginNext.setOnClickListener{
            if (binding.editTextUser.text.isNotBlank() && binding.editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.editTextUser.text.toString(), binding.editTextPassword.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(binding.root.context, "Credencials Correctes!!! ", Toast.LENGTH_SHORT).show()
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else{
                            Toast.makeText(binding.root.context, "Credencials Incorrectes!!! ", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(binding.root.context, "Llena todos los campos ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showHome(user: String, provider: ProviderType){
        val homeIntent = Intent(requireContext(), FragmentActivity::class.java).apply {
            putExtra("user", user)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}