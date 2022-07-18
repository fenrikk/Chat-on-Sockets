package com.nikfen.testtask9.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikfen.testtask9.databinding.FragmentUserListBinding
import com.nikfen.testtask9.view.adapter.UserAdapter
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

        val userAdapter = UserAdapter(
            onItemClicked = {
                val action =
                    UserListFragmentDirections.actionToChat(it.id, it.name)
                findNavController().navigate(action)
            }
        )

        binding.buttonReset.setOnClickListener {
            viewModel.resetUser()
            val action =
                UserListFragmentDirections.actionResetName()
            findNavController().navigate(action)
        }
        binding.userView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }

        viewModel.getUsers().observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }
}