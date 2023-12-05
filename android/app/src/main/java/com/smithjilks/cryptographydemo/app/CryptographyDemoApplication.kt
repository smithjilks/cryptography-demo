package com.smithjilks.cryptographydemo.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.smithjilks.cryptographydemo.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CryptographyDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseApp.initializeApp(this)
    }
}