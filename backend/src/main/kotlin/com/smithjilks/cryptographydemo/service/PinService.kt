package com.smithjilks.cryptographydemo.service

import com.smithjilks.cryptographydemo.dto.request.KeyExchangeRequest
import com.smithjilks.cryptographydemo.dto.request.PinRequest
import com.smithjilks.cryptographydemo.dto.response.KeyExchangeResponse
import com.smithjilks.cryptographydemo.dto.response.PinResponse
import com.smithjilks.cryptographydemo.dto.response.SetPinResponse
import com.smithjilks.cryptographydemo.entity.Key
import com.smithjilks.cryptographydemo.entity.Pin
import com.smithjilks.cryptographydemo.entity.User
import com.smithjilks.cryptographydemo.exception.InvalidPinException
import com.smithjilks.cryptographydemo.exception.KeysNotFoundException
import com.smithjilks.cryptographydemo.exception.PinNotSetException
import com.smithjilks.cryptographydemo.exception.UserNotFoundException
import com.smithjilks.cryptographydemo.repository.KeyExchangeRepository
import com.smithjilks.cryptographydemo.repository.PinRepository
import com.smithjilks.cryptographydemo.repository.UserRepository
import mu.KLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PinService(
    private val cryptographyService: CryptographyService,
    private val keyExchangeRepository: KeyExchangeRepository,
    private val pinRepository: PinRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    companion object : KLogging()

    fun validatePin(pinRequest: PinRequest): PinResponse {
        val user = SecurityContextHolder.getContext().authentication?.principal as User
        val existingUser = userRepository.findByEmail(user.email).orElseThrow {
            UserNotFoundException("User with email: ${user.email} does not exist")
        }

        if (existingUser.pin == null) {
            throw PinNotSetException("Pin not set for user : ${user.email}")
        }

        val existingKey = keyExchangeRepository.findKeyByUserId(existingUser.id!!)
        val existingPin = pinRepository.findPinByUserId(existingUser.id!!)

        if (!existingKey.isPresent) {
            throw KeysNotFoundException("Keys for user : ${user.email} do not exist")
        }

        val decryptedPin = cryptographyService.decryptUsingRsa(pinRequest.pin, existingKey.get().serverPrivateKey)

        return if (passwordEncoder.matches(decryptedPin, existingUser.pin!!.pin)) {
            logger.info("Pin validated....")
            PinResponse(validated = true)
        } else {
            throw InvalidPinException("Invalid pin supplied")
        }
    }


    fun setPin(pinRequest: PinRequest): SetPinResponse {
        //ToDo("Decouple set/reset")

        val user = SecurityContextHolder.getContext().authentication?.principal as User
        val existingUser = userRepository.findByEmail(user.email).orElseThrow {
            UserNotFoundException("User with email: ${user.email} does not exist")
        }

        val existingKey = keyExchangeRepository.findKeyByUserId(existingUser.id!!)
        val existingPin = pinRepository.findPinByUserId(existingUser.id)

        if (!existingKey.isPresent) {
            throw KeysNotFoundException("Keys for user : ${user.email} do not exist")
        }

        val decryptedPin = cryptographyService.decryptUsingRsa(pinRequest.pin, existingKey.get().serverPrivateKey)
        val pin: Pin

        if (existingUser.pin != null) {
            existingPin.get().let {
                it.pin = passwordEncoder.encode(decryptedPin)
                pin = pinRepository.save(it)
            }
        } else {
            pin = pinRepository.save(
                Pin(
                    id = null,
                    pin = passwordEncoder.encode(decryptedPin),
                    user = existingUser
                )
            )
        }

        existingUser.pin = pin
        userRepository.save(existingUser)

        return SetPinResponse(
            success = true,
            statusCode = 200,
            message = "Pin set successfully"
        )
    }
}