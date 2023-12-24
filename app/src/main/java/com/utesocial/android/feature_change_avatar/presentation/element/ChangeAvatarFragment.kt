package com.utesocial.android.feature_change_avatar.presentation.element

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentChangeAvatarBinding
import com.utesocial.android.feature_create_post.domain.model.MediaReq

class ChangeAvatarFragment : BaseFragment<FragmentChangeAvatarBinding>() {

    override lateinit var binding: FragmentChangeAvatarBinding
    override val viewModel: ViewModel? = null

    private val requestOptions by lazy { RequestOptions().circleCrop().placeholder(R.drawable.pla_oval).error(R.drawable.ico_default_profile) }
    private var mediaReq: MediaReq? = null

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChangeAvatarBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_avatar, container, false)
        return binding
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.buttonChooseImage.setOnClickListener {
            mediaReq = null
            chooseSingleMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private val chooseSingleMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            mediaReq = MediaReq(uri = uri, isVideo = false)

            Glide.with(this@ChangeAvatarFragment).load(uri).apply(requestOptions).into(binding.imageViewAvatar)
        }
    }
}