package com.smithjilks.cryptographydemo.dto.request

import jakarta.validation.constraints.NotBlank

data class SetPinRequest(
    @get:NotBlank(message = "setPinRequest.email must not be blank")
    val email: String,
    @get:NotBlank(message = "setPinRequest.pin must not be blank")
    val pin: String
)
