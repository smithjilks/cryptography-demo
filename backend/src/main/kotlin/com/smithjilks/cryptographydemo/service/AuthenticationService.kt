package com.smithjilks.cryptographydemo.service

import com.smithjilks.cryptographydemo.dto.request.AuthenticationRequest
import com.smithjilks.cryptographydemo.dto.request.RegisterRequest
import com.smithjilks.cryptographydemo.dto.response.AuthenticationResponse
import com.smithjilks.cryptographydemo.entity.Role
import com.smithjilks.cryptographydemo.entity.User
import com.smithjilks.cryptographydemo.exception.UserNotFoundException
import com.smithjilks.cryptographydemo.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(registerRequest: RegisterRequest): AuthenticationResponse {
        val user = User(
            id = null,
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            role = Role.USER
        )
        userRepository.save(user)

        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

    fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )
        val user = userRepository.findByEmail(authenticationRequest.email).orElseThrow {
            UserNotFoundException("User with email: ${authenticationRequest.email} does not exist")
        }
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(token = jwtToken)
    }

}