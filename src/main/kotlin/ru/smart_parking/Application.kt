package ru.smart_parking

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import ru.smart_parking.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}
