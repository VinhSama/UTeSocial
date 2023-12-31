package com.utesocial.android.di.module

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.utesocial.android.feature_community.data.datasource.database.CommunityDatabase
import com.utesocial.android.feature_change_avatar.data.network.ChangeAvatarApi
import com.utesocial.android.feature_change_avatar.domain.use_case.ChangeAvatarUseCase
import com.utesocial.android.feature_change_password.data.network.ChangePasswordApi
import com.utesocial.android.feature_change_password.domain.use_case.ChangePasswordUseCase
import com.utesocial.android.feature_community.data.network.CommunityApi
import com.utesocial.android.feature_community.domain.dao.FriendsListDao
import com.utesocial.android.feature_community.domain.dao.FriendsRemoteKeysDao
import com.utesocial.android.feature_community.domain.use_case.CommunityUseCase
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_login.domain.use_case.LoginUseCase
import com.utesocial.android.feature_post.data.network.PostApi
import com.utesocial.android.feature_post.domain.use_case.PostUseCase
import com.utesocial.android.feature_settings.data.network.SettingsApi
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
    fun provideCommunityApi(retrofit: Retrofit) : CommunityApi {
        return retrofit.create(CommunityApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChangeAvatarApi(retrofit: Retrofit) : ChangeAvatarApi {
        return retrofit.create(ChangeAvatarApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChangePasswordApi(retrofit: Retrofit) : ChangePasswordApi {
        return retrofit.create(ChangePasswordApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlobalDisposable() : CompositeDisposable {
        return CompositeDisposable()
    }
    @Singleton
    @Provides
    fun provideAppApi(loginApi: LoginApi, postApi: PostApi, communityApi: CommunityApi, changeAvatarApi: ChangeAvatarApi, changePasswordApi: ChangePasswordApi) : AppApi {
        return AppApiImpl(
            loginApi = loginApi,
            postApi = postApi,
            communityApi = communityApi,
            changeAvatarApi = changeAvatarApi,
            changePasswordApi = changePasswordApi
        )
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
    fun provideChangeAvatarUseCase(appModule: AppModule) : ChangeAvatarUseCase {
        return appModule.changeAvatarUseCase
    }

    @Singleton
    @Provides
    fun provideChangePasswordUseCase(appModule: AppModule) : ChangePasswordUseCase {
        return appModule.changePasswordUseCase
    }

    @Singleton
    @Provides
    fun provideCommunityUseCase(appModule: AppModule) : CommunityUseCase {
        return appModule.communityUseCase
    }

    @Singleton
    @Provides
    fun provideAuthorizedUser(preferenceManager: PreferenceManager) : BehaviorSubject<User> {
        if(preferenceManager.getString(Constants.ACCESS_TOKEN, null) != null && preferenceManager.getString(Constants.CURRENT_USER, null) != null) {
            val user = preferenceManager.getObject(Constants.CURRENT_USER, User::class.java)
            Debug.log("CurrentUser", user.toString())
            Debug.log("AccessToken", "" + preferenceManager.getString(Constants.ACCESS_TOKEN, null))
            Debug.log("RefreshToken", "" + preferenceManager.getString(Constants.REFRESH_TOKEN, null))
            if(user != null) {
                return BehaviorSubject.createDefault(user)
            }
        }
        return BehaviorSubject.createDefault(User.EMPTY)
    }

    @Singleton
    @Provides
    fun provideUnauthorizedEventBroadcast() : MutableLiveData<Boolean> {
        return MutableLiveData<Boolean>(false)
    }
    @Singleton
    @Provides
    fun provideFriendsDatabase(@ApplicationContext context: Context) : CommunityDatabase {

        return Room
            .databaseBuilder(context, CommunityDatabase::class.java, "friendsCaching")
            .fallbackToDestructiveMigration()
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    Debug.log("FriendsListDatabase", "onCreate")
                }
            } )
            .build()
    }

    @Singleton
    @Provides
    fun provideFriendsListDao(communityDatabase: CommunityDatabase) : FriendsListDao =
        communityDatabase.getFriendsListDao()

    @Singleton
    @Provides
    fun provideFriendsRemoteKeyDao(communityDatabase: CommunityDatabase) : FriendsRemoteKeysDao =
        communityDatabase.getRemoteKeysDao()

}