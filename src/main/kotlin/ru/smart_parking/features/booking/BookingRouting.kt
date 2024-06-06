package ru.smart_parking.features.booking

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureBookingRoutes() {
    routing {
        post("/add-booking") {
            val bookingController = BookingController(call)
            bookingController.addBooking()
        }

        get("/booking") {
            val login = call.request.queryParameters["login"]
            if (login != null) {
                val bookingController = BookingController(call)
                bookingController.getAllBooking(login)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login parameter is missing")
            }
        }

        get("/available-slots") {
            val parkingId = call.request.queryParameters["parkingId"]
            val date = call.request.queryParameters["date"]
            if (parkingId != null && date != null) {
                val bookingController = BookingController(call)
                bookingController.getAvailableTimeSlots(parkingId, date)
            } else {
                call.respond(HttpStatusCode.BadRequest, "ParkingId or date parameter is missing")
            }
        }
    }
}