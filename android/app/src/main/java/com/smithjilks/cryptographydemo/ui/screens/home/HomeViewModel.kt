package com.smithjilks.cryptographydemo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.R
import com.smithjilks.cryptographydemo.data.DataStoreManager
import com.smithjilks.cryptographydemo.extensions.isStringDigitsOnly
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.network.response.PinResponse
import com.smithjilks.cryptographydemo.network.response.SetPinResponse
import com.smithjilks.cryptographydemo.repository.crypto.CryptoRepository
import com.smithjilks.cryptographydemo.repository.pin.PinRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val dataStoreManager: DataStoreManager,
    private val pinRepositoryImpl: PinRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _networkResult =
        MutableStateFlow<NetworkResponse<PinResponse, DefaultErrorResponse>?>(null)
    val networkResult: StateFlow<NetworkResponse<PinResponse, DefaultErrorResponse>?> =
        _networkResult

    private val _setPinNetworkResult =
        MutableStateFlow<NetworkResponse<SetPinResponse, DefaultErrorResponse>?>(null)
    val setPinNetworkResult: StateFlow<NetworkResponse<SetPinResponse, DefaultErrorResponse>?> =
        _setPinNetworkResult



    var pin = mutableStateOf("")
    val pinError = derivedStateOf {
        if (pin.value.length != 4 || !pin.value.isStringDigitsOnly()) R.string.pin_error else null
    }

    val isFormValid = derivedStateOf { pinError.value == null }

    fun submit() {
        effect {
            val userEmail = dataStoreManager.readValue(DataStoreManager.USER_EMAIL).firstOrNull() ?: ""
            val serverPublicKey = dataStoreManager.readValue(DataStoreManager.SERVER_PUBLIC_KEY).firstOrNull() ?: ""

            val encryptedPin = cryptoRepository.encryptUsingRsa(pin.value, serverPublicKey)
            val payload = Payload()
            payload["email"] = userEmail
            payload["pin"] = encryptedPin

            val pinValidationResponse = pinRepositoryImpl.validatePin(payload)
            _networkResult.value = pinValidationResponse

        }
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch(ioDispatcher) { block() }
    }

    fun generateAndPerformKeyExchange() {
        effect {
            cryptoRepository.generateRSAKeyPairAndSave()
            val uiPublicKey = dataStoreManager.readValue(DataStoreManager.UI_PUBLIC_KEY).firstOrNull() ?: ""
            val uiPrivateKey = dataStoreManager.readValue(DataStoreManager.UI_PRIVATE_KEY).firstOrNull() ?: ""

            val payload = Payload()
            payload["uiPublicKey"] = uiPublicKey

            when(val keyExchangeResponse = cryptoRepository.performKeyExchange(payload)) {
                is NetworkResponse.Success -> {
                    val serverSigningKey = cryptoRepository.decryptUsingRsa(keyExchangeResponse.body.serverSigningKey, uiPrivateKey) ?: ""
                    dataStoreManager.storeValue(DataStoreManager.SERVER_SIGNING_KEY, serverSigningKey)
                    dataStoreManager.storeValue(DataStoreManager.SERVER_PUBLIC_KEY, keyExchangeResponse.body.serverPublicKey)
                }
                else -> {}
            }
        }
    }


    fun setPin() {
        effect {
            val userEmail = dataStoreManager.readValue(DataStoreManager.USER_EMAIL).firstOrNull() ?: ""
            val serverPublicKey = dataStoreManager.readValue(DataStoreManager.SERVER_PUBLIC_KEY).firstOrNull() ?: ""

            val encryptedPin = cryptoRepository.encryptUsingRsa(pin.value, serverPublicKey)
            val payload = Payload()
            payload["email"] = userEmail
            payload["pin"] = encryptedPin

            val setPinResponse = pinRepositoryImpl.setPin(payload)
            _setPinNetworkResult.value = setPinResponse

        }
    }

}