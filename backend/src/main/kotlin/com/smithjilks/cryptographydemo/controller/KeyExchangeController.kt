package com.smithjilks.cryptographydemo.controller

import com.smithjilks.cryptographydemo.dto.request.KeyExchangeRequest
import com.smithjilks.cryptographydemo.dto.response.KeyExchangeResponse
import com.smithjilks.cryptographydemo.entity.User
import com.smithjilks.cryptographydemo.service.KeyExchangeService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/keys")
@Validated
class KeyExchangeController(
    private val keyExchangeService: KeyExchangeService
) {

    @PostMapping("/exchange")
    @ResponseStatus(HttpStatus.OK)
    fun performKeyExchange(@RequestBody @Valid keyExchangeRequest: KeyExchangeRequest, request: HttpServletRequest): ResponseEntity<KeyExchangeResponse> {
        return ResponseEntity.ok(keyExchangeService.exchangeKeys(keyExchangeRequest))
    }

}