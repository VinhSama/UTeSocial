package com.utesocial.android.feature_login.presentation.element

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.textChangeEvents
import com.jakewharton.rxbinding4.widget.textChanges
import com.utesocial.android.R
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.domain.util.ValidationResult
import com.utesocial.android.core.presentation.base.BaseFragment
import com.utesocial.android.databinding.FragmentLoginBinding
import com.utesocial.android.feature_login.domain.use_case.ValidateEmail
import com.utesocial.android.feature_login.domain.use_case.ValidatePassword
import com.utesocial.android.feature_login.presentation.state_holder.LoginViewModel
import com.utesocial.android.feature_login.presentation.state_holder.state.LoginFormEvent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override lateinit var binding: FragmentLoginBinding
    override val viewModel: LoginViewModel by viewModels()

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginBinding {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        binding.apply {
            viewModel.apply {
                disposable.add(
                    validationDeliver
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { validationResult ->
                            validationResult?.let {
                                Looper.myLooper()?.let { it1 ->
                                    Handler(it1).post {
                                        when (validationResult) {
                                            is ValidateEmail -> {
                                                textInputLayoutEmail.error =
                                                    when (validationResult.errorType) {
                                                        ValidationResult.ErrorType.EMPTY -> getString(
                                                            validationResult.errorType.stringResourceId
                                                        )

                                                        ValidationResult.ErrorType.EMAIL_INVALID -> getString(
                                                            validationResult.errorType.stringResourceId
                                                        )

                                                        else -> null
                                                    }
                                                textInputLayoutEmail.errorIconDrawable = null
                                            }

                                            is ValidatePassword -> {
                                                textInputLayoutPassword.error =
                                                    when (validationResult.errorType) {
                                                        ValidationResult.ErrorType.EMPTY -> getString(
                                                            validationResult.errorType.stringResourceId
                                                        )

                                                        ValidationResult.ErrorType.PASSWORD_GREATER_THAN_MAXIMUM -> getString(
                                                            validationResult.errorType.stringResourceId
                                                        )

                                                        ValidationResult.ErrorType.PASSWORD_LESS_THAN_MINIMUM -> getString(
                                                            validationResult.errorType.stringResourceId
                                                        )


                                                        else -> null
                                                    }
                                                textInputLayoutPassword.errorIconDrawable = null
                                            }
                                        }
                                    }
                                }
                            }
                        }
                )

                emailInputSubject.value?.let {
                    textInputEditTextEmail.setText(it)
                    disposable.add(
                        textInputEditTextEmail
                            .textChanges()
                            .skipInitialValue()
                            .subscribe { charSequence ->
                                onEvent(LoginFormEvent.EmailChanged(charSequence.toString()))
                                emailInputSubject.onNext(charSequence.toString())
                            }
                    )

                }
                passwordInputSubject.value?.let {
                    textInputEditTextPassword.setText(it)
                    disposable.add(
                        textInputEditTextPassword
                            .textChanges()
                            .skipInitialValue()
                            .subscribe { charSequence ->
                                onEvent(LoginFormEvent.PasswordChanged(charSequence.toString()))
                                passwordInputSubject.onNext(charSequence.toString())
                            }
                    )

                }
                submitUIState.observe(viewLifecycleOwner) {
                    buttonLogin.isEnabled = it
                }

                buttonLogin.setOnClickListener {
                    Snackbar.make(root, "Login handler", Snackbar.LENGTH_SHORT).show()

                }

            }


        }
    }

    private fun setupListener() {
        binding.buttonRegister.setOnClickListener {
//            val transitionName = resources.getString(R.string.tra_login_to_register)
//            val extras = FragmentNavigatorExtras(binding.buttonRegister to transitionName)

            val action = LoginFragmentDirections.actionLoginRegister()
            getBaseActivity().navController()?.navigate(action/*, null, null, extras*/)
        }
    }
}