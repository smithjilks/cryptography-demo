package com.smithjilks.cryptographydemo.entity

import jakarta.persistence.*

@Entity
@Table(name = "PINS")
data class Pin(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var pin: String,
    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User
)