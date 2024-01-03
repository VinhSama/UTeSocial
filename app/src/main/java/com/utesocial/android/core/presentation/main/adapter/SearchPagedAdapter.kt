package com.utesocial.android.core.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.main.listener.MainListener
import com.utesocial.android.databinding.ItemSearchBinding
import com.utesocial.android.feature_search.domain.model.SearchUser

class SearchPagedAdapter(
    private val lifecycleOwner: LifecycleOwner,
    val mainListener: MainListener
) : PagingDataAdapter<SearchUser, ViewHolder>(SearchUserDiffCallback()) {

    class SearchUserDiffCallback : DiffUtil.ItemCallback<SearchUser>() {

        override fun areItemsTheSame(oldItem: SearchUser, newItem: SearchUser): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SearchUser, newItem: SearchUser): Boolean =
            oldItem.userId == newItem.userId

        override fun getChangePayload(oldItem: SearchUser, newItem: SearchUser): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    inner class SearchUserViewHolder(private val binding: ItemSearchBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(searchUser: SearchUser) {
            binding.searchUser = searchUser
            binding.listener = mainListener

            if (searchUser.user.type == User.UserType.Candidate) {
                binding.linearLayoutFaculty.visibility = GONE
            } else {
                val faculty = searchUser.user.details?.faculty?.name
                binding.textViewFaculty.text = faculty
            }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        item?.let { (holder as SearchUserViewHolder).bind(item) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = SearchUserViewHolder(DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_search,
        parent,
        false
    ))
}