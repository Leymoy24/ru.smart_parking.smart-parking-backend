package ru.smart_parking.features.parking

import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.smart_parking.database.parking.Parking

class ParkingController(private val call: ApplicationCall) {
    suspend fun getAllParking() {
        val parking = Parking.fetchAllParking()
        call.respond(parking.map { parking ->
            ParkingReceiveRemote(
                id = parking.id,
                name = parking.name,
                image = parking.image,
                address = parking.address,
                location = parking.location,
                description = parking.description,
                totalPlaces = parking.totalPlaces,
                availablePlaces = parking.availablePlaces,
                costPerHour = parking.costPerHour,
                chargingStation = parking.chargingStation,
                opened = parking.opened,
                closed = parking.closed,
            )
        })
    }
}