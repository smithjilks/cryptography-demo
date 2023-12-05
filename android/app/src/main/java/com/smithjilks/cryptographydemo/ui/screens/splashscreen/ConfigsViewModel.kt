package com.smithjilks.cryptographydemo.ui.screens.splashscreen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.smithjilks.cryptographydemo.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConfigsViewModel @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val dataStoreManager: DataStoreManager,
    private val ioDispatcher: CoroutineDispatcher,
    application: Application
) : AndroidViewModel(application) {

    val onSyncComplete = mutableStateOf(false)

    fun initialize() {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Timber.d("Config params updated")
                    updateRemoteConfigPref(firebaseRemoteConfig)
                    onSyncComplete.value = true
                } else {
                    Timber.e(task.exception)
                }
            }

        firebaseRemoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {
                updateRemoteConfigPref(firebaseRemoteConfig)
            }

            override fun onError(error : FirebaseRemoteConfigException) {
                Timber.w("Config update error with code: " + error.code, error)
            }
        })
    }

    private fun updateRemoteConfigPref(firebaseRemoteConfig: FirebaseRemoteConfig) {
        val values = mutableMapOf<String, Any>()

        firebaseRemoteConfig.all.forEach { entry ->
            values[entry.key] = entry.value.asString()
        }

        effect {
            dataStoreManager.storeValue(
                DataStoreManager.FIREBASE_REMOTE_CONFIG,
                JSONObject(values.toMap()).toString()
            )
        }
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch(ioDispatcher) { block() }
    }

}