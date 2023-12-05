package com.smithjilks.cryptographydemo.repository.pin

import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.network.response.PinResponse
import com.smithjilks.cryptographydemo.network.response.SetPinResponse

interface PinRepository {
    suspend fun validatePin(payload: Payload): NetworkResponse<PinResponse, DefaultErrorResponse>

    suspend fun setPin(payload: Payload): NetworkResponse<SetPinResponse, DefaultErrorResponse>

}