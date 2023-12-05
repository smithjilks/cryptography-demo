package com.smithjilks.cryptographydemo.network.interceptor

import com.smithjilks.cryptographydemo.data.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadersInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain) = runBlocking {
        val authToken = dataStoreManager.readValue(DataStoreManager.AUTH_TOKEN).firstOrNull()
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("Cache-Control", "no-cache")

        if(!chain.request().url.toString().contains("auth")) {
            request.addHeader("Authorization", "Bearer $authToken")
        }
        chain.proceed(request.build())
    }
}
