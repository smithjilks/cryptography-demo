package com.smithjilks.cryptographydemo.network.response

data class DefaultErrorResponse(
    val success: Boolean = false,
    val statusCode: Int = 0,
    val message: String? = null
)