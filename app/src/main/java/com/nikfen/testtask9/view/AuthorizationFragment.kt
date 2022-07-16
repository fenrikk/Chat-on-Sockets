package com.nikfen.testtask9.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikfen.testtask9.databinding.FragmentAuthorizationBinding
import com.nikfen.testtask9.viewModel.AuthorizationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthorizationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.getUsername() != "") {
            val action = AuthorizationFragmentDirections.actionToList()
            findNavController().navigate(action)
        }
        binding.enter.setOnClickListener {
            if (binding.userName.text.toString() == "") {
                Toast.makeText(requireContext(), "Enter username", Toast.LENGTH_LONG).show()
            } else {
                viewModel.saveUsername(binding.userName.text.toString())
                val action = AuthorizationFragmentDirections.actionToList()
                findNavController().navigate(action)
            }
        }
    }
}