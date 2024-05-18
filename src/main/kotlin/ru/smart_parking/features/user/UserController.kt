package ru.smart_parking.features.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.smart_parking.database.users.Users.fetchUser

class UserController(private val call: ApplicationCall) {
    suspend fun getUserByLogin(login: String) {
        val user = fetchUser(login)
        if (user != null) {
            val userResponseRemote = UserResponseRemote(
                login = user.login,
                password = user.password,
                username = user.username,
                email = user.email
            )
            call.respond(userResponseRemote)
        } else {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}