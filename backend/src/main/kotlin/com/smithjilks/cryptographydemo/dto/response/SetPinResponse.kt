package com.smithjilks.cryptographydemo.dto.response

data class SetPinResponse(
    val success: Boolean = false,
    val statusCode: Int = 0,
    val message: String? = null
)
