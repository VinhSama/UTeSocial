package com.utesocial.android.feature_settings.presentation.element

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.presentation.auth.element.AuthActivity
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.databinding.FragmentSettingsBinding
import com.utesocial.android.feature_settings.presentation.state_holder.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override lateinit var binding: FragmentSettingsBinding
    override val viewModel: SettingsViewModel by viewModels { SettingsViewModel.Factory }

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { getBaseActivity() })
    private val user by lazy { mainViewModel.authorizedUser.value }

    @Inject
    lateinit var preferenceManager: PreferenceManager

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
        observer()
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
            showLoadingDialog()
            val accessToken = HashMap<String, String>()
            accessToken["accessToken"] = preferenceManager.getString(Constants.ACCESS_TOKEN, "")!!
            viewModel.logout(accessToken)
        }
    }

    private fun observer() = lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.responseLogout.collect {
                if (it.isLogout) {
                    preferenceManager.remove(Constants.ACCESS_TOKEN)
                    preferenceManager.remove(Constants.REFRESH_TOKEN)
                    preferenceManager.remove(Constants.CURRENT_USER)

                    Intent(getBaseActivity(), AuthActivity::class.java).apply {
                        viewModel.resetStatus()
                        startActivity(this)

                        getBaseActivity().finish()
                        Toast.makeText(getBaseActivity(), getString(R.string.str_logout_success), LENGTH_SHORT).show()
                    }
                } else if (it.message.isNotEmpty()) {
                    getBaseActivity().showSnackbar(message = getString(R.string.str_error_logout))
                    viewModel.resetStatus()
                }
            }
        }
    }

    private fun navigation(action: NavDirections) {
        getBaseActivity().navController()?.navigate(action)
        getBaseActivity().handleBar(false)
    }
}