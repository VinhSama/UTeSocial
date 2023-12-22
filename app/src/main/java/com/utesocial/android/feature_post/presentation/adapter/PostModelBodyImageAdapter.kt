package com.utesocial.android.feature_post.presentation.adapter

import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
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
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.domain.model.PostResource

class PostModelBodyImageAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val postModel: PostModel,
    private val listener: PostBodyImageListener
) : RecyclerView.Adapter<PostModelBodyImageAdapter.PostBodyImageViewHolder>() {

    inner class PostBodyImageViewHolder(private val binding: ItemPostBodyImageBinding) : RecyclerView.ViewHolder(binding.root) {

        init { binding.lifecycleOwner = lifecycleOwner }

        fun bind(postResources: List<PostResource>, position: Int) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.bac_image_placeholder)
                .error(R.drawable.bac_image_error)
            binding.apply {
                Glide
                    .with(imageViewContent.context)
                    .load(postResources[position].url)
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
                            Handler().postDelayed(Runnable {
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