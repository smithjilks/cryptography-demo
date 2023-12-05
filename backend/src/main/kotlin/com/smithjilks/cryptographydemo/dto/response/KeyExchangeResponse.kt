package com.smithjilks.cryptographydemo.dto.response

data class KeyExchangeResponse(
    val serverPublicKey: String,
    val serverSigningKey: String
)