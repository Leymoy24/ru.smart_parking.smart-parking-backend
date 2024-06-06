package ru.smart_parking.features.favourite

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.smart_parking.database.favourite.Favourite
import ru.smart_parking.database.parking.Parking
import ru.smart_parking.features.parking.ParkingReceiveRemote

class FavouriteParkingController(private val call: ApplicationCall) {
    suspend fun addFavouriteParkingForUser(userLogin: String, parkingId: String) {
        if (!Parking.parkingExists(parkingId)) {
            call.respond(HttpStatusCode.NotFound, "Parking not found")
            return
        }

        if (Favourite.parkingExistsInFavourites(userLogin, parkingId)) {
            call.respond(HttpStatusCode.Conflict, "Parking is already in favourites")
        } else {
            Favourite.addFavouriteParkingForUser(userLogin, parkingId)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getFavouriteParkingForUser(userLogin: String) {
        val favouriteParking = Favourite.getFavouriteParkingForUser(userLogin)
        call.respond(favouriteParking.map { parking ->
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