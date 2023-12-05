package com.smithjilks.cryptographydemo.service

import com.smithjilks.cryptographydemo.dto.request.KeyExchangeRequest
import com.smithjilks.cryptographydemo.dto.response.KeyExchangeResponse
import com.smithjilks.cryptographydemo.entity.Key
import com.smithjilks.cryptographydemo.entity.KeyPair
import com.smithjilks.cryptographydemo.entity.User
import com.smithjilks.cryptographydemo.exception.UserNotFoundException
import com.smithjilks.cryptographydemo.repository.KeyExchangeRepository
import com.smithjilks.cryptographydemo.repository.UserRepository
import mu.KLogging
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class KeyExchangeService(
    private val cryptographyService: CryptographyService,
    private val keyExchangeRepository: KeyExchangeRepository,
    private val userRepository: UserRepository
) {
    companion object : KLogging()

    fun exchangeKeys(keyExchangeRequest: KeyExchangeRequest): KeyExchangeResponse {
        val user = SecurityContextHolder.getContext().authentication?.principal as User
        val existingUser = userRepository.findByEmail(user.email).orElseThrow {
            UserNotFoundException("User with email: ${user.email} does not exist")
        }

        val existingKey = keyExchangeRepository.findKeyByUserId(existingUser.id!!)

        val keys = cryptographyService.generateRSAKeyPair()
        val serverSigningKey =
            keys?.let {
                cryptographyService.generateServerSigningKey(keyExchangeRequest.uiPublicKey)
            }

        return if (keys != null && !serverSigningKey.isNullOrEmpty()) {
            val key: Key

            if (existingUser.key != null) {
                existingUser.key!!.let {
                    it.uiPublicKey = keyExchangeRequest.uiPublicKey
                    it.serverPublicKey = keys.publicKey
                    it.serverPrivateKey = keys.privateKey
                    it.serverSigningSecret = serverSigningKey
                    key = keyExchangeRepository.save(it)
                    logger.info("Updated existing key....")
                }
            } else {
                    key  = keyExchangeRepository.save(
                        Key(
                            id =  null,
                            uiPublicKey = keyExchangeRequest.uiPublicKey,
                            serverPublicKey = keys.publicKey,
                            serverPrivateKey = keys.privateKey,
                            serverSigningSecret = serverSigningKey,
                            user = existingUser
                        )
                    )
            }

            existingUser.key = key
            userRepository.save(existingUser)
            logger.info("Updated user: ${existingUser.email}")

            KeyExchangeResponse(
                serverPublicKey = keys.publicKey,
                serverSigningKey = serverSigningKey
            )
        } else {
            throw Exception("Unable to generate keys")
        }
    }
}