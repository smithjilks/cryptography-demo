package com.smithjilks.cryptographydemo.repository.auth

import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse

interface AuthRepository {
    suspend fun authUser(payload: Payload): NetworkResponse<AuthResponse, DefaultErrorResponse>
}