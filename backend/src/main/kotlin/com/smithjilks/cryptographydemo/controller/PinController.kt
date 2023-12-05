package com.smithjilks.cryptographydemo.controller

import com.smithjilks.cryptographydemo.dto.request.KeyExchangeRequest
import com.smithjilks.cryptographydemo.dto.request.PinRequest
import com.smithjilks.cryptographydemo.dto.request.SetPinRequest
import com.smithjilks.cryptographydemo.dto.response.KeyExchangeResponse
import com.smithjilks.cryptographydemo.dto.response.PinResponse
import com.smithjilks.cryptographydemo.dto.response.SetPinResponse
import com.smithjilks.cryptographydemo.entity.User
import com.smithjilks.cryptographydemo.service.KeyExchangeService
import com.smithjilks.cryptographydemo.service.PinService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/pin")
@Validated
class PinController(
    private val pinService: PinService
) {
    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    fun validatePin(@RequestBody @Valid pinRequest: PinRequest): ResponseEntity<PinResponse> {
        return ResponseEntity.ok(pinService.validatePin(pinRequest))
    }

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.OK)
    fun setPin(@RequestBody @Valid pinRequest: PinRequest): ResponseEntity<SetPinResponse> {
        return ResponseEntity.ok(pinService.setPin(pinRequest))
    }

}