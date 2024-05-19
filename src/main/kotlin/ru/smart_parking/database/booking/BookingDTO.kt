package ru.smart_parking.database.booking

class BookingDTO(
    val id: String,
    val userLogin: String,
    val parkingId: String,
    val carId: String,
    val checkIn: String,
    val exit: String,
    val amount: Int,
    val paymentStatus: Boolean,
    val numberOfPlace: Int
)