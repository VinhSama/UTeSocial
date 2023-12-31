package com.utesocial.android.feature_create_post.presentation.element

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.permissionx.guolindev.PermissionX
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.FileRequestBody
import com.utesocial.android.core.presentation.util.InputMethodLifecycleHelper
import com.utesocial.android.core.presentation.util.Mode
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.hideLoading
import com.utesocial.android.core.presentation.util.showDialog
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.core.presentation.util.showLongToast
import com.utesocial.android.databinding.FragmentCreatePostBinding
import com.utesocial.android.feature_create_post.domain.model.MediaItem
import com.utesocial.android.feature_create_post.domain.model.MediaReq
import com.utesocial.android.feature_create_post.domain.model.MediaUrl
import com.utesocial.android.feature_create_post.presentation.SpanSizeLookup
import com.utesocial.android.feature_create_post.presentation.adapter.ChooseMediaAdapter
import com.utesocial.android.feature_create_post.presentation.adapter.MediaAdapter
import com.utesocial.android.feature_create_post.presentation.element.partial.CreatePostFragmentInfo
import com.utesocial.android.feature_create_post.presentation.state_holder.CreatePostViewModel
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_post.data.network.request.CreatePostRequest
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import java.io.File
import java.net.URI
import java.util.Date

@AndroidEntryPoint
class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {

    override lateinit var binding: FragmentCreatePostBinding
    override val viewModel: CreatePostViewModel by viewModels()
    private var originalMode : Int? = null

    private val infoBinding by lazy { CreatePostFragmentInfo(binding.info) }

    private val data: ArrayList<MediaReq> by lazy { ArrayList() }
    private val mediaItems: ArrayList<MediaItem> by lazy { ArrayList() }
    private val mediaItemsHashSet: LinkedHashSet<MediaItem> by lazy { LinkedHashSet() }
    private val chooseMediaAdapter by lazy { ChooseMediaAdapter(this@CreatePostFragment, data) }
    private val mediaAdapter by lazy { MediaAdapter(viewLifecycleOwner) }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCreatePostBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        originalMode = activity?.window?.attributes?.softInputMode
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun functionNotAvailable() = run {
        showDialog(
            message = "Chức năng này hiện không có sẵn",
            textPositive = "ĐÓNG",
            positiveListener = {
                viewModel.inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
                dismissLoadingDialog()
            }
        )
    }

    private fun notValidContent() = run {
        showDialog(
            message = "Nội dung bài viết không được để trống",
            textPositive = "ĐÓNG",
            positiveListener = {
                viewModel.validateUIState.postValue(false)
                dismissLoadingDialog()
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner
            .lifecycle
            .addObserver(InputMethodLifecycleHelper(activity?.window, Mode.ADJUST_RESIZE, binding.root))
        setupRecyclerView()
        setupListener()

        binding.apply {
            emojiPickerView.setOnEmojiPickedListener {
                edtContent.append(it.emoji)
            }
            viewModel.apply {
                validateUIState.observe(viewLifecycleOwner) {
                    Debug.log("CreatePostFragment", "validateUIState: $it")
                    info.buttonCreate.isEnabled = it == true
                }
                disposable.add(
                    edtContent
                        .textChanges()
                        .skipInitialValue()
                        .subscribe { charSequence ->
                            if (charSequence.toString().trim().isEmpty()) {
                                mediaItems.value?.let { list ->
                                    validateUIState.postValue(list.isNotEmpty())
//                                    info.buttonCreate.isEnabled = list.isNotEmpty()
                                } ?: run {
                                    validateUIState.postValue(false)
//                                    info.buttonCreate.isEnabled = false
                                }
                            } else {
                                Debug.log("CreatePostFragment", "post: true1")
                                validateUIState.postValue(true)
//                                info.buttonCreate.isEnabled = true
                            }
                        }
                )
                viewModel.mediaItems.observe(viewLifecycleOwner) {
                    var valid = it.isNotEmpty()
                    edtContent.let { edt ->
                        valid = valid || edt.text.toString().isNotEmpty()
                        Debug.log("CreatePostFragment", "post2")
                        validateUIState.postValue(valid)
//                        info.buttonCreate.isEnabled = valid
                    }
                    mediaAdapter.submitList(it)
                }

                inputSelectorUsing.observe(viewLifecycleOwner) { selector ->
                    when (selector) {
                        InputSelectorEvent.NoneSelector -> btnSelectorToggle.clearChecked()
                        InputSelectorEvent.EmojiSelector -> {
                            edtContent.clearFocus()
                            context?.apply {
                                val imm =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(edtContent.windowToken, 0)
                            }
                            emojiPickerView.isVisible = true
                        }

                        InputSelectorEvent.LocationSelector -> functionNotAvailable()
                        InputSelectorEvent.MediaSelector -> onMediaSelector()
                        InputSelectorEvent.TagSelector -> functionNotAvailable()
                        else -> {}
                    }
                }

                disposable
                    .add(
                        edtContent.focusChanges()
                            .skipInitialValue()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { focused ->
                                if (focused && inputSelectorUsing.value != InputSelectorEvent.NoneSelector) {
                                    inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
                                }
                            }
                    )

                btnSelectorToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    when (checkedId) {
                        R.id.btn_emoji_selector -> {
                            if (!isChecked) {
                                emojiPickerView.isVisible = false
                                edtContent.requestFocus()
                                context?.apply {
                                    val imm =
                                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT)
                                }
                            } else {
                                inputSelectorUsing.postValue(InputSelectorEvent.EmojiSelector)
                            }
                        }

                        else -> {}
                    }
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_location_selector -> {
                                inputSelectorUsing.postValue(InputSelectorEvent.LocationSelector)
                            }

                            R.id.btn_media_selector -> {
                                inputSelectorUsing.postValue(InputSelectorEvent.MediaSelector)
                            }

                            R.id.btn_tag_selector -> {
                                inputSelectorUsing.postValue(InputSelectorEvent.TagSelector)
                            }

                            else -> {}
                        }
                    }
                }


            }
            info.buttonCreate.setOnClickListener {
                var valid = viewModel.mediaItems.value?.isNotEmpty()
                edtContent.apply {
                    valid = valid == true || text.toString().isNotEmpty()
                }
                if (valid != true) {
                    notValidContent()
                } else {
                    var isEmptyResource = false
                    viewModel.mediaItems.value?.let {
                        isEmptyResource = it.isEmpty()
                    }
                    if (isEmptyResource) {
                        viewModel.createPost(CreatePostRequest(content = edtContent.text.toString()))
                            .observe(viewLifecycleOwner) { response ->
                                if (response.isRunning()) {
                                    showLoadingDialog()
                                } else {

                                    if (response.isFailure()) {
                                        dismissLoadingDialog()
                                        showError(response)
                                        return@observe
                                    }
                                    if (response.isSuccessful()) {
                                        dismissLoadingDialog()
                                        showLongToast("Post success!!")
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                }
                            }
                    } else {
                        viewModel.uploadPostResources(getMediaRequestBody())
                            .observe(viewLifecycleOwner) { response ->
                                if (response.isRunning()) {
                                    showLoadingDialog()
                                } else {
                                    if (response.isFailure()) {
                                        dismissLoadingDialog()
                                        showError(response)
                                        return@observe
                                    }
                                    if (response.isSuccessful()) {

                                        val resources = ArrayList<String>()
                                        response.getResponseBody()?.data?.resources?.forEach {
                                            it.id?.let { it1 -> resources.add(it1) }
                                        }
                                        viewModel.createPost(
                                            CreatePostRequest(
                                                edtContent.text.toString(),
                                                resources
                                            )
                                        )
                                            .observe(viewLifecycleOwner) {
                                                if (response.isFailure()) {
                                                    dismissLoadingDialog()
                                                    showError(response)
                                                }
                                                if (response.isSuccessful()) {
                                                    dismissLoadingDialog()
                                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                                    showLongToast("Post success!!")

                                                }
                                            }


                                    }
                                }
                            }
                    }


                }
            }

        }
    }

    private fun onMediaSelector() {
        val permissions = object : ArrayList<String>() {
            init {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                } else {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
        PermissionX.init(this)
            .permissions(permissions)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Các quyền này cho phép ứng dụng truy cập các tệp Media",
                    "ĐỒNG Ý",
                    "HỦY"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "YouBạn cần phải cho phép các quyền cần thiết trong Cài đặt bằng tay",
                    "ĐỒNG Ý",
                    "HỦY"
                )
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    chooseMultipleMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                } else {
                    viewModel.inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Ứng dụng không được cấp quyền",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMedia.adapter = mediaAdapter
//        binding.recyclerViewMedia.adapter = chooseMediaAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = SpanSizeLookup(5, 1, 2)
        binding.recyclerViewMedia.layoutManager = layoutManager

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMedia)
    }

    private fun setupListener() {
        infoBinding.setupListener(getBaseActivity())
    }

    private val chooseMultipleMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri ->
            if (uri.isNotEmpty()) {
                val mediaItems = uri.map {
                    val isVideo =
                        requireActivity().contentResolver.getType(it)!!.startsWith("video")
                    MediaItem(MediaUrl.LocalMedia(it), isVideo)
                }
                mediaItemsHashSet.addAll(mediaItems)
                viewModel.mediaItems.postValue(mediaItemsHashSet.toMutableList())
            }
            viewModel.inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
        }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                getBaseActivity().showSnackbar(getString(R.string.str_remove_item_create_post))
                val positionRemoved = viewHolder.adapterPosition
                val item = mediaAdapter.getItem(positionRemoved)
                mediaItemsHashSet.remove(item)
                viewModel.mediaItems.postValue(mediaItemsHashSet.toMutableList())

            }
        }

    fun getRealPathFromURI(contentUri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity().contentResolver.query(contentUri, proj, null, null, null)
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

    fun getContentType(uri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.MIME_TYPE)
        val cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)
        cursor.use { cursorPointer ->
            val columnIndex =
                cursorPointer?.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            cursorPointer?.moveToFirst()
            if (cursorPointer != null) {
                return columnIndex?.let { cursorPointer.getString(it) }
            }
        }
        return null

    }

    private fun getMediaRequestBody(): MultipartBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val contentResolver = requireActivity().contentResolver
        mediaItemsHashSet.forEach { item ->
            Debug.log(
                "getMediaRequestBody",
                "item: ${(item.mediaUrl as MediaUrl.LocalMedia).uri.toString()}"
            )

//            item.mediaUrl.uri?.let { uri ->
//                val contentType = getContentType(uri)
//                val fileRequestBody =
//                    contentResolver.openInputStream(uri)?.let { FileRequestBody(it, contentType) }
//                fileRequestBody?.let {
//                    var fieldType = "images"
//                    if(item.isVideo) {
//                        fieldType = "videos"
//                    }
//                    val part = MultipartBody.Part.createFormData(fieldType,Date().time.toString(), it)
//                    builder.addPart(part)
//
//                }
//            }

            val file = item.mediaUrl.uri?.let { getRealPathFromURI(it)?.let { File(it) } }
            file?.let {
                var mimeType = "image"
                var fieldType = "images"
                if (item.isVideo) {
                    mimeType = "video"
                    fieldType = "videos"
                }
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

        }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        originalMode?.let {
            activity?.window?.apply {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                    setDecorFitsSystemWindows(true)
                } else {
                    setSoftInputMode(it)
                }
            }
        }
    }
}