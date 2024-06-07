package ru.smart_parking.features.booking

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseRemote(
    val id: String,
    val userLogin: String,
    val parkingId: String,
    val carNumber: String,
    val checkIn: String,
    val exit: String,
    val amount: Int,
    val paymentStatus: Boolean,
    val numberOfPlace: Int,
    val parkingName: String
)

@Serializable
data class BookingReceiveRemote(
    val userLogin: String,
    val parkingId: String,
    val parkingName: String,
    val carNumber: String,
    val checkIn: String,
    val exit: String,
    val amount: Int
)