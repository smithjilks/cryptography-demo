package com.smithjilks.cryptographydemo.controller

import com.smithjilks.cryptographydemo.dto.request.AuthenticationRequest
import com.smithjilks.cryptographydemo.dto.request.RegisterRequest
import com.smithjilks.cryptographydemo.dto.response.AuthenticationResponse
import com.smithjilks.cryptographydemo.service.AuthenticationService
import com.smithjilks.cryptographydemo.service.JwtService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Validated
class AuthenticationController(
    val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(registerRequest))
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    fun authenticate(@RequestBody @Valid authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest))
    }
}