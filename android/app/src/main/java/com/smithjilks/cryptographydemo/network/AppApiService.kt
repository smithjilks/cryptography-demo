package com.smithjilks.cryptographydemo.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.network.response.KeyExchangeResponse
import com.smithjilks.cryptographydemo.network.response.PinResponse
import com.smithjilks.cryptographydemo.network.response.SetPinResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AppApiService {
    @POST("api/v1/auth/authenticate")
    suspend fun authUser(@Body payload: Payload): NetworkResponse<AuthResponse, DefaultErrorResponse>

    @POST("api/v1/keys/exchange")
    suspend fun exchangeKeys(@Body payload: Payload): NetworkResponse<KeyExchangeResponse, DefaultErrorResponse>

    @POST("api/v1/pin/validate")
    suspend fun validatePin(@Body payload: Payload): NetworkResponse<PinResponse, DefaultErrorResponse>

    @POST("api/v1/pin/set")
    suspend fun setPin(@Body payload: Payload): NetworkResponse<SetPinResponse, DefaultErrorResponse>

}