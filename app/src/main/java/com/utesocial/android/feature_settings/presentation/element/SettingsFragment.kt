package com.utesocial.android.feature_settings.presentation.element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.utesocial.android.R
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override lateinit var binding: FragmentSettingsBinding
    override val viewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })

    private val user by lazy { viewModel.authorizedUser.value }

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingsBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupListener()
    }

    private fun setupBinding() { binding.user = user }

    private fun setupListener() {
        binding.constraintLayoutProfile.setOnClickListener {
            val transitionAvatar = resources.getString(R.string.tra_settings_profile_avatar)
            val transitionName = resources.getString(R.string.tra_settings_profile_name)

            val extras = FragmentNavigatorExtras(
                binding.imageViewAvatar to transitionAvatar,
                binding.textViewName to transitionName
            )
            val action = SettingsFragmentDirections.actionSettingsProfile(user!!)

            getBaseActivity().navController()?.navigate(action, extras)
            getBaseActivity().handleBar(false)
        }

        binding.buttonChangeAvatar.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsChangeAvatar()
            navigation(action)
        }

        binding.buttonChangePassword.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsChangePassword()
            navigation(action)
        }

        binding.buttonLogout.setOnClickListener {
            getBaseActivity().showSnackbar("Đăng xuất!!!")
        }
    }

    private fun navigation(action: NavDirections) {
        getBaseActivity().navController()?.navigate(action)
        getBaseActivity().handleBar(false)
    }
}