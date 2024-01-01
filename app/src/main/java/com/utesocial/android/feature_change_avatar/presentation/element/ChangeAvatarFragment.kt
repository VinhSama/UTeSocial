package com.utesocial.android.feature_change_avatar.presentation.element

import android.Manifest
import android.animation.ObjectAnimator
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.FragmentChangeAvatarBinding
import com.utesocial.android.feature_change_avatar.presentation.state_holder.ChangeAvatarViewModel
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.domain.model.MediaUrl
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Companion.FORM
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ChangeAvatarFragment : BaseFragment<FragmentChangeAvatarBinding>() {

    override lateinit var binding: FragmentChangeAvatarBinding
    override val viewModel: ChangeAvatarViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })

    private val requestOptions by lazy {
        RequestOptions().circleCrop().placeholder(R.drawable.pla_oval)
            .error(R.drawable.ico_default_profile)
    }
    private lateinit var photoCaptureUri: Uri
    private var mediaItem: MediaItem? = null

    private val oaShowChooseImage by lazy {
        ObjectAnimator.ofFloat(
            binding.imageViewChoose,
            "alpha",
            0F,
            0.9F
        ).setDuration(200).apply {
            doOnStart { binding.imageViewChoose.visibility = VISIBLE }
        }
    }
    private val oaHideChooseImage by lazy {
        ObjectAnimator.ofFloat(
            binding.imageViewChoose,
            "alpha",
            0.9F,
            0F
        ).setDuration(200).apply {
            doOnCancel { binding.imageViewChoose.visibility = GONE }
            doOnEnd { binding.imageViewChoose.visibility = GONE }
        }
    }
    private val countDownTimer = object : CountDownTimer(3000, 1000) {

        override fun onTick(p0: Long) {}

        override fun onFinish() = oaHideChooseImage.start()
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChangeAvatarBinding {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_avatar, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBinding()
        setupListener()
        observer()
    }

    private fun setup() {
        photoCaptureUri = createPhotoUri()
        countDownTimer.start()
        binding.buttonUpdateAvatar.isEnabled = false
    }

    private fun setupBinding() {
        binding.mainViewModel = mainViewModel
    }

    private fun setupListener() {
        binding.toolbar.setNavigationOnClickListener { getBaseActivity().onBackPressedDispatcher.onBackPressed() }

        binding.imageViewAvatar.setOnClickListener {
            if (binding.imageViewChoose.alpha == 0F) {
                oaShowChooseImage.start()
                countDownTimer.cancel()
                countDownTimer.start()
            } else {
                oaHideChooseImage.start()
                countDownTimer.cancel()
            }
        }

        binding.imageViewChoose.setOnClickListener { onMediaSelector() }

        binding.buttonUpdateAvatar.setOnClickListener {
            if (mediaItem == null) {
                getBaseActivity().showSnackbar(getString(R.string.str_change_avatar_image_not_found))
                binding.buttonUpdateAvatar.isEnabled = false
                return@setOnClickListener
            }

            viewModel.uploadAvatar(getAvatarRequestBody())
                .observe(viewLifecycleOwner) { responseState ->
                    when (responseState.getNetworkState().getStatus()) {
                        Status.RUNNING -> showLoadingDialog()
                        Status.SUCCESS -> {
                            dismissLoadingDialog()
                            binding.buttonUpdateAvatar.isEnabled = false
                            getBaseActivity().showSnackbar(message = getString(R.string.str_change_avatar_image_success))
                        }

                        Status.FAILED -> {
                            dismissLoadingDialog()
                            binding.buttonUpdateAvatar.isEnabled = false
                            getBaseActivity().showSnackbar(message = getString(R.string.str_change_avatar_image_fail))
                        }

                        else -> return@observe
                    }
                }
        }

        binding.buttonRemoveAvatar.setOnClickListener {
            viewModel.deleteAvatar().observe(viewLifecycleOwner) { response ->
                when (response.getNetworkState().getStatus()) {
                    Status.RUNNING -> showLoadingDialog()
                    Status.SUCCESS -> {
                        dismissLoadingDialog()
                        binding.buttonRemoveAvatar.isEnabled = false
                        getBaseActivity().showSnackbar(getString(R.string.str_change_avatar_remove_success))
                    }

                    Status.FAILED -> {
                        dismissLoadingDialog()
                        response.getError()?.let { error ->
                            if (error.undefinedMessage.isNullOrEmpty()) {
                                Snackbar.make(
                                    binding.root,
                                    error.errorType.stringResId,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    error.undefinedMessage.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    else -> return@observe
                }
            }
        }
    }

    private fun observer() {
        mainViewModel.authorizedUser.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.avatar?.url.isNullOrEmpty()) {
                    binding.buttonRemoveAvatar.isEnabled = false
                }
            }
        }
    }

    private fun getAvatarRequestBody(): MultipartBody {
        val builder = MultipartBody.Builder().setType(FORM)

        Debug.log(
            "getMediaRequestBody",
            "item: ${(mediaItem?.mediaUrl as MediaUrl.LocalMedia).uri.toString()}"
        )

        val file = (mediaItem?.mediaUrl as MediaUrl.LocalMedia).uri?.let {
            getRealPathFromURI(it)?.let { path ->
                File(path)
            }
        }
        file?.let {
            val mimeType = "image"
            val fieldType = "images"

            val extension = file.path.substring(file.path.lastIndexOf(".") + 1)
            val part = file.let {
                MultipartBody.Part.createFormData(
                    fieldType,
                    file.name,
                    it.asRequestBody("${mimeType}/$extension".toMediaTypeOrNull())
                )
            }

            builder.addPart(part)
        }

        return builder.build()
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = getBaseActivity().contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            if (cursor != null) {
                return columnIndex?.let { cursor.getString(it) }
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun onMediaSelector() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        PermissionX.init(this)
            .permissions(permission)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    getString(R.string.str_change_avatar_permission_request_content),
                    getString(R.string.str_change_avatar_permission_request_positive),
                    getString(R.string.str_change_avatar_permission_request_neutral)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.str_change_avatar_permission_forward_content),
                    getString(R.string.str_change_avatar_permission_forward_positive),
                    getString(R.string.str_change_avatar_permission_forward_neutral)
                )
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    /*chooseSingleMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )*/
                    Log.d("VVVVV", "Uri: $photoCaptureUri")
                    openCameraLauncher.launch(photoCaptureUri)
                } else {
                    getBaseActivity().showSnackbar(getString(R.string.str_change_avatar_permission_denied))
                }
            }
    }

    private val chooseSingleMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                if (getBaseActivity().contentResolver.getType(uri)?.endsWith("gif") != false) {
                    getBaseActivity().showSnackbar(getString(R.string.str_change_avatar_image_format_error))
                    return@registerForActivityResult
                }

                mediaItem = MediaItem(mediaUrl = MediaUrl.LocalMedia(uri), isVideo = false)
                binding.buttonUpdateAvatar.isEnabled = true

                Glide.with(this@ChangeAvatarFragment).load(uri).apply(requestOptions)
                    .into(binding.imageViewAvatar)
            }
        }

    private fun createPhotoUri(): Uri {
        val photo = File(getBaseActivity().filesDir, "my_avatar.png")
        return FileProvider.getUriForFile(
            getBaseActivity(),
            getBaseActivity().packageName + ".provider",
            photo
        )
    }

    private fun deleteOldPhoto() {

    }

    private val openCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                mediaItem = MediaItem(mediaUrl = MediaUrl.LocalMedia(photoCaptureUri), isVideo = false)
                binding.buttonUpdateAvatar.isEnabled = true

                Glide.with(this@ChangeAvatarFragment).load(photoCaptureUri).apply(requestOptions)
                    .into(binding.imageViewAvatar)
            } else {
                getBaseActivity().showSnackbar(getString(R.string.str_fra_change_avatar_take_photo_error))
            }
        }
}