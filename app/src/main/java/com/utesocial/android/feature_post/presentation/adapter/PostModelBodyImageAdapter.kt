package com.utesocial.android.feature_post.presentation.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemPostBodyImageBinding
import com.utesocial.android.databinding.ItemPostBodyVideoBinding
import com.utesocial.android.feature_create_post.domain.model.MediaUrl
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostResource

class PostModelBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val postModel: PostModel,
    private val listener: PostBodyImageListener
) : RecyclerView.Adapter<PostModelBodyImageAdapter.PostBodyImageViewHolder>() {

    inner class PostBodyVideoViewHolder(private val binding: ItemPostBodyVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var player: ExoPlayer
        private var playbackPosition = 0L
        private var playWhenReady = true
        init {
            binding.lifecycleOwner = lifecycleOwner
            binding.videoViewPost.player = player
        }
        fun preparePlayer() {
            player = ExoPlayer.Builder(binding.root.context).build()
            player?.playWhenReady = true
            binding.videoViewPost.player = player
            val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaItem = MediaItem.fromUri(Uri.parse(""))
        }
        fun bind(postResources: List<PostResource>, position: Int) {
            binding.apply {

            }
        }

//        fun buildMediaSource(remoteMedia: MediaUrl.RemoteMedia) : MediaSource {
//            val dataSourceFactory = DefaultDataSource.Factory(binding.root.context, androidx.media3.datasource.DataSource.Factory {  })
//        }
    }

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(postResources: List<PostResource>, position: Int) {
            binding.apply {
                Glide
                    .with(imageViewContent.context)
                    .load(postResources[position].url)
                    .error(R.drawable.bac_image_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            shimmerImageView.stopShimmer()
                            shimmerImageView.visibility = View.GONE
                            imageViewContent.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Handler().postDelayed({
                                shimmerImageView.stopShimmer()
                                shimmerImageView.visibility = View.GONE
                                imageViewContent.visibility = View.VISIBLE
                            }, 300)
                            return false
                        }

                    })
                    .into(imageViewContent)
                imageViewContent.setOnClickListener {
                    listener.onClick(postModel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBodyImageViewHolder {
        return PostBodyImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post_body_image, parent, false))
    }

    override fun getItemCount(): Int = postModel.postResources.size

    override fun onBindViewHolder(holder: PostBodyImageViewHolder, position: Int) {
        holder.bind(postModel.postResources, position)
    }

    interface PostBodyImageListener {
        fun onClick(postModel: PostModel)
    }
}