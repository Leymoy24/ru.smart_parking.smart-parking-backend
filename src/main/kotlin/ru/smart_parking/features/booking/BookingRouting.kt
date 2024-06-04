package ru.smart_parking.features.booking

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureBookingRoutes() {
    routing {
        post("/add-booking") {
            val bookingController = BookingController(call)
            bookingController.addBooking()
        }
    }
}