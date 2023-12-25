package com.utesocial.android.core.presentation.main.element.partial

import com.google.android.material.search.SearchView
import com.utesocial.android.core.presentation.main.adapter.SearchAdapter
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.databinding.ActivityMainSearchBinding
import com.utesocial.android.feature_notification.presentation.request.adapter.RequestAdapter

class MainActivitySearch(
    activity: MainActivity,
    private val binding: ActivityMainSearchBinding
) : MainPartial(activity, binding) {

    init {
        setupRecyclerView(activity)
    }

    private fun setupRecyclerView(activity: MainActivity) {
        val requestAdapter = SearchAdapter(activity, listOf("", "", "", ""))
        binding.recyclerViewSearch.adapter = requestAdapter
    }

    fun searchView(): SearchView = binding.searchView
}