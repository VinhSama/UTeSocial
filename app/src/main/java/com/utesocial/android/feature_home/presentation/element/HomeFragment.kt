package com.utesocial.android.feature_home.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.utesocial.android.R
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.FragmentHomeBinding
import com.utesocial.android.feature_home.presentation.state_holder.HomeViewModel
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.domain.model.PostInteraction
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.adapter.PostLoadStateAdapter
import com.utesocial.android.feature_post.presentation.adapter.PostPagedAdapter
import com.utesocial.android.feature_post.presentation.dialog.ChangePrivacyDialog
import com.utesocial.android.feature_post.presentation.dialog.DeletePostDialog
import com.utesocial.android.feature_post.presentation.element.CommentBottomDialogFragment
import com.utesocial.android.feature_post.presentation.listener.PostListener
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override lateinit var binding: FragmentHomeBinding
    override val viewModel: HomeViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })

    private lateinit var changePrivacyDialog: ChangePrivacyDialog
    private lateinit var deletePostDialog: DeletePostDialog

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePrivacyDialog = ChangePrivacyDialog(inflater, container)
        deletePostDialog = DeletePostDialog(inflater, container)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        observer()
    }

    private val postListener = object : PostListener {

        override fun onShowDetail(postModel: PostModel) {
            val action = HomeFragmentDirections.actionHomePost(postModel)
            getBaseActivity().navController()?.navigate(action)
            getBaseActivity().handleBar(false)
        }

        override fun onChangePrivacy(postId: String, privacyMode: Int) {
            changePrivacyDialog.setDefaultChecked(privacyMode)
            changePrivacyDialog.showDialog {
                viewModel.changePrivacy(postId, PrivacyRequest(changePrivacyDialog.getPrivacySelected()))
                    .observe(viewLifecycleOwner) { responseState ->
                        when (responseState.getNetworkState().getStatus()) {
                            Status.RUNNING -> showLoadingDialog()

                            Status.SUCCESS -> {
                                dismissLoadingDialog()
                                getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_scope_success))
                                refreshData()
                            }

                            Status.FAILED -> {
                                dismissLoadingDialog()
                                getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_scope_fail))
                            }

                            else -> return@observe
                        }
                    }
            }
        }

        override fun onChangePrivacy(postModel: PostModel, privacyMode: Int) {
            changePrivacyDialog.setDefaultChecked(privacyMode)
            changePrivacyDialog.showDialog {
                viewModel.changePrivacy(postModel, PrivacyRequest(changePrivacyDialog.getPrivacySelected()))
            }

        }

        override fun onDeletePost(postId: String) {
        }

        override fun onDeletePost(postModel: PostModel) {
            deletePostDialog.showDialog {
                viewModel.deleteMyPost(postModel)
                    .observe(viewLifecycleOwner) { responseState ->
                        if(responseState.isSuccessful()) {
                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_confirm_delete_post_success))
                        }
                        if(responseState.isFailure()) {
                            showError(responseState)
                        }
                }
            }
        }
    }

    private lateinit var pagedAdapter: PostPagedAdapter

    private fun setupRecyclerView() {
        pagedAdapter = PostPagedAdapter(
            viewLifecycleOwner,
            postListener,
            mainViewModel.authorizedUser.value?.userId ?: "",
            object : PostPagedAdapter.OnItemActionsListener {
                override fun onLikeChanged(isChecked: Boolean, postModel: PostModel) : Boolean {
                    when(isChecked) {
                        true -> {
                            if(!viewModel.likeStateInProcessing.contains(postModel.id)) {
                                viewModel.onLikeStateChanged.onNext(
                                    PostInteraction.Like(
                                        postModel.copy()
                                    )
                                )
                                return true
                            }
                            return false
                        }
                        false -> {
                            return if(!viewModel.likeStateInProcessing.contains(postModel.id)) {
                                viewModel.onLikeStateChanged.onNext(
                                    PostInteraction.Unlike(
                                        postModel.copy()
                                    )
                                )
                                true
                            } else {
                                false
                            }

                        }
                    }
                }

                override fun onCommentClicked(postModel: PostModel) {
                    CommentBottomDialogFragment.newInstance(postId = postModel.id).show(childFragmentManager, CommentBottomDialogFragment.TAG)
                }

            }
        )
        val postLoadStateAdapter = PostLoadStateAdapter { pagedAdapter.retry() }

        binding.recyclerViewPost.adapter = pagedAdapter.withLoadStateFooter(postLoadStateAdapter)
        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded =
                    it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                if (appendFailed) {
                    (loadState.append as LoadState.Error).error.let { ex ->
                        (ex as ResponseException).error?.let { error ->
                            showError(error)
                        }
                    }
                }
                binding.shimmerFrameLayout.isVisible = loadingState
                binding.recyclerViewPost.isVisible = !loadingState && !firstFailed
                binding.loadStateViewGroup.isVisible = firstEmptyLoaded || firstFailed
                binding.lottieEmptyView.isVisible = true
                binding.txvErrorMessage.isVisible = firstFailed

                if(loadState.refresh is LoadState.Error) {
                    val errorState = loadState.refresh as LoadState.Error
                    val errorResponse = errorState.error as ResponseException
                    errorResponse.error?.let { error ->
                        if(error.undefinedMessage.isNullOrEmpty()) {
                            binding.txvErrorMessage.text = getString(error.errorType.stringResId)
                        } else {
                            binding.txvErrorMessage.text = error.undefinedMessage
                        }
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun observer() = mainViewModel.authorizedUser.observe(viewLifecycleOwner) {
        it?.let {
            (it != User.EMPTY).run {
                setupRecyclerView()

                refreshData()
                viewModel.onErrorResponseState.observe(viewLifecycleOwner) { errorResponse ->
                    showError(errorResponse)
                }
            }
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getFeedPosts()
            .observe(viewLifecycleOwner) { pagingData ->
                pagedAdapter.submitData(lifecycle, pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }
}