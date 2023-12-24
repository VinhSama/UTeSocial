package com.utesocial.android.feature_create_post.presentation.adapter

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.graphics.decodeBitmap
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.databinding.ItemFraCreateImageBinding
import com.utesocial.android.databinding.ItemFraCreateVideoBinding
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.domain.model.MediaUrl
import java.lang.Exception

class MediaAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private var mediaAsyncList : AsyncListDiffer<MediaItem>
) : RecyclerView.Adapter<MediaAdapter.BaseMediaViewHolder>() {

    init {
        mediaAsyncList = AsyncListDiffer(this, MediaItem.DiffCallback())
    }

    companion object {

        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
    }


    abstract inner class BaseMediaViewHolder(open val binding: ViewDataBinding) : ViewHolder(binding.root) {
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
            var bitmap : Bitmap? = null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = localMedia.uri?.let {uri ->
                    ImageDecoder.createSource(binding.root.context.contentResolver,
                        uri
                    )
                }
                bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        binding.root.context.contentResolver, localMedia.uri
                    )
                } catch (e : Exception) {
                    Debug.log("MediaAdapter", "Fail to get bitmap")
                }
            }
            Glide.with(binding.root.context)
                .load(bitmap)
                .error(R.drawable.bac_image_error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageViewPost)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaAdapter.BaseMediaViewHolder {
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

    override fun onBindViewHolder(holder: MediaAdapter.BaseMediaViewHolder, position: Int) {
//        val item = getItem(position)
//        when (val mediaUrl = item.mediaUrl) {
//            is MediaUrl.LocalMedia -> {
//                // Xử lý khi mediaUrl là LocalMedia
//                val localMedia = item.mediaUrl
//                // Do something with localMedia
//            }
//            is MediaUrl.RemoteMedia -> {
//                // Xử lý khi mediaUrl là RemoteMedia
//                val remoteMedia = item.mediaUrl
//                // Do something with remoteMedia
//            }
//            else -> {
//                // Xử lý khi mediaUrl không phải là LocalMedia hoặc RemoteMedia
//            }
//        }
//        when(item.mediaUrl?) {
//            is MediaUrl.LocalMedia -> {
//
//            }
//            else ->
//        }
    }

}