package com.example.fundamentalgithub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentalgithub.data.model.User
import com.example.fundamentalgithub.databinding.ItemUserBinding

class UserAdapter(private val onClick: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val listUser = ArrayList<User>()

    fun setData(users: MutableList<User>) {
        clearData()
        addData(users)
        refreshData()
    }

    private fun clearData() {
        listUser.clear()
    }

    private fun addData(users: MutableList<User>) {
        listUser.addAll(users)
    }

    private fun refreshData() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = inflateLayout(parent)
        return createViewHolder(binding)
    }

    private fun inflateLayout(parent: ViewGroup): ItemUserBinding {
        return ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    private fun createViewHolder(binding: ItemUserBinding): ViewHolder {
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getUser(position)
        holder.bind(user)
    }

    private fun getUser(position: Int): User {
        return listUser[position]
    }

    override fun getItemCount(): Int = getUserCount()

    private fun getUserCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            bindData(user)
            setItemClickListener(user)
        }

        private fun bindData(user: User) {
            with(binding) {
                userNameTextView.text = user.login
                githubUrlTextView.text = user.htmlUrl
                loadUserAvatar(user.avatarUrl)
            }
        }

        private fun loadUserAvatar(avatarUrl: String?) {
            avatarUrl?.let {
                binding.userAvatarImageView.load(it) {
                    transformations(CircleCropTransformation())
                }
            }
        }

        private fun setItemClickListener(user: User) {
            itemView.setOnClickListener {
                onClick(user)
            }
        }
    }
}