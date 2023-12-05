package com.smithjilks.cryptographydemo.dto.request

import jakarta.validation.constraints.NotBlank

data class AuthenticationRequest(
    @get:NotBlank(message = "registerRequest.email must not be blank")
    val email: String,
    @get:NotBlank(message = "registerRequest.password must not be blank")
    val password: String
)
