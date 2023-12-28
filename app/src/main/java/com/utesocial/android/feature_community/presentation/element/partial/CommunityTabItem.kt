package com.utesocial.android.feature_community.presentation.element.partial

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.utesocial.android.databinding.FragmentCommunityBinding
import com.utesocial.android.databinding.LayoutCommunityTabHeaderBinding

class CommunityTabItem(
    communityBinding: FragmentCommunityBinding,
    title: String
) {
    private var binding: LayoutCommunityTabHeaderBinding
    init {
        binding = LayoutCommunityTabHeaderBinding.inflate(LayoutInflater.from(communityBinding.root.context), communityBinding.communityTabLayout, false)
        binding.txvTitle.text = title
        binding.txvBadge.isVisible = false
        binding.txvBadge.text = 0.toString()
    }

    fun rootView() = binding.root
}