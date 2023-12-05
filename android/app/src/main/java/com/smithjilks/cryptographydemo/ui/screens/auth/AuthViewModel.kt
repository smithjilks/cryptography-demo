package com.smithjilks.cryptographydemo.ui.screens.auth

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.R
import com.smithjilks.cryptographydemo.data.DataStoreManager
import com.smithjilks.cryptographydemo.extensions.isValidEmail
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.repository.auth.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {


    private val _networkResult =
        MutableStateFlow<NetworkResponse<AuthResponse, DefaultErrorResponse>?>(null)
    val networkResult: StateFlow<NetworkResponse<AuthResponse, DefaultErrorResponse>?> =
        _networkResult

    var email = mutableStateOf("")
    val emailError = derivedStateOf {
        if (!email.value.isValidEmail()) R.string.email_error else null
    }

    var password =  mutableStateOf("")
    val passwordError = derivedStateOf {
        //Very simple validation. Do better
        if (password.value.length <= 8) R.string.password_error else null
    }

    val isFormValid = derivedStateOf {
        passwordError.value == null && emailError.value == null
    }

    fun submit() {
        val payload = Payload()
        payload["email"] = email.value
        payload["password"] = password.value

        effect {
            val authResponse = authRepository.authUser(payload)
            when(authResponse) {
                is NetworkResponse.Success -> {
                    dataStoreManager.storeValue(DataStoreManager.AUTH_TOKEN, authResponse.body.token ?: "")
                    dataStoreManager.storeValue(DataStoreManager.USER_EMAIL, email.value)
                }
                else -> {}
            }
            _networkResult.value = authResponse
        }
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch(ioDispatcher) { block() }
    }
}