package com.utesocial.android.feature_profile.presentation.element

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
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
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.FragmentProfileBinding
import com.utesocial.android.databinding.ViewDialogConfirmBinding
import com.utesocial.android.databinding.ViewDialogInputBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.listener.PostListener
import com.utesocial.android.feature_profile.presentation.adapter.ProfileLoadStateAdapter
import com.utesocial.android.feature_profile.presentation.adapter.ProfilePagedAdapter
import com.utesocial.android.feature_profile.presentation.state_holder.ProfileViewModel
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override lateinit var binding: FragmentProfileBinding
    override val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })
    private val user by lazy { args.user }

    private lateinit var bindingDialogUsername: ViewDialogInputBinding
    private lateinit var dialogUsername: AlertDialog

    private lateinit var bindingDialogDelete: ViewDialogConfirmBinding
    private lateinit var dialogDelete: AlertDialog

    private lateinit var popupMenu: PopupMenu

    private val postListener = object : PostListener {

        override fun onShowDetail(postModel: PostModel) {
            val action = ProfileFragmentDirections.actionProfilePost(postModel)
            getBaseActivity().navController()?.navigate(action)
        }

        override fun onDeletePost(postId: String) {
            bindingDialogDelete.buttonPositive.setOnClickListener {
                dialogDelete.dismiss()

                viewModel.deleteMyPost(postId).observe(viewLifecycleOwner) { responseState ->
                    when (responseState.getNetworkState().getStatus()) {
                        Status.RUNNING -> showLoadingDialog()
                        Status.SUCCESS -> {
                            dismissLoadingDialog()
                            refreshData()
                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_confirm_delete_post_success))
                        }

                        Status.FAILED -> {
                            dismissLoadingDialog()
                            getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_confirm_delete_post_fail))
                        }

                        else -> return@observe
                    }
                }
            }

            dialogDelete.show()
        }
    }
    private val pagedAdapter: ProfilePagedAdapter by lazy {
        ProfilePagedAdapter(
            viewLifecycleOwner,
            postListener,
            mainViewModel.authorizedUser.value?.userId ?: ""
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

        bindingDialogDelete =
            DataBindingUtil.inflate(inflater, R.layout.view_dialog_confirm, container, false)
        val materialAlertDialogBuilderDelete = MaterialAlertDialogBuilder(bindingDialogDelete.root.context)
        materialAlertDialogBuilderDelete.setView(bindingDialogDelete.root)
        dialogDelete = materialAlertDialogBuilderDelete.create()
        dialogDelete.window?.attributes?.windowAnimations = R.style.DialogConfirmDelete

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

        binding.post.recyclerViewPost.layoutParams = RelativeLayout.LayoutParams(
            MATCH_PARENT,
            Resources.getSystem().displayMetrics.heightPixels
        )
    }

    private fun setupBinding() {
        binding.user = user
        binding.isVisit = user.userId != (mainViewModel.authorizedUser.value?.userId ?: "")
        bindingDialogUsername.fragment = this@ProfileFragment
    }

    private fun setupRecyclerView() {
        binding.post.recyclerViewPost.setHasFixedSize(true)
        val profileLoadStateAdapter = ProfileLoadStateAdapter { pagedAdapter.retry() }
        binding.post.recyclerViewPost.adapter =
            pagedAdapter.withLoadStateFooter(profileLoadStateAdapter)

        pagedAdapter.addLoadStateListener { loadState ->
            val loadingState = loadState.source.refresh is LoadState.Loading
            val firstFailed = loadState.source.refresh is LoadState.Error

            if (loadState.refresh is LoadState.Error) {
                val errorState = loadState.refresh as LoadState.Error
                val errorMessage = errorState.error.localizedMessage
                if (errorMessage != null) {
                    getBaseActivity().showSnackbar(message = errorMessage)
                }
            }

            binding.post.recyclerViewPost.isVisible = !loadingState && !firstFailed
            Debug.log(
                "ProfileFragment",
                "recyclerViewPost - visible: " + binding.post.recyclerViewPost.isVisible
            )
            Debug.log("ProfileFragment", "loadingState: $loadingState")
            Debug.log("ProfileFragment", "firstFailed: $firstFailed")

            binding.post.shimmerFrameLayout.isVisible = loadingState
            binding.post.lottieEmptyView.isVisible =
                firstFailed || (loadState.source.refresh is LoadState.NotLoading && pagedAdapter.itemCount == 0)
        }
    }

    private fun setupListener() {
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

        bindingDialogDelete.buttonNeutral.setOnClickListener { dialogDelete.dismiss() }

        binding.buttonBack.setOnClickListener { getBaseActivity().onBackPressedDispatcher.onBackPressed() }
        binding.buttonMenu.setOnClickListener { popupMenu.show() }

        binding.username.buttonChangeUsername.setOnClickListener { dialogUsername.show() }

        binding.info.cardViewInfo.setOnClickListener {
            navigation(
                ProfileFragmentDirections.actionProfileDetail(user)
            )
        }
        binding.info.buttonMore.setOnClickListener {
            navigation(
                ProfileFragmentDirections.actionProfileDetail(user)
            )
        }
    }

    private fun refreshData() {
        val userId = user.userId
        viewModel.getMyPosts(userId, 10).observe(viewLifecycleOwner) { pagingData ->
            pagedAdapter.submitData(lifecycle, pagingData)
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

    /*private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.linearLayoutPosts) }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            bottomSheetBehavior.state = STATE_COLLAPSED
            isEnabled = false
        }
    }
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == STATE_EXPANDED) {
                onBackPressedCallback.isEnabled = true
                getBaseActivity().onBackPressedDispatcher.addCallback(this@ProfileFragment, onBackPressedCallback)
            } else {
                onBackPressedCallback.isEnabled = false
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    private fun setup() { bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback) }

    override fun onDestroyView() {
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
        super.onDestroyView()
    }*/
}