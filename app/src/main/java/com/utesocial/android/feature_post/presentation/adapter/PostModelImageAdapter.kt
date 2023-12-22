package com.utesocial.android.feature_post.presentation.adapter

import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.utesocial.android.R
import com.utesocial.android.databinding.ItemFraPostBinding
import com.utesocial.android.feature_post.domain.model.PostResource

class PostModelImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val postResources: List<PostResource>
) : Adapter<PostModelImageAdapter.PostImageViewHolder>() {

    inner class PostImageViewHolder(private val binding: ItemFraPostBinding) : RecyclerView.ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(postResource: PostResource) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.bac_image_placeholder)
                .error(R.drawable.bac_image_error)
            binding.apply {
                Glide
                    .with(imageViewPost.context)
                    .load(postResource.url)
                    .apply(requestOptions)
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
                            imageViewPost.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Handler().postDelayed(Runnable {
                                shimmerImageView.stopShimmer()
                                shimmerImageView.visibility = View.GONE
                                imageViewPost.visibility = View.VISIBLE
                            }, 300)
                            return false
                        }

                    })
                    .into(imageViewPost)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImageViewHolder {
        val binding = ItemFraPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostImageViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int = postResources.size

    override fun onBindViewHolder(holder: PostImageViewHolder, position: Int) = holder.bind(postResources[position])
}