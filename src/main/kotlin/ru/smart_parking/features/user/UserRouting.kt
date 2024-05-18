package ru.smart_parking.features.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserRouting() {
    routing {
        get("/user") {
            val login = call.request.queryParameters["login"]
            if (login != null) {
                val userController = UserController(call)
                userController.getUserByLogin(login)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Login parameter is missing")
            }
        }
    }
}