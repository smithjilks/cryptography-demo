package com.smithjilks.cryptographydemo.repository

import com.smithjilks.cryptographydemo.entity.Key
import com.smithjilks.cryptographydemo.entity.Pin
import com.smithjilks.cryptographydemo.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface PinRepository : CrudRepository<Pin, Int> {
    fun findPinByUserId(userId: Int): Optional<Pin>

}