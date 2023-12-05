package com.smithjilks.cryptographydemo.repository.pin

import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.network.AppApiService
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.AuthResponse
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.network.response.PinResponse
import com.smithjilks.cryptographydemo.network.response.SetPinResponse
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(
    private val appApiService: AppApiService
) : PinRepository {
    override suspend fun validatePin(payload: Payload): NetworkResponse<PinResponse, DefaultErrorResponse> {
        return appApiService.validatePin(payload)
    }

    override suspend fun setPin(payload: Payload): NetworkResponse<SetPinResponse, DefaultErrorResponse> {
        return appApiService.setPin(payload)
    }

}