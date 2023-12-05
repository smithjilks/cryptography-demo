package com.smithjilks.cryptographydemo.dto.request

import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @get:NotBlank(message = "registerRequest.firstName must not be blank")
    val firstName: String,
    @get:NotBlank(message = "registerRequest.lastName must not be blank")
    val lastName: String,
    @get:NotBlank(message = "registerRequest.email must not be blank")
    val email: String,
    @get:NotBlank(message = "registerRequest.password must not be blank")
    val password: String
)
