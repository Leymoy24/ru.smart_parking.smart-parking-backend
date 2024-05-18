package ru.smart_parking.features.parking

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureParkingRouting() {
    routing {
        get("/parking") {
            val parkingController = ParkingController(call)
            parkingController.getAllParking()
        }
    }
}