package com.smithjilks.cryptographydemo.repository

import com.smithjilks.cryptographydemo.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, Int> {
    fun findByEmail(email: String): Optional<User>
}