package com.utesocial.android.core.presentation.util

import android.graphics.Color
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Common
import com.utesocial.android.core.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@BindingAdapter("imageUri")
fun setImageWithUri(
    imageView: ShapeableImageView,
    uri: Uri?
) = imageView.setImageURI(uri)

@BindingAdapter("videoUri")
fun setVideoWithUri(
    videoView: VideoView,
    uri: Uri?
) {
    videoView.setVideoURI(uri)
    videoView.start()
}

@BindingAdapter(value = ["lifecycle", "numberBadge"], requireAll = true)
fun changeNumberBadge(
    materialTextView: MaterialTextView,
    lifecycle: LifecycleOwner,
    numberBadge: Int
) {
    val duration = materialTextView.context.resources.getInteger(R.integer.duration_200).toLong()

    if (numberBadge == 0) {
        lifecycle.lifecycleScope.launch(Dispatchers.IO) {
            delay(duration)
            withContext(Dispatchers.Main) { materialTextView.visibility = GONE }
        }
    } else if (materialTextView.visibility == GONE) {
        materialTextView.text = numberBadge.toString()
        materialTextView.visibility = VISIBLE
    } else {
        materialTextView.isEnabled = false

        lifecycle.lifecycleScope.launch(Dispatchers.IO) {
            delay(duration)
            withContext(Dispatchers.Main) {
                materialTextView.text = numberBadge.toString()
                materialTextView.isEnabled = true
            }
        }
    }
}

@BindingAdapter("empty")
fun emptyData(
    view: View,
    isEmpty: Boolean
) {
    view.animate().setDuration(view.resources.getInteger(R.integer.duration_200).toLong()).apply {
        if (isEmpty) {
            this@apply.alpha(0F)
            view.visibility = GONE
        } else {
            this@apply.alpha(1F)
            view.visibility = VISIBLE
        }
    }
}

@BindingAdapter("notLoading")
fun hideLoading(
    view: View,
    isNotLoading: Boolean
) {
    val durationAnimation = view.resources.getInteger(R.integer.duration_200).toLong()
    val translateAnimation: TranslateAnimation

    if (isNotLoading) {
        translateAnimation = TranslateAnimation(0F, 0F, -view.height.toFloat(), 0F).apply {
            duration = durationAnimation
            fillAfter = true
        }

        view.startAnimation(translateAnimation)
        view.visibility = VISIBLE
    } else {
        translateAnimation = TranslateAnimation(0F, 0F, 0F, -view.height.toFloat()).apply {
            duration = durationAnimation
            fillAfter = true
        }

        view.startAnimation(translateAnimation)
        view.visibility = GONE
    }
}

@BindingAdapter("unread")
fun notificationUnread(
    constraintLayout: ConstraintLayout,
    unread: Boolean
) {
    val hexColor = if (unread) {
        val context = constraintLayout.context
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)

        val color = ContextCompat.getColor(context, typedValue.resourceId)
        "#25" + Integer.toHexString(color).substring(2)
    } else {
        "#00000000"
    }

    constraintLayout.setBackgroundColor(Color.parseColor(hexColor))
}

@BindingAdapter("avatar")
fun setAvatar(
    shapeableImageView: ShapeableImageView,
    avatar: String?
) {
    val requestOptions = RequestOptions()
        .circleCrop()
        .placeholder(R.drawable.pla_oval)
        .error(R.drawable.ico_default_profile)

    Glide.with(shapeableImageView.context).load(avatar).apply(requestOptions).into(shapeableImageView)
}
@BindingAdapter("textTime")
fun setTextTime(
    materialTextView: MaterialTextView?,
    date: Date
) {
    materialTextView?.apply {
        val textTime = Common.getTimeAgo(date, context)
        materialTextView.text = textTime
    }
}

@BindingAdapter("image")
fun setImage(
    shapeableImageView: ShapeableImageView,
    image: String?
) {
    /*if (image != null) {
        Glide.with(subsamplingScaleImageView.context).download(GlideUrl(image))
            .into(object : CustomViewTarget<SubsamplingScaleImageView, File>(subsamplingScaleImageView) {

                override fun onResourceReady(resource: File, transition: Transition<in File>?) = subsamplingScaleImageView.setImage(ImageSource.uri(Uri.fromFile(resource)))

                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    Log.d("VVVVV", "onResourceCleared: $placeholder")
                }
            })
    }*/

    val requestOptions = RequestOptions()
        .placeholder(R.drawable.bac_image_placeholder)
        .error(R.drawable.bac_image_error)

    Glide.with(shapeableImageView.context).load(image).apply(requestOptions).into(shapeableImageView)
}

@BindingAdapter("username")
fun setUsername(
    materialTextView: MaterialTextView,
    user: User?
) {
    val text = user?.username?.ifEmpty {
        materialTextView.context.getString(
            R.string.str_fra_settings_tv_name,
            user.firstName,
            user.lastName
        )
    } ?: ""
    materialTextView.text = text
}

@BindingAdapter("wallpapers")
fun setWallpapers(
    shapeableImageView: ShapeableImageView,
    wallpapers: String?
) {
    val requestOptions = RequestOptions()
        .placeholder(R.drawable.bac_image_placeholder)
        .error(R.drawable.ima_default_group)

    Glide.with(shapeableImageView.context).load(wallpapers).apply(requestOptions).into(shapeableImageView)
}

@BindingAdapter("textMutualFriends")
fun setNumberMutualFriend(
    materialTextView: MaterialTextView,
    number: Int
) {
    val resources = materialTextView.resources
    val text = if (number == 0) {
        resources.getString(R.string.str_ite_fra_request_tv_number_empty)
    } else {
        resources.getString(R.string.str_ite_fra_request_tv_number, number)
    }

    materialTextView.text = text
}

@BindingAdapter(value = ["lifecycle", "showBadge"], requireAll = true)
fun showContainerBadge(
    frameLayout: FrameLayout,
    lifecycle: LifecycleOwner,
    showBadge: Boolean
) {
    if (showBadge) {
        if (frameLayout.visibility == GONE) {
            frameLayout.visibility = VISIBLE
            frameLayout.isEnabled = true
        }
    } else {
        if (frameLayout.visibility == VISIBLE) {
            frameLayout.isEnabled = false
            lifecycle.lifecycleScope.launch(Dispatchers.IO) {
                delay(frameLayout.context.resources.getInteger(R.integer.duration_200).toLong())
                withContext(Dispatchers.Main) { frameLayout.visibility = GONE }
            }
        }
    }
}

@BindingAdapter("loading")
fun showLoading(
    view: View,
    isLoading: Boolean
) {
    val durationAnimation = view.resources.getInteger(R.integer.duration_200).toLong()
    val translateAnimation: TranslateAnimation

    if (isLoading) {
        translateAnimation = TranslateAnimation(0F, 0F, view.height.toFloat(), 0F).apply {
            duration = durationAnimation
            fillAfter = true
        }

        view.startAnimation(translateAnimation)
        view.visibility = VISIBLE
    } else {
        translateAnimation = TranslateAnimation(0F, 0F, 0F, view.height.toFloat()).apply {
            duration = durationAnimation
            fillAfter = true
        }

        view.startAnimation(translateAnimation)
        view.visibility = GONE
    }
}