package com.utesocial.android.feature_detail.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.utesocial.android.R
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentDetailBinding
import com.utesocial.android.databinding.FragmentDetailCandidateBinding
import com.utesocial.android.databinding.FragmentDetailLecturerBinding
import com.utesocial.android.databinding.FragmentDetailStudentBinding

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override lateinit var binding: FragmentDetailBinding
    override val viewModel: ViewModel? = null
    private val args: DetailFragmentArgs by navArgs()

    private val user by lazy { args.user }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupListener()
    }

    private fun setupBinding() {
        binding.user = user

        when (user.type) {
            User.UserType.CollegeStudent -> {
                val detailStudentBinding: FragmentDetailStudentBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(binding.linearLayoutOther.context),
                    R.layout.fragment_detail_student,
                    binding.linearLayoutOther,
                    false
                )
                detailStudentBinding.userDetail = user.details
                detailStudentBinding.major = user.details?.major?.name?.get("vi") ?: ""
                binding.frameLayoutOther.addView(detailStudentBinding.root)
            }

            User.UserType.Lecturer -> {
                val detailLecturerBinding: FragmentDetailLecturerBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(binding.linearLayoutOther.context),
                    R.layout.fragment_detail_lecturer,
                    binding.linearLayoutOther,
                    false
                )
                detailLecturerBinding.userDetail = user.details
                binding.frameLayoutOther.addView(detailLecturerBinding.root)
            }

            User.UserType.Candidate -> {
                val detailCandidateBinding: FragmentDetailCandidateBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(binding.linearLayoutOther.context),
                        R.layout.fragment_detail_candidate,
                        binding.linearLayoutOther,
                        false
                    )
                detailCandidateBinding.userDetail = user.details
                detailCandidateBinding.major = user.details?.registeredMajor?.name?.get("vi") ?: ""
                binding.frameLayoutOther.addView(detailCandidateBinding.root)
            }

            else -> {}
        }
    }

    private fun setupListener() {
        binding.toolbar.setNavigationOnClickListener { getBaseActivity().onBackPressedDispatcher.onBackPressed() }
    }
}