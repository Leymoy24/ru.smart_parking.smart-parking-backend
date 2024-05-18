package ru.smart_parking.features.favourite

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureFavouriteRouting() {
    routing {
        post("/users/{userLogin}/favouriteParking/{parkingId}") {
            val userLogin = call.parameters["userLogin"] ?: return@post
            val parkingId = call.parameters["parkingId"] ?: return@post
            val favouriteParkingController = FavouriteParkingController(call)
            favouriteParkingController.addFavouriteParkingForUser(userLogin, parkingId)
        }

        get("/users/{userLogin}/favouriteParking") {
            val userLogin = call.parameters["userLogin"] ?: return@get
            val favouriteParkingController = FavouriteParkingController(call)
            call.respond(favouriteParkingController.getFavouriteParkingForUser(userLogin))
        }
    }
}