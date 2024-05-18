package ru.smart_parking.features.parking

import kotlinx.serialization.Serializable

@Serializable
data class ParkingReceiveRemote(
    val id: String,
    val name: String,
    val image: String,
    val address: String,
    val location: String,
    val description: String,
    val totalPlaces: Int,
    val availablePlaces: Int,
    val costPerHour: Int,
    val chargingStation: Boolean
)