package com.smithjilks.cryptographydemo.dto.request

import jakarta.validation.constraints.NotBlank

data class KeyExchangeRequest(
    @get:NotBlank(message = "keyExchangeRequest.uiPublicKey must not be blank")
    val uiPublicKey: String
)
