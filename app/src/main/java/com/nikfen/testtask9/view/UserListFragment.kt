package com.nikfen.testtask9.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikfen.testtask9.databinding.FragmentUserListBinding
import com.nikfen.testtask9.viewModel.UserListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<UserListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
        binding.buttonReset.setOnClickListener {
            viewModel.resetUser()
            val action = UserListFragmentDirections.actionUserListFragmentToAuthorizationFragment()
            findNavController().navigate(action)
        }
        viewModel.getUsers().observe(viewLifecycleOwner) {

        }
    }
}