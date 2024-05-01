package ru.smart_parking.database.parking

class ParkingDTO(
    val id: String,
    val name: String,
    val image: String,
    val address: String,
    val location: String,
    val description: String,
    val totalPlaces: Int,
    val availablePlaces: Int,
    val costPerHour: Int
)