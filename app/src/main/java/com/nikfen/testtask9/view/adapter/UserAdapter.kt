package com.nikfen.testtask9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikfen.testtask9.databinding.UserItemBinding
import com.nikfen.testtask9.model.User

class UserAdapter(
    private val onItemClicked: (String) -> Unit
) :
    ListAdapter<User, UserAdapter.UserViewHolder>(
        UserDiffUtilCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)
    }

    class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, onItemClicked: (String) -> Unit) {
            binding.userItemText.text = user.name
            binding.root.setOnClickListener {
                onItemClicked(user.id)
            }
        }
    }

    class UserDiffUtilCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}