package com.utesocial.android.core.presentation.main.element.partial

import com.google.android.material.search.SearchView
import com.utesocial.android.core.presentation.main.element.MainActivity
import com.utesocial.android.core.presentation.main.element.MainPartial
import com.utesocial.android.databinding.PartialMainSearchBinding

class MainPartialSearch(
    activity: MainActivity,
    private val binding: PartialMainSearchBinding
) : MainPartial(activity, binding) {

    fun searchView(): SearchView = binding.searchView
}