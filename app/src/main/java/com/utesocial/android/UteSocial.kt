package com.utesocial.android

import android.app.Application
import com.utesocial.android.di.module.AppModule
import com.utesocial.android.di.module.AppModuleImpl
import com.utesocial.android.di.network.AppApi
import com.utesocial.android.di.network.AppApiImpl
import com.utesocial.android.di.repository.AppRepository
import com.utesocial.android.di.repository.AppRepositoryImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class UteSocial : Application() {
    @Inject
    lateinit var appModule: AppModule
//    @Inject
//    lateinit var appApi: AppApi
    override fun onCreate() {
        super.onCreate()

//        val appRepository: AppRepository = AppRepositoryImpl(appApi)
//        appModule = AppModuleImpl(appRepository)
    }
}