package com.nikfen.testtask9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikfen.testtask9.databinding.MassageItemBinding
import com.nikfen.testtask9.model.MessageDto

class MessageAdapter :
    ListAdapter<MessageDto, MessageAdapter.MessageViewHolder>(MessageDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            MassageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(private val binding: MassageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDto: MessageDto) {
            binding.message.text = messageDto.message
        }
    }

    class MessageDiffUtilCallBack : DiffUtil.ItemCallback<MessageDto>() {
        override fun areItemsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.message == newItem.message
        }

        override fun areContentsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem == newItem
        }

    }
}