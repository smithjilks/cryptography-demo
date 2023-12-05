package com.smithjilks.cryptographydemo.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.smithjilks.cryptographydemo.BuildConfig
import com.smithjilks.cryptographydemo.data.DataStoreManager
import com.smithjilks.cryptographydemo.data.dataStore
import com.smithjilks.cryptographydemo.firebase.RemoteConfig
import com.smithjilks.cryptographydemo.network.AppApiService
import com.smithjilks.cryptographydemo.network.interceptor.HeadersInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun providesRemoteConfig(
        @ApplicationContext context: Context,
        gson: Gson
    ): RemoteConfig = runBlocking {
        context.dataStore.data.map {
            try {
                val item = gson.fromJson(
                    it[DataStoreManager.FIREBASE_REMOTE_CONFIG],
                    RemoteConfig::class.java
                )
                item
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }
        }.firstOrNull() ?: RemoteConfig()
    }


    @Provides
    @Singleton
    fun providesOkHttp(
        @ApplicationContext context: Context,
        remoteConfig: RemoteConfig,
        headersInterceptor: HeadersInterceptor,
        ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .certificatePinner(getSSLCertificatePinner(remoteConfig))

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp Client").d(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
        builder.addInterceptor(headersInterceptor)

        return builder.build()
    }

    private fun getSSLCertificatePinner(remoteConfig: RemoteConfig): CertificatePinner {
        return if (remoteConfig.certificateLeafSha256.isEmpty() || remoteConfig.certificateIntermediateSha256.isEmpty()) {
            CertificatePinner.Builder().build()
        } else {
            CertificatePinner.Builder()
                .add("ngrok-free.app", remoteConfig.certificateIntermediateSha256)
                .add("ngrok-free.app", remoteConfig.certificateLeafSha256)
                .build()
        }
    }


    @Suppress("UNCHECKED_CAST")
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(gson: Gson) = Firebase.remoteConfig.apply {
        val firebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                TimeUnit.MINUTES.toSeconds(5L)
            } else {
                TimeUnit.MINUTES.toSeconds(12L)
            }
            fetchTimeoutInSeconds = 30
        }
        setConfigSettingsAsync(firebaseRemoteConfigSettings)
        val defaultConfigJsonString = gson.toJson(RemoteConfig())
        val defaultConfig =
            gson.fromJson(defaultConfigJsonString, Map::class.java) as Map<String, Any>
        setDefaultsAsync(defaultConfig)
    }


    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    fun providesAppApiService(builder: Retrofit.Builder): AppApiService {
        return builder
            .baseUrl(BuildConfig.APP_API_BASE_URL)
            .build()
            .create(AppApiService::class.java)
    }
}