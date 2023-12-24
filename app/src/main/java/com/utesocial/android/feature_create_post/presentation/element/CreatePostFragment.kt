package com.utesocial.android.feature_create_post.presentation.element

import android.Manifest
import android.content.Context
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.jakewharton.rxbinding4.view.focusChanges
import com.permissionx.guolindev.PermissionX
import com.utesocial.android.R
import com.utesocial.android.UteSocial
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showDialog
import com.utesocial.android.databinding.FragmentCreatePostBinding
import com.utesocial.android.feature_create_post.domain.model.MediaReq
import com.utesocial.android.feature_create_post.presentation.SpanSizeLookup
import com.utesocial.android.feature_create_post.presentation.adapter.ChooseMediaAdapter
import com.utesocial.android.feature_create_post.presentation.element.partial.CreatePostFragmentInfo
import com.utesocial.android.feature_create_post.presentation.state_holder.CreatePostViewModel
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {

    override lateinit var binding: FragmentCreatePostBinding
    override val viewModel: CreatePostViewModel by viewModels()

    private val infoBinding by lazy { CreatePostFragmentInfo(binding.info) }

    private val data: ArrayList<MediaReq> by lazy { ArrayList() }
//    private val mediaItems: ArrayList<>
    private val chooseMediaAdapter by lazy { ChooseMediaAdapter(this@CreatePostFragment, data) }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCreatePostBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        return binding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()

        binding.apply {
            emojiPickerView.setOnEmojiPickedListener {
                edtContent.append(it.emoji)
            }
            viewModel.apply {

                inputSelectorUsing.observe(viewLifecycleOwner) {selector ->
                    when(selector) {
                        InputSelectorEvent.NoneSelector -> btnSelectorToggle.clearChecked()
                        InputSelectorEvent.EmojiSelector -> {
                            edtContent.clearFocus()
                            context?.apply {
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                            .subscribe {focused ->
                                if(focused && inputSelectorUsing.value != InputSelectorEvent.NoneSelector) {
                                    inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
                                }
                            }
                    )

                btnSelectorToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    when(checkedId) {
                        R.id.btn_emoji_selector -> {
                            if(!isChecked) {
                                emojiPickerView.isVisible = false
                                edtContent.requestFocus()
                                context?.apply {
                                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.showSoftInput(edtContent, InputMethodManager.SHOW_IMPLICIT)
                                }
                            } else {
                                inputSelectorUsing.postValue(InputSelectorEvent.EmojiSelector)
                            }
                        }
                        else -> {}
                    }
                    if(isChecked) {
                        when(checkedId) {
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
                scope.showRequestReasonDialog(deniedList, "Các quyền này cho phép ứng dụng truy cập các tệp Media", "ĐỒNG Ý", "HỦY")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "YouBạn cần phải cho phép các quyền cần thiết trong Cài đặt bằng tay", "ĐỒNG Ý", "HỦY")
            }
            .request { allGranted, _, _ ->
                if(allGranted) {
                    chooseMultipleMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                } else {
                    viewModel.inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
                    Toast.makeText(requireContext().applicationContext, "Ứng dụng không được cấp quyền", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewMedia.adapter = chooseMediaAdapter
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = SpanSizeLookup(5, 1, 2)
        binding.recyclerViewMedia.layoutManager = layoutManager

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMedia)
    }

    private fun setupListener() {
        infoBinding.setupListener(getBaseActivity())
    }

    private val chooseMultipleMediaLauncher = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uri ->
        if (uri.isNotEmpty()) {
            val mediaReq: List<MediaReq> = uri.map {
                val isVideo = getBaseActivity().contentResolver.getType(it)!!.startsWith("video")
                MediaReq(uri = it, isVideo = isVideo)
            }
            val positionInsert = data.size

            data.addAll(mediaReq)
            chooseMediaAdapter.notifyItemInserted(positionInsert)
        }
        viewModel.inputSelectorUsing.postValue(InputSelectorEvent.NoneSelector)
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

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

            data.removeAt(positionRemoved)
            chooseMediaAdapter.notifyItemRemoved(positionRemoved)
        }
    }
}