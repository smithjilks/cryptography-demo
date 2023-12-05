package com.smithjilks.cryptographydemo.dto.request

import jakarta.validation.constraints.NotBlank

data class PinRequest(
    @get:NotBlank(message = "registerRequest.email must not be blank")
    val email: String,
    @get:NotBlank(message = "registerRequest.pin must not be blank")
    val pin: String
)
