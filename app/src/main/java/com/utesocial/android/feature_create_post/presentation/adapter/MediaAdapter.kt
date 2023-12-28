package com.utesocial.android.feature_create_post.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraCreateImageBinding
import com.utesocial.android.databinding.ItemFraCreateVideoBinding
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.domain.model.MediaUrl

class MediaAdapter(
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<MediaAdapter.BaseMediaViewHolder>() {
    private var mediaAsyncList : AsyncListDiffer<MediaItem> = AsyncListDiffer(this, MediaItem.DiffCallback())

    companion object {

        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
    }


    abstract class BaseMediaViewHolder(open val binding: ViewDataBinding) : ViewHolder(binding.root) {
         abstract fun bindRemoteMedia(remoteMedia: MediaUrl.RemoteMedia)
         abstract fun bindLocalMedia(localMedia: MediaUrl.LocalMedia)
    }
    inner class ImageMediaViewHolder(override val binding : ItemFraCreateImageBinding) : BaseMediaViewHolder(binding) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        override fun bindRemoteMedia(remoteMedia: MediaUrl.RemoteMedia) {
            Glide.with(binding.root.context)
                .load(remoteMedia.url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.bac_image_error)
                .into(binding.imageViewPost)
        }

        override fun bindLocalMedia(localMedia: MediaUrl.LocalMedia) {
            binding.uri = localMedia.uri
        }
    }


    inner class VideoViewHolder(override val binding: ItemFraCreateVideoBinding) : BaseMediaViewHolder(binding) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        override fun bindRemoteMedia(remoteMedia: MediaUrl.RemoteMedia) {
        }

        override fun bindLocalMedia(localMedia: MediaUrl.LocalMedia) {
            val mediaController = MediaController(binding.root.context)
            mediaController.setAnchorView(binding.videoViewPost)
            binding.videoViewPost.setMediaController(mediaController)
            binding.uri = localMedia.uri
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMediaViewHolder {
        return if(viewType == TYPE_VIDEO) {
            val binding = ItemFraCreateVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VideoViewHolder(binding)
        } else {
            val binding = ItemFraCreateImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImageMediaViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).isVideo) {
            TYPE_VIDEO
        } else {
            TYPE_IMAGE
        }
    }

    override fun getItemCount(): Int {
        return mediaAsyncList.currentList.size
    }

    fun getItem(position : Int) : MediaItem {
        return mediaAsyncList.currentList[position]
    }

    fun submitList(mediaItems : List<MediaItem>) {
        mediaAsyncList.submitList(ArrayList(mediaItems))
    }

    override fun onBindViewHolder(holder: BaseMediaViewHolder, position: Int) {
        val item = getItem(position)
        if(item.mediaUrl.javaClass == MediaUrl.LocalMedia::class.java) {
            holder.bindLocalMedia(item.mediaUrl as MediaUrl.LocalMedia)
        } else {
            holder.bindRemoteMedia(remoteMedia = item.mediaUrl as MediaUrl.RemoteMedia)
        }
    }

}