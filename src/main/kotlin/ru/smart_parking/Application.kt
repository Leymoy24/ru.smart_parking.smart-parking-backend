package ru.smart_parking

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import ru.smart_parking.features.login.configureLoginRouting
import ru.smart_parking.features.register.configureRegisterRouting
import ru.smart_parking.plugins.*

fun main() {
    Database.connect(
        url = System.getenv("DATABASE_SMARTPARKING_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("POSTGRES_USER"),
        password = System.getenv("POSTGRES_PASSWORD")
    )

    embeddedServer(
        CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
}
