package com.utesocial.android.di.module

import com.google.gson.Gson
import com.utesocial.android.BuildConfig
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.remote.interceptor.MainNetworkInterceptor
import com.utesocial.android.remote.interceptor.UnauthorizedInterceptor
import com.utesocial.android.remote.simpleCallAdapter.SimpleCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Socket
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLEngine
import javax.net.ssl.TrustManager
import javax.net.ssl.X509ExtendedTrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(SimpleCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            run {
                Debug.log("OkHttp", message)
            }
        }
        loggingInterceptor.level = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }
    @Singleton
    @Provides
    fun provideUnauthorizedInterceptor(
        preferenceManager: PreferenceManager,
        loginApiProvider: Provider<LoginApi>
    ): UnauthorizedInterceptor {
        return UnauthorizedInterceptor(preferenceManager, loginApiProvider)
    }
    @Singleton
    @Provides
    fun provideMainNetworkInterceptor(preferenceManager: PreferenceManager) : MainNetworkInterceptor{
        return MainNetworkInterceptor(preferenceManager)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(unauthorizedInterceptor: UnauthorizedInterceptor, mainNetworkInterceptor: MainNetworkInterceptor) : OkHttpClient {
        val builder = OkHttpClient.Builder()
        try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return builder
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(mainNetworkInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .build()
    }
}