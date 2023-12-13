package com.utesocial.android.feature_home.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.utesocial.android.R
import com.utesocial.android.feature_post.domain.model.Post
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.feature_post.presentation.adapter.PostAdapter
import com.utesocial.android.feature_post.presentation.listener.PostBodyImageListener
import com.utesocial.android.databinding.FragmentHomeBinding
import com.utesocial.android.feature_home.presentation.state_holder.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    private val data: ArrayList<Post> by lazy { ArrayList() }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding
    }

    override fun initViewModel(): ViewModel = viewModel

    override fun assignLifecycleOwner() { binding.lifecycleOwner = this@HomeFragment }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        val postAdapter = PostAdapter(this@HomeFragment, data, object : PostBodyImageListener {

            override fun onClick(post: Post) {
                val action = HomeFragmentDirections.actionHomePost(post)
                getBaseActivity().navController()?.navigate(action)
                getBaseActivity().handleBar(false)
            }
        })
        binding.recyclerViewPost.adapter = postAdapter
    }

    private fun setupListener() = binding.swipeRefreshLayout.setOnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getSuggestPostsUseCase()
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeState.collect {
                if (!it.isLoading) {
                    data.clear()

                    if (it.posts.isNotEmpty()) {
                        data.addAll(it.posts)
                    } else {
                        getBaseActivity().showSnackbar(message = it.error)
                    }
                }
            }
        }
    }
}