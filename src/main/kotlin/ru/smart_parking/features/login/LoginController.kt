package ru.smart_parking.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.smart_parking.database.tokens.TokenDTO
import ru.smart_parking.database.tokens.Tokens
import ru.smart_parking.database.users.Users
import ru.smart_parking.utils.generateUUID

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(LoginResponseRemote(token = "User not found"))
//            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == receive.password) {
                val token = generateUUID()
                Tokens.insert(TokenDTO(rowId = generateUUID(), login = receive.login, token = token))
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(LoginResponseRemote(token = "Invalid password"))
//                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}