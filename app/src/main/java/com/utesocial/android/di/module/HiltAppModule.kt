package com.utesocial.android.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.DateDeserializer
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.core.domain.model.User
import com.utesocial.android.di.network.AppApi
import com.utesocial.android.di.network.AppApiImpl
import com.utesocial.android.di.repository.AppRepository
import com.utesocial.android.di.repository.AppRepositoryImpl
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_post.data.network.PostApi
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Retrofit
import java.util.Date
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class HiltAppModule {
    @Singleton
    @Provides
    fun provideGson() : Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java,
                DateDeserializer()
            )
            .create()
    }

    @Singleton
    @Provides
    fun preferenceManager(@ApplicationContext context: Context) : PreferenceManager {
        return PreferenceManager(context)
    }
    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi {
        Debug.log("provideLoginApi", "retrofit:baseUrl:${retrofit.baseUrl()}")
        return retrofit.create(LoginApi::class.java)
    }
    @Singleton
    @Provides
    fun providePostApi(retrofit: Retrofit) : PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlobalDisposable() : CompositeDisposable {
        return CompositeDisposable()
    }
    @Singleton
    @Provides
    fun provideAppApi(loginApi: LoginApi, postApi: PostApi) : AppApi {
        return AppApiImpl(loginApi = loginApi, postApi = postApi)
    }

    @Singleton
    @Provides
    fun provideAppModule(appApi: AppApi) : AppModule {
        val appRepository : AppRepository = AppRepositoryImpl(appApi)
        return AppModuleImpl(appRepository)
    }
    @Singleton
    @Provides
    fun provideLoginUseCase(appModule: AppModule) : LoginUseCase{
        return appModule.loginUseCase
    }

    @Singleton
    @Provides
    fun providePostUseCase(appModule: AppModule) : PostUseCase{
        return appModule.postUseCase
    }



    @Singleton
    @Provides
    fun provideAuthorizedUser(preferenceManager: PreferenceManager) : BehaviorSubject<User> {
        if(preferenceManager.getString(Constants.ACCESS_TOKEN, null) != null && preferenceManager.getString(Constants.CURRENT_USER, null) != null) {
            val user = preferenceManager.getObject(Constants.CURRENT_USER, User::class.java)
            if(user != null) {
                return BehaviorSubject.createDefault(user)
            }
        }
        return BehaviorSubject.createDefault(User.EMPTY)
    }
}