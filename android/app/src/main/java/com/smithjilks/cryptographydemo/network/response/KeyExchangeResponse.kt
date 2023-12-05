package com.smithjilks.cryptographydemo.network.response

data class KeyExchangeResponse(
    val serverPublicKey: String,
    val serverSigningKey: String
)