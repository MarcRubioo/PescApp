package com.marcr.pescapp.registre

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.FragmentActivity
import com.marcr.pescapp.ProviderType
import com.marcr.pescapp.R
import com.marcr.pescapp.databinding.FragmentLoginBinding
import com.marcr.pescapp.databinding.FragmentRegistreBinding

class FragmentRegistre : Fragment() {
    private lateinit var binding: FragmentRegistreBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRegistreBinding.inflate(inflater)

        //Setup
        setup()

        return binding.root
    }

    private fun setup(){
        binding.btnLoginNext.setOnClickListener{
            if (binding.editTextUser.text.isNotBlank() && binding.editTextEmail2.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.editTextEmail2.text.toString(), binding.editTextPassword.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            findNavController().navigate(R.id.action_fragmentRegistre_to_fragmentLogin, null)
                        } else{
                            Toast.makeText(binding.root.context, "Credencials Incorrectes!!! ", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}