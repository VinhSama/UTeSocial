package com.utesocial.android.feature_post.presentation.adapter

import android.graphics.drawable.Drawable
import android.media.metrics.PlaybackStateEvent.STATE_ENDED
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.databinding.ItemPostBodyImageBinding
import com.utesocial.android.databinding.ItemPostBodyVideoBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostResource

class PostModelBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val postModel: PostModel,
    private val listener: PostBodyImageListener
) : RecyclerView.Adapter<PostModelBodyImageAdapter.PostBodyMediaViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 1
        private const val TYPE_VIDEO = 2
    }

    @OptIn(UnstableApi::class) override fun onViewAttachedToWindow(holder: PostBodyMediaViewHolder) {
        super.onViewAttachedToWindow(holder)
        if(holder.itemViewType == 2) {
            (holder as PostBodyVideoViewHolder).player?.playWhenReady = true
        }
    }

    @OptIn(UnstableApi::class) override fun onViewDetachedFromWindow(holder: PostBodyMediaViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if(holder.itemViewType == 2) {
            (holder as PostBodyVideoViewHolder).player?.apply {
                playWhenReady = false
                stop()
            }

        }
    }


    abstract inner class PostBodyMediaViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(postResources: List<PostResource>, position: Int)
    }

    @UnstableApi inner class PostBodyVideoViewHolder(private val binding: ItemPostBodyVideoBinding) : PostBodyMediaViewHolder(binding) {
        var player: ExoPlayer?
        private var dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        private var playbackPosition = 0L
        private var playWhenReady = true

        init {
            binding.lifecycleOwner = lifecycleOwner
            player = ExoPlayer.Builder(binding.root.context)
                .setRenderersFactory(DefaultRenderersFactory(binding.root.context))
                .build()
            binding.videoViewPost.player = player

        }

        override fun bind(postResources: List<PostResource>, position: Int) {
            Debug.log("bind", postResources[position].url!!)
            player?.apply {
                player?.seekTo(playbackPosition)

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(postResources[position].url)))
                setMediaSource(source)
                player?.playWhenReady = playWhenReady
                player?.prepare()
            }
        }


        private fun releasePlayer() {
            player?.let { player ->
                playbackPosition = player.currentPosition
                playWhenReady = player.playWhenReady
                player.release()
                this.player = null
            }
        }

    }

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : PostBodyMediaViewHolder(binding) {
        init {
            binding.lifecycleOwner = lifecycleOwner
        }

        override fun bind(postResources: List<PostResource>, position: Int) {
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
                            dataSource: com.bumptech.glide.load.DataSource,
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
                    Debug.log("posss", postModel.toString())
                    listener.onClick(postModel)
                }
            }
        }


    }

    @OptIn(UnstableApi::class) override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostBodyMediaViewHolder {
        return if(viewType == TYPE_IMAGE) {
            PostBodyImageViewHolder(
                ItemPostBodyImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            PostBodyVideoViewHolder(
                ItemPostBodyVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = postModel.postResources.size

    override fun onBindViewHolder(holder: PostBodyMediaViewHolder, position: Int) {
        holder.bind(postModel.postResources, position)
    }

    override fun getItemViewType(position: Int): Int {
        val item = postModel.postResources[position]

        return if(item.resourceType == PostResource.ResourceType.IMAGE) {
            TYPE_IMAGE
        } else {
            TYPE_VIDEO
        }
    }

    interface PostBodyMediaListener {
        fun onClick(postModel: PostModel)
    }

    interface PostBodyImageListener {
        fun onClick(postModel: PostModel)
    }
}