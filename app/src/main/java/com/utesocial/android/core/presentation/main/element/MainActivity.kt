package com.utesocial.android.core.presentation.main.element

import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import com.utesocial.android.NavActMainDirections
import com.utesocial.android.R
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.core.presentation.auth.element.AuthActivity
import com.utesocial.android.core.presentation.base.BaseActivity
import com.utesocial.android.core.presentation.main.adapter.SearchPagedAdapter
import com.utesocial.android.core.presentation.main.element.partial.MainActivityBottom
import com.utesocial.android.core.presentation.main.element.partial.MainActivityScreen
import com.utesocial.android.core.presentation.main.element.partial.MainActivitySearch
import com.utesocial.android.core.presentation.main.element.partial.MainActivityTop
import com.utesocial.android.core.presentation.main.listener.MainListener
import com.utesocial.android.core.presentation.main.state_holder.MainViewModel
import com.utesocial.android.core.presentation.util.NavigationUICustom
import com.utesocial.android.core.presentation.util.dismissLoadingDialog
import com.utesocial.android.core.presentation.util.showLoadingDialog
import com.utesocial.android.core.presentation.util.showUnauthorizedDialog
import com.utesocial.android.databinding.ActivityMainBinding
import com.utesocial.android.feature_search.domain.model.SearchUser
import com.utesocial.android.remote.networkState.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main) }
    override val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager
    private val bottomBinding by lazy { MainActivityBottom(this@MainActivity, binding.bottomBar) }
    private val screenBinding by lazy { MainActivityScreen(this@MainActivity, binding.screen) }
    private val searchBinding by lazy { MainActivitySearch(this@MainActivity, binding.search) }
    private val topBinding by lazy { MainActivityTop(this@MainActivity, binding.topBar) }

    private val pagedAdapter by lazy { SearchPagedAdapter(
        this@MainActivity,
        object : MainListener {

            override fun onSendFriendRequest(receiverId: String) =
                viewModel.sendFriendRequest(receiverId).observe(this@MainActivity) { response ->
                    when (response.getNetworkState().getStatus()) {
                        Status.RUNNING -> showLoadingDialog()

                        Status.SUCCESS -> {
                            dismissLoadingDialog()
                            showSnackbar(getString(R.string.str_send_friend_request_success))
                        }

                        Status.FAILED -> {
                            dismissLoadingDialog()
                            response.getError()?.let { error ->
                                if (error.undefinedMessage.isNullOrEmpty()) {
                                    Snackbar.make(
                                        binding.root,
                                        error.errorType.stringResId,
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        error.undefinedMessage.toString(),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        else -> return@observe
                    }
                }

            override fun onProfileClick(searchUser: SearchUser) {
                searchBinding.searchView().hide()
                val action = NavActMainDirections.actionMainProfile(searchUser)
                screenBinding.navController().navigate(action)
                handleBar(false)
            }
        }
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setSplashScreenExit(splashScreen)
        setup(splashScreen)
        setupRecyclerView()
        setupListener()
        setupFloatingActionButton()

        viewModel.apply {
            unauthorizedEventBroadcast.observe(this@MainActivity) {
                if(it) {
                    showUnauthorizedDialog {dialog ->
                        handleUnauthorizedEvent()
                        dialog.dismiss()
                    }
                }
            }

            disposable.add(searchUserRequest.distinctUntilChanged().debounce(500, TimeUnit.MILLISECONDS).subscribe {
                if (!it.isNullOrEmpty()) {
                    Handler(Looper.getMainLooper()).post { refreshData(it) }
                }
            })

            disposable.add(binding.search.searchView.editText.textChanges().skipInitialValue().subscribe { charSequence ->
                if (charSequence.toString().trim().isEmpty()) {
                    searchBinding.isClear()
                    searchUserRequest.onNext("")
                } else {
                    searchBinding.isTyping()
                    searchUserRequest.onNext(charSequence.toString())
                }
            })
        }
    }

    private fun setSplashScreenExit(splashScreen: SplashScreen) = splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
        AnimatorInflater.loadAnimator(this@MainActivity, R.animator.animator_act_main_splash_screen_exit).apply {
            setTarget(splashScreenViewProvider.view)
            doOnEnd { splashScreenViewProvider.remove() }
            start()
        }
    }

    private fun setup(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition { true }
        viewModel.apply {
            authorizedUser.observe(this@MainActivity) { user ->
                if(user == User.EMPTY) {
                    Intent(this@MainActivity, AuthActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                } else {
                    disableDragActionBar(topBinding.appBarLayout())
                    setupActionBar(topBinding.relativeLayoutAction(), screenBinding.frameLayoutScreen())
                    setupBottomBar(bottomBinding.bottomAppBar(), bottomBinding.bottomViewOnScrollBehavior())

                    val navController = screenBinding.navController()
                    setupNavController(navController)
                    setupSnackbar(binding.floatingActionButtonCreate)

                    NavigationUICustom.setupWithNavController(
                        this@MainActivity,
                        bottomBinding.bottomNavigationView(),
                        navController,
                        animEnter = R.anim.anim_act_main_bnv_enter,
                        animExit = R.anim.anim_act_main_bnv_exit,
                        animPopEnter = R.anim.anim_act_main_bnv_enter,
                        animPopExit = R.anim.anim_act_main_bnv_exit,
                        animatorEnter = R.animator.animator_act_main_bnv_enter,
                        animatorExit = R.animator.animator_act_main_bnv_exit,
                        animatorPopEnter = R.animator.animator_act_main_bnv_enter,
                        animatorPopExit = R.animator.animator_act_main_bnv_exit,
                        R.id.item_fra_friend_request, R.id.item_fra_settings,
                        R.id.item_fra_friends_list
                    )

                    topBinding.setup()
                    bottomBinding.setup()

                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }

    private fun setupRecyclerView() { searchBinding.setupRecyclerView(pagedAdapter) }

    private fun setupListener() {
        binding.floatingActionButtonCreate.setOnClickListener {
            val action = NavActMainDirections.actionMainCreatePost()
            screenBinding.navController().navigate(action)
            handleBar(false)
        }
        topBinding.setListener(searchBinding.searchView())
    }

    private fun refreshData(search: String) = viewModel.searchUsers(
        search = search,
        limit = 10
    ).observe(this) { paging ->
        pagedAdapter.submitData(lifecycle, paging)
    }

    private fun setupFloatingActionButton() {
        val fab = binding.floatingActionButtonCreate
        val listener = NavController.OnDestinationChangedListener {_, destination, _ ->
            fab.isVisible = destination.id == R.id.item_fra_home
        }

        screenBinding.navController().addOnDestinationChangedListener(listener)
        lifecycle.addObserver(LifecycleEventObserver {_ , event ->
            if(event == Lifecycle.Event.ON_DESTROY) {
                screenBinding.navController().removeOnDestinationChangedListener(listener)
            }
        })
    }
}