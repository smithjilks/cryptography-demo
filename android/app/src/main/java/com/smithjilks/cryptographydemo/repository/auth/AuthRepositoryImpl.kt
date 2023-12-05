package com.smithjilks.cryptographydemo.repository.auth

import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.network.AppApiService
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val appApiService: AppApiService
) : AuthRepository {
    override suspend fun authUser(payload: Payload): NetworkResponse<AuthResponse, DefaultErrorResponse> {
        return appApiService.authUser(payload)
    }

}