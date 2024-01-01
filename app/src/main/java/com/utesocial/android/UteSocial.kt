package com.utesocial.android

import android.app.Application
import com.utesocial.android.di.module.AppModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class UteSocial : Application() {
    @Inject
    lateinit var appModule: AppModule
    override fun onCreate() {
        super.onCreate()
    }
}