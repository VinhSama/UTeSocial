package com.utesocial.android.feature_profile.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.transition.ChangeBounds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.util.ResponseException
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showError
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.FragmentProfileBinding
import com.utesocial.android.databinding.ViewDialogInputBinding
import com.utesocial.android.feature_post.data.network.request.PrivacyRequest
import com.utesocial.android.feature_post.domain.model.PostInteraction
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.adapter.PostPagedAdapter
import com.utesocial.android.feature_post.presentation.dialog.ChangePrivacyDialog
import com.utesocial.android.feature_post.presentation.dialog.DeletePostDialog
import com.utesocial.android.feature_post.presentation.element.CommentBottomDialogFragment
import com.utesocial.android.feature_post.presentation.listener.PostListener
import com.utesocial.android.feature_profile.presentation.adapter.ProfileLoadStateAdapter
import com.utesocial.android.feature_profile.presentation.state_holder.ProfileViewModel
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override lateinit var binding: FragmentProfileBinding
    override val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()

    private val searchUser by lazy { args.searchUser }

    private lateinit var bindingDialogUsername: ViewDialogInputBinding
    private lateinit var dialogUsername: AlertDialog

    private lateinit var changePrivacyDialog: ChangePrivacyDialog
    private lateinit var deletePostDialog: DeletePostDialog

    private lateinit var popupMenu: PopupMenu

    private val postListener = object : PostListener {

        override fun onShowDetail(postModel: PostModel) {
            val action = ProfileFragmentDirections.actionProfilePost(postModel)
            getBaseActivity().navController()?.navigate(action)
        }

        override fun onChangePrivacy(postId: String, privacyMode: Int) {
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
    private val pagedAdapter: PostPagedAdapter by lazy {
        PostPagedAdapter(
            viewLifecycleOwner,
            postListener,
            viewModel.authorizedUser.value?.userId ?: "",
            onItemActionsListener = object : PostPagedAdapter.OnItemActionsListener {
                override fun onLikeChanged(isChecked: Boolean, postModel: PostModel): Boolean {
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
    }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()

        bindingDialogUsername =
            DataBindingUtil.inflate(inflater, R.layout.view_dialog_input, container, false)
        val materialAlertDialogBuilderUsername = MaterialAlertDialogBuilder(bindingDialogUsername.root.context)
        materialAlertDialogBuilderUsername.setView(bindingDialogUsername.root)
        dialogUsername = materialAlertDialogBuilderUsername.create()
        dialogUsername.window?.attributes?.windowAnimations = R.style.DialogInputUsername

        changePrivacyDialog = ChangePrivacyDialog(inflater, container)
        deletePostDialog = DeletePostDialog(inflater, container)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupBinding()
        setupRecyclerView()
        setupListener()

        viewLifecycleOwner.lifecycleScope.launch {
            Debug.log("ProfileFragment", "Start Refresh Data")
            refreshData()
        }
    }

    private fun setup() {
        popupMenu = PopupMenu(binding.buttonMenu.context, binding.buttonMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_fra_profile_btn, popupMenu.menu)

        bindingDialogUsername.buttonPositive.isEnabled = false
    }

    private fun setupBinding() {
        binding.user = searchUser.user
//        (searchUser.userId != (viewModel.authorizedUser.value?.userId ?: "")).apply {
//            Debug.log("isCurrentUser", this.toString())
//            binding.viewGroupBtnState.isVisible = this
//        }
        binding.isVisit =
            searchUser.userId != (viewModel.authorizedUser.value?.userId ?: "")
        when (searchUser.friendState) {
            "Pending" -> {
                binding.buttonRequest.isVisible = searchUser.isSender == true
                binding.btnFriendState.isVisible = true
                binding.btnFriendState.text = getString(R.string.str_fra_profile_sent_friend_request)
            }
            "Accepted" -> {
                binding.btnFriendState.isVisible = true
                binding.btnFriendState.text = getString(R.string.str_fra_profile_be_friend)
                binding.buttonRequest.isVisible = false
            }
            else -> {
                binding.buttonRequest.isVisible = true
                binding.btnFriendState.isVisible = false
                binding.buttonRequest.setOnClickListener {

                }
            }
        }
//        binding.isVisit = searchUser.userId != (viewModel.authorizedUser.value?.userId ?: "")
        bindingDialogUsername.fragment = this@ProfileFragment
    }

    private fun setupRecyclerView() {
        val profileLoadStateAdapter = ProfileLoadStateAdapter { pagedAdapter.retry() }
        binding.recyclerViewPost.adapter =
            pagedAdapter.withLoadStateFooter(profileLoadStateAdapter)

        pagedAdapter.addLoadStateListener { loadState ->
            loadState.mediator?.let {
                val loadingState = it.refresh is LoadState.Loading
                val firstFailed = it.refresh is LoadState.Error
                val firstEmptyLoaded =
                    it.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0
                val appendFailed = it.append is LoadState.Error
                Debug.log("postProfile", pagedAdapter.itemCount.toString())
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
                Debug.log("recyclerView visible", binding.recyclerViewPost.isVisible.toString())

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
        binding.btnRefresh.setOnClickListener {
            refreshData()
        }
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_change_username -> {
                    dialogUsername.show()
                    true
                }

                R.id.item_change_avatar -> {
                    val transitionAvatar = resources.getString(R.string.tra_settings_profile_avatar)
                    val extras =
                        FragmentNavigatorExtras(binding.relativeLayoutAvatar to transitionAvatar)

                    val action = ProfileFragmentDirections.actionProfileAvatar()
                    getBaseActivity().navController()?.navigate(action, extras)

                    getBaseActivity().handleBar(false)
                    true
                }

                R.id.item_change_password -> {
                    navigation(ProfileFragmentDirections.actionProfilePassword())
                    true
                }

                else -> false
            }
        }

        bindingDialogUsername.buttonPositive.setOnClickListener {
            bindingDialogUsername.textInputEditText.clearFocus()

            viewModel.updateUsername(bindingDialogUsername.textInputEditText.text.toString())
                .observe(viewLifecycleOwner) { responseState ->
                    when (responseState.getNetworkState().getStatus()) {
                        Status.RUNNING -> showLoadingDialog()
                        Status.SUCCESS -> {
                            dismissLoadingDialog()

                            binding.username.textViewUsername.text =
                                bindingDialogUsername.textInputEditText.text
                            dialogUsername.dismiss()

                            bindingDialogUsername.textInputEditText.setText("")
                            refreshData()

                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_change_username_success))
                        }

                        Status.FAILED -> {
                            dismissLoadingDialog()
                            bindingDialogUsername.buttonPositive.isEnabled = false
                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_change_username_fail))
                        }

                        else -> return@observe
                    }
                }
        }
        bindingDialogUsername.buttonNeutral.setOnClickListener { dialogUsername.dismiss() }

        binding.buttonBack.setOnClickListener { getBaseActivity().onBackPressedDispatcher.onBackPressed() }
        binding.buttonMenu.setOnClickListener { popupMenu.show() }

        binding.username.buttonChangeUsername.setOnClickListener { dialogUsername.show() }

        binding.info.cardViewInfo.setOnClickListener {
            navigation(
                ProfileFragmentDirections.actionProfileDetail(searchUser.user)
            )
        }
        binding.info.buttonMore.setOnClickListener {
            navigation(
                ProfileFragmentDirections.actionProfileDetail(searchUser.user)
            )
        }
    }

    private fun refreshData() {
        searchUser.userId?.let {
            viewModel.getPostsByUserId(it)
                .observe(viewLifecycleOwner) { pagingData ->
                    pagedAdapter.submitData(lifecycle, pagingData)
                }
        }
    }

    private fun navigation(action: NavDirections) =
        getBaseActivity().navController()?.navigate(action)

    private fun enablePositiveButton() {
        val isInputValid =
            checkValid(bindingDialogUsername.textInputLayout, bindingDialogUsername.textInputEditText)
        bindingDialogUsername.buttonPositive.isEnabled = isInputValid
    }

    private fun checkValid(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ): Boolean = textInputLayout.error.isNullOrEmpty() && !textInputEditText.text.isNullOrEmpty()

    private fun setError(error: String) {
        bindingDialogUsername.textInputLayout.isErrorEnabled = true
        bindingDialogUsername.textInputLayout.error = error
        bindingDialogUsername.buttonPositive.isEnabled = false
    }

    fun checkInputUsername() {
        bindingDialogUsername.textInputLayout.error = null
        bindingDialogUsername.textInputLayout.isErrorEnabled = false
        val text = bindingDialogUsername.textInputEditText.text ?: ""

        if (text.trim().isEmpty()) {
            val error = resources.getString(R.string.str_fra_register_error_empty)
            setError(error)
        } else if (text.trim().length < 3) {
            val error = resources.getString(R.string.str_dialog_change_username_error_min)
            setError(error)
        } else {
            enablePositiveButton()
        }
    }

}