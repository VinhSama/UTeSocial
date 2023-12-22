package com.utesocial.android.feature_create_post.presentation.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraCreateImageBinding
import com.utesocial.android.databinding.ItemFraCreateVideoBinding
import com.utesocial.android.feature_create_post.domain.model.MediaReq

class ChooseMediaAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val data: List<MediaReq>
) : Adapter<ViewHolder>() {

    companion object {

        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
    }

    inner class ImageViewHolder(private val binding: ItemFraCreateImageBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(mediaReq: MediaReq) {
            binding.uri = mediaReq.uri
        }
    }

    inner class VideoViewHolder(private val binding: ItemFraCreateVideoBinding) : ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(mediaReq: MediaReq) {
            val mediaController = MediaController(binding.root.context)
            mediaController.setAnchorView(binding.videoViewPost)
            binding.videoViewPost.setMediaController(mediaController)

            binding.uri = mediaReq.uri
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = if (viewType == TYPE_VIDEO) {
        VideoViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_create_video, parent, false))
    } else {
        ImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fra_create_image, parent, false))
    }

    override fun getItemViewType(position: Int): Int = if (data[position].isVideo) {
        TYPE_VIDEO
    } else {
        TYPE_IMAGE
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaReq = data[position]
        if (mediaReq.isVideo) {
            (holder as VideoViewHolder).bind(mediaReq)
        } else {
            (holder as ImageViewHolder).bind(mediaReq)
        }
    }
}