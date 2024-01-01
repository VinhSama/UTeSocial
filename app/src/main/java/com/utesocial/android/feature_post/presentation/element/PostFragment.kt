package com.utesocial.android.feature_post.presentation.element

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.feature_post.presentation.adapter.PostImageAdapter
import com.utesocial.android.databinding.FragmentPostBinding
import com.utesocial.android.databinding.ViewDialogConfirmBinding
import com.utesocial.android.feature_post.domain.model.PostModel
import com.utesocial.android.feature_post.presentation.adapter.PostBodyImageAdapter
import com.utesocial.android.feature_post.presentation.adapter.PostModelBodyImageAdapter
import com.utesocial.android.feature_post.presentation.adapter.PostModelImageAdapter
import com.utesocial.android.feature_post.presentation.element.partial.InfoPost
import com.utesocial.android.feature_post.presentation.listener.PostListener
import com.utesocial.android.feature_post.presentation.state_holder.PostViewModel
import com.utesocial.android.feature_profile.presentation.element.ProfileFragmentDirections
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>() {

    override lateinit var binding: FragmentPostBinding
    override val viewModel: PostViewModel by viewModels()
    private val args: PostFragmentArgs by navArgs()

    private val infoBinding by lazy { InfoPost(binding.info) }
    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })
    private val postModel by lazy { args.postModel }

    private lateinit var bindingDialogDelete: ViewDialogConfirmBinding
    private lateinit var dialogDelete: AlertDialog

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPostBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false)
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    private fun setup() {
        val userId = mainViewModel.authorizedUser.value?.userId ?: ""
        val userAuthorId = postModel.userAuthor?.id ?: ""

        if (userId != userAuthorId) {
            infoBinding.hideButtonMenu()
        } else {
            val postListener = object : PostListener {

                override fun onShowDetail(postModel: PostModel) {}

                override fun onDeletePost(postId: String) {
                    bindingDialogDelete.buttonPositive.setOnClickListener {
                        dialogDelete.dismiss()

                        viewModel.deleteMyPost(postId).observe(viewLifecycleOwner) { responseState ->
                            when (responseState.getNetworkState().getStatus()) {
                                Status.RUNNING -> showLoadingDialog()
                                Status.SUCCESS -> {
                                    dismissLoadingDialog()
                                    getBaseActivity().showSnackbar(message = getString(R.string.str_dialog_confirm_delete_post_success))
                                    getBaseActivity().onBackPressedDispatcher.onBackPressed()
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

            infoBinding.setupListener(
                listener = postListener,
                postId = postModel.id
            )
        }
    }

    private fun setupBinding() { binding.postModel = postModel }

    private fun setupRecyclerView() {
//        val postImageAdapter = PostImageAdapter(this@PostFragment, post.images)
        val postModelImageAdapter = PostModelImageAdapter(this@PostFragment, postModel.postResources)
        binding.recyclerViewImage.adapter = postModelImageAdapter
    }

    private fun setupListener() {
        bindingDialogDelete.buttonNeutral.setOnClickListener { dialogDelete.dismiss() }

        binding.textViewContent.setOnClickListener {
            val lines = if (binding.textViewContent.lineCount == 3) 1000 else 3
            ObjectAnimator.ofInt(binding.textViewContent, "maxLines", lines).apply {
                duration = resources.getInteger(R.integer.duration_200).toLong()
                start()
            }
        }
    }
}