package com.example.fundamentalgithub.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentalgithub.data.model.FavoriteEntity
import com.example.fundamentalgithub.databinding.ItemUserBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private val userList: ArrayList<FavoriteEntity> = arrayListOf()
    var onUserClick: ((FavoriteEntity) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listUser: ArrayList<FavoriteEntity>) {
        this.userList.clear()
        this.userList.addAll(listUser)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    inner class ViewHolder(private var v: ItemUserBinding) :
        RecyclerView.ViewHolder(v.root) {
        fun bind(item: FavoriteEntity) {
            with(v) {
                userAvatarImageView.load(item.avatarUrl) {
                    transformations(CircleCropTransformation())
                }

                userNameTextView.text = item.login

                if (!item.htmlUrl.isNullOrEmpty()) {
                    githubUrlTextView.text = item.htmlUrl
                } else {
                    githubUrlTextView.text = "No URL available"
                }

                root.setOnClickListener {
                    onUserClick?.invoke(item)
                }
            }
        }
    }
}