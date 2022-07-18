package com.nikfen.testtask9.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikfen.testtask9.databinding.FragmentChatBinding
import com.nikfen.testtask9.view.adapter.MessageAdapter
import com.nikfen.testtask9.viewModel.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModel { parametersOf(args.id) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userName.text = args.name
        binding.send.setOnClickListener {
            viewModel.sendMessage(args.id, binding.inputMessage.text.toString())
        }

        val messageAdapter = MessageAdapter()
        binding.messageRecycleView.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }

        viewModel.getMessages().observe(viewLifecycleOwner) {
            Log.d("MyApp", "messages: $it")
            messageAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }
}