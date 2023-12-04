package com.utesocial.android.core.presentation.main.element.partial

import com.google.android.material.search.SearchView
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.databinding.ActivityMainSearchBinding

class MainActivitySearch(
    activity: MainActivity,
    private val binding: ActivityMainSearchBinding
) : MainPartial(activity, binding) {

    fun searchView(): SearchView = binding.searchView
}