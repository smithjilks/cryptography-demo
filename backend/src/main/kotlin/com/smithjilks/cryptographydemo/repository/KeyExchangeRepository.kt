package com.smithjilks.cryptographydemo.repository

import com.smithjilks.cryptographydemo.entity.Key
import com.smithjilks.cryptographydemo.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface KeyExchangeRepository : CrudRepository<Key, Int> {
    fun findKeyByUserId(userId: Int): Optional<Key>

}