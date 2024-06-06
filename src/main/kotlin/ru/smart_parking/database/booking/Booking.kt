package ru.smart_parking.database.booking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.smart_parking.database.parking.Parking
import ru.smart_parking.features.booking.BookingReceiveRemote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Booking : Table("booking") {
    private val id = Booking.varchar("id", 50)
    private val userLogin = Booking.varchar("user_login", 30)
    private val parkingId = Booking.varchar("parking_id", 50)
    private val carNumber = Booking.varchar("car_number", 50)
    private val checkIn = Booking.varchar("check_in", 16)
    private val exit = Booking.varchar("exit", 16)
    private val amount = Booking.integer("amount")
    private val paymentStatus = Booking.bool("payment_status")
    private val numberOfPlace = Booking.integer("number_of_place")

    fun insert(bookingDTO: BookingDTO) {
        transaction {
            Booking.insert {
                it[id] = bookingDTO.id
                it[userLogin] = bookingDTO.userLogin
                it[parkingId] = bookingDTO.parkingId
                it[carNumber] = bookingDTO.carNumber
                it[checkIn] = bookingDTO.checkIn
                it[exit] = bookingDTO.exit
                it[amount] = bookingDTO.amount
                it[paymentStatus] = bookingDTO.paymentStatus
                it[numberOfPlace] = bookingDTO.numberOfPlace
            }
        }
    }

    fun existingOverlappingBooking(booking: BookingReceiveRemote): Boolean {
        return transaction {
            val parkingAvailablePlaces = Parking.slice(Parking.availablePlaces)
                .select { Parking.id eq booking.parkingId }
                .singleOrNull()?.get(Parking.availablePlaces) ?: 0

            val overlappingBookingsCount = Booking.select {
                (checkIn.lessEq(booking.checkIn) and exit.greater(booking.checkIn)) or
                        (checkIn.less(booking.exit) and exit.greaterEq(booking.exit)) or
                        (checkIn.greaterEq(booking.checkIn) and exit.lessEq(booking.exit))
            }.count()

            val existingOverlap = overlappingBookingsCount >= parkingAvailablePlaces

            existingOverlap
        }
    }

    fun fetchBooking(userId: String): List<BookingDTO> {
        return transaction {
            Booking.selectAll().where { userLogin eq userLogin }
                .map { bookingRow ->
                    BookingDTO(
                        id = bookingRow[Booking.id],
                        userLogin = bookingRow[userLogin],
                        parkingId = bookingRow[parkingId],
                        carNumber = bookingRow[carNumber],
                        checkIn = bookingRow[checkIn],
                        exit = bookingRow[exit],
                        amount = bookingRow[amount],
                        paymentStatus = bookingRow[paymentStatus],
                        numberOfPlace = bookingRow[numberOfPlace]
                    )
                }
        }
    }

    fun getAvailableTimeSlots(parkingId: String, date: String): List<Pair<String, String>> {
        return transaction {
            val parkingCapacity = Parking.slice(Parking.availablePlaces)
                .select { Parking.id eq parkingId }
                .singleOrNull()?.get(Parking.availablePlaces) ?: 0

            val bookedSlots = Booking.select { Booking.parkingId eq parkingId }
                .map { bookingRow ->
                    val checkIn = bookingRow[Booking.checkIn]
                    val exit = bookingRow[Booking.exit]
                    if (checkIn.startsWith(date) || exit.startsWith(date)) {
                        checkIn to exit
                    } else {
                        null
                    }
                }
                .filterNotNull()

            val opened = Parking.slice(Parking.opened)
                .select { Parking.id eq parkingId }
                .singleOrNull()?.get(Parking.opened) ?: "00:00"
            val closed = Parking.slice(Parking.closed)
                .select { Parking.id eq parkingId }
                .singleOrNull()?.get(Parking.closed) ?: "23:59"

            val startTime = LocalDateTime.parse("$date $opened", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            val endTime = LocalDateTime.parse("$date $closed", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

            val hourlySlotCount = mutableMapOf<LocalDateTime, Int>()

            // Подсчет количества пересечений для каждого часового интервала
            for ((start, end) in bookedSlots) {
                val startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                val endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

                var currentTime = startDateTime
                while (currentTime < endDateTime) {
                    hourlySlotCount[currentTime] = hourlySlotCount.getOrDefault(currentTime, 0) + 1
                    currentTime = currentTime.plusHours(1)
                }
            }

            val availableSlots = mutableListOf<Pair<String, String>>()
            var currentTime = startTime

            // Формирование списка доступных промежутков
            while (currentTime < endTime) {
                val nextTime = currentTime.plusHours(1)
                val count = hourlySlotCount.getOrDefault(currentTime, 0)
                if (count < parkingCapacity) {
                    val start = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    val end = nextTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    availableSlots.add(start to end)
                }
                currentTime = nextTime
            }

            availableSlots
        }
    }

}