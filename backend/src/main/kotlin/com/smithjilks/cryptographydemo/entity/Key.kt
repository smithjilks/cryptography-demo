package com.smithjilks.cryptographydemo.entity

import jakarta.persistence.*

@Entity
@Table(name = "KEYS")
data class Key(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    @Column(columnDefinition = "TEXT")
    var uiPublicKey: String,
    @Column(columnDefinition = "TEXT")
    var serverPublicKey: String,
    @Column(columnDefinition = "TEXT")
    var serverPrivateKey: String,
    @Column(columnDefinition = "TEXT")
    var serverSigningSecret: String,
    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User
)