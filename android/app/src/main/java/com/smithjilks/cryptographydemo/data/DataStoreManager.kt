package com.smithjilks.cryptographydemo.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager @Inject constructor(
    @ApplicationContext applicationContext: Context
) {
    companion object {
        val FIREBASE_REMOTE_CONFIG = stringPreferencesKey("firebase_remote_config")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")

        val UI_PUBLIC_KEY = stringPreferencesKey("ui_public_key")
        val UI_PRIVATE_KEY = stringPreferencesKey("ui_private_key")
        val SERVER_SIGNING_KEY = stringPreferencesKey("server_signing_key")
        val SERVER_PUBLIC_KEY = stringPreferencesKey("server_public_key")


    }

    private val preferencesDataStore = applicationContext.dataStore

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        preferencesDataStore.edit {
            it[key] = value
        }
    }

    fun <T> readValue(key: Preferences.Key<T>): Flow<T?> =
        preferencesDataStore.data.map { it[key] }
}