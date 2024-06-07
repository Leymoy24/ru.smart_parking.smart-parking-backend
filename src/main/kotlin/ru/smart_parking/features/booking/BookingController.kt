package ru.smart_parking.features.booking

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.smart_parking.database.booking.Booking
import ru.smart_parking.database.booking.BookingDTO
import ru.smart_parking.database.parking.Parking
import ru.smart_parking.utils.generateUUID

class BookingController(private val call: ApplicationCall) {
    suspend fun addBooking() {
        val bookingReceiveRemote = call.receive<BookingReceiveRemote>()
        val existingOverlappingBooking = Booking.existingOverlappingBooking(bookingReceiveRemote)
        if (existingOverlappingBooking) {
            call.respond(HttpStatusCode.Conflict, "The time for booking is not available")
        } else {
            Booking.insert(
                BookingDTO(
                    id = generateUUID(),
                    userLogin = bookingReceiveRemote.userLogin,
                    parkingId = bookingReceiveRemote.parkingId,
                    carNumber = bookingReceiveRemote.carNumber,
                    checkIn = bookingReceiveRemote.checkIn,
                    exit = bookingReceiveRemote.exit,
                    amount = bookingReceiveRemote.amount,
                    paymentStatus = false,
                    numberOfPlace = 1,
                    parkingName = bookingReceiveRemote.parkingName
                )
            )
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun deleteBooking(bookingId: String) {
        val booking = Booking.deleteBooking(bookingId)
        if (booking) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.BadRequest, "Booking not found")
        }
    }

    suspend fun getAllBooking(login: String) {
        val booking = Booking.fetchBooking(login)
        call.respond(booking.map { booking ->
            BookingResponseRemote(
                id = booking.id,
                userLogin = booking.userLogin,
                parkingId = booking.parkingId,
                carNumber = booking.carNumber,
                checkIn = booking.checkIn,
                exit = booking.exit,
                amount = booking.amount,
                paymentStatus = booking.paymentStatus,
                numberOfPlace = booking.numberOfPlace,
                parkingName = booking.parkingName
            )
        })
    }

    suspend fun getAvailableTimeSlots(parkingId: String, date: String) {
        val availableTimeSlots = Booking.getAvailableTimeSlots(parkingId, date)
        call.respond(availableTimeSlots)
    }
}