package com.marcr.pescapp.commnentsPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.marcr.pescapp.R
import com.marcr.pescapp.adapter.CommentAdapter
import com.marcr.pescapp.databinding.FragmentCommentsPostBinding
import com.marcr.pescapp.SharedVM


class CommentsPostFragment : Fragment() {

    private lateinit var binding: FragmentCommentsPostBinding
    private val viewModel: ViewModelCommentsPost by viewModels()
    private val sharedVM: SharedVM by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsPostBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()

        sharedVM.idPost.observe(viewLifecycleOwner) { postId ->
            postId?.let { id ->
                viewModel.getCommentsByPostId(id) { comments ->
                    binding.recyclerComments.adapter = CommentAdapter(requireContext(), comments)
                }

                binding.btnEnviarComment.setOnClickListener {
                    if (binding.editTextComment.text.isNotBlank()) {
                        viewModel.addComment(id, auth.currentUser?.email.toString(), binding.editTextComment.text.toString()) { success ->
                            if (success) {
                                Toast.makeText(requireContext(), "Comentario creado!", Toast.LENGTH_SHORT).show()
                                viewModel.getCommentsByPostId(id) { updatedComments ->
                                    val commentAdapter = CommentAdapter(requireContext(), updatedComments)
                                    binding.recyclerComments.adapter = commentAdapter
                                }
                                binding.editTextComment.text.clear()
                            } else {
                                Toast.makeText(requireContext(), "Problemas al crear el comentario", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.imageGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_commentsPostFragment_to_principalPostsFragment2)
        }

        binding.recyclerComments.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
}
