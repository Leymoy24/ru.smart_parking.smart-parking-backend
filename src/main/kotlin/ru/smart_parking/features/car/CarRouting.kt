package ru.smart_parking.features.car

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCarRouting() {
    routing {
        get("/cars") {
            val login = call.request.queryParameters["login"]
            if (login != null) {
                val carController = CarController(call)
                carController.getAllCarsByUserLogin(login)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login parameter is missing")
            }
        }

        post("/add-car") {
            val carController = CarController(call)
            carController.addNewCar()
        }

        delete("/delete-car") {
            val login = call.request.queryParameters["login"]
            val number = call.request.queryParameters["number"]

            if (login != null && number != null) {
                val carController = CarController(call)
                carController.deleteCar(login, number)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login or number parameter is missing")
            }
        }
    }
}