package ru.smart_parking.features.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseRemote(
    val login: String,
    val password: String,
    val username: String,
    val email: String?
)