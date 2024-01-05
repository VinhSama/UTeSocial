package com.utesocial.android.feature_post.presentation.element

import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.utesocial.android.R
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showDialog
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.databinding.FragmentCommentBottomSheetBinding
import com.utesocial.android.feature_create_post.presentation.state_holder.state.InputSelectorEvent
import com.utesocial.android.feature_post.presentation.adapter.CommentLoadStateAdapter
import com.utesocial.android.feature_post.presentation.adapter.CommentPagedAdapter
import com.utesocial.android.feature_post.presentation.state_holder.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


@AndroidEntryPoint
class CommentBottomDialogFragment : BottomSheetDialogFragment() {
    var binding: FragmentCommentBottomSheetBinding? = null
    private val viewModel : CommentViewModel by viewModels()
    companion object {
        val ARG_POST_ID = "postId"

        const val TAG = "CommentBottomDialogFragment"
        fun newInstance(postId: String) : CommentBottomDialogFragment {
            val args = Bundle()
            args.putString(ARG_POST_ID, postId)
            val fragment = CommentBottomDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var postId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
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

    private val pagedAdapter : CommentPagedAdapter by lazy {
        CommentPagedAdapter()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = arguments?.getString(ARG_POST_ID)
        val behavior = BottomSheetBehavior.from(view.parent as View)
        val params = (view.parent as View).layoutParams
        if(params != null) {
            params.height = WindowManager.LayoutParams.MATCH_PARENT
        }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setupRecyclerView()
        binding?.apply {
            swipeRefreshLayout.setOnRefreshListener {
                refreshComment()
            }
            emojiPickerView.setOnEmojiPickedListener {
                edtComment.append(it.emoji)
            }
            viewModel.apply {
                validateUIState.observe(viewLifecycleOwner) {
                    btnSendComment.isEnabled = it == true
                }
                disposable.add(
                    edtComment
                        .textChanges()
                        .skipInitialValue()
                        .subscribe { charSequence ->
                            validateUIState.postValue(!charSequence.toString().trim().isEmpty())
                        }
                )
                inputSelectorUsing.observe(viewLifecycleOwner) { selector ->
                    when(selector) {
                        InputSelectorEvent.NoneSelector -> btnSelectorToggle.clearChecked()
                        InputSelectorEvent.EmojiSelector -> {
                            edtComment.clearFocus()
                            context?.apply {
                                val imm =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(edtComment.windowToken, 0)
                            }
                            emojiPickerView.isVisible = true
                        }
                        InputSelectorEvent.MediaSelector -> functionNotAvailable()
                        else -> {}
                    }
                }
                disposable.add(
                    edtComment.focusChanges()
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
                                edtComment.requestFocus()
                                context?.apply {
                                    val imm =
                                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.showSoftInput(edtComment, InputMethodManager.SHOW_IMPLICIT)
                                }
                            } else {
                                inputSelectorUsing.postValue(InputSelectorEvent.EmojiSelector)
                            }
                        }

                        else -> {}
                    }
                    if (isChecked) {
                        when (checkedId) {
                            R.id.btn_media_selector -> {
                                inputSelectorUsing.postValue(InputSelectorEvent.MediaSelector)
                            }

                            else -> {}
                        }
                    }
                }
                btnSendComment.setOnClickListener {
                    Toast.makeText(requireActivity(), "Send success", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
    private fun setupRecyclerView() {
        val commentLoadStateAdapter = CommentLoadStateAdapter() {
            pagedAdapter.retry()
        }
        binding?.rcvComments?.adapter = pagedAdapter.withLoadStateFooter(commentLoadStateAdapter)
        pagedAdapter.addLoadStateListener {loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded = it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if(appendFailed) {
                    (loadState.append as LoadState.Error).error.let { ex ->
                        (ex as ResponseException).error?.let { error ->
                            showError(error)
                        }
                    }
                }
                binding?.apply {
                    shimmerLayout.isVisible = loadingState
                    rcvComments.isVisible = !loadingState && !firstFailed
                    emptyView.isVisible = firstEmptyLoaded
                    txvErrorMessage.isVisible = firstFailed

                }
                if(loadState.refresh is LoadState.Error) {
                    val errorState = loadState.refresh as LoadState.Error
                    val errorResponse = errorState.error as ResponseException
                    errorResponse.error?.let { error ->
                        if(error.undefinedMessage.isNullOrEmpty()) {
                            binding?.txvErrorMessage?.text = getString(error.errorType.stringResId)
                        } else {
                            binding?.txvErrorMessage?.text = error.undefinedMessage
                        }
                    }
                }
            }

        }
    }

    private fun getWindowHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels

        }
    }
    private fun refreshComment() {
        binding?.apply {
            postId?.let {
                swipeRefreshLayout.isRefreshing = false
                viewModel.getCommentsInPost(it)
                    .observe(viewLifecycleOwner) { paging ->

                    }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(!viewModel.disposable.isDisposed) {
            viewModel.disposable.dispose()
        }
        binding = null
    }
}