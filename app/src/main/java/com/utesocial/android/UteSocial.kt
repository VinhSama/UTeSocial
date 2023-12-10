package com.utesocial.android

import android.app.Application
import com.utesocial.android.di.module.AppModule
import com.utesocial.android.di.module.AppModuleImpl
import com.utesocial.android.di.network.AppApi
import com.utesocial.android.di.network.AppApiImpl
import com.utesocial.android.di.repository.AppRepository
import com.utesocial.android.di.repository.AppRepositoryImpl

class UteSocial : Application() {

    lateinit var appModule: AppModule

    override fun onCreate() {
        super.onCreate()

        val appApi: AppApi = AppApiImpl()
        val appRepository: AppRepository = AppRepositoryImpl(appApi)
        appModule = AppModuleImpl(appRepository)
    }
}