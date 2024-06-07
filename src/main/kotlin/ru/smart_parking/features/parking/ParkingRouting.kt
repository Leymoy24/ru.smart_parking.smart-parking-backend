package ru.smart_parking.features.parking

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureParkingRouting() {
    routing {
        get("/all-parking") {
            val parkingController = ParkingController(call)
            parkingController.getAllParking()
        }

        get("/parking") {
            val parkingId = call.request.queryParameters["parkingId"]
            if (parkingId != null) {
                val parkingController = ParkingController(call)
                parkingController.getParking(parkingId)
            } else {
                call.respond(HttpStatusCode.BadRequest, "PakingId is invalid")
            }
        }
    }
}