package ru.smart_parking.features.car

import kotlinx.serialization.Serializable

@Serializable
data class CarReceiveRemote(
    val userLogin: String,
    val number: String,
    val model: String
)

@Serializable
data class CarResponseRemote(
    val number: String,
    val model: String
)