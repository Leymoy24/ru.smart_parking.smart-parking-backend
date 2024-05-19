package ru.smart_parking.features.car

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.smart_parking.database.cars.CarDTO
import ru.smart_parking.database.cars.Cars
import ru.smart_parking.utils.generateUUID

class CarController(private val call: ApplicationCall) {
    suspend fun addNewCar() {
        val carReceiveRemote = call.receive<CarReceiveRemote>()

        if (Cars.carExists(carReceiveRemote.number)) {
            call.respond(HttpStatusCode.Conflict, "Car with the same number already exists")
        } else {
            val carDTO = CarDTO(
                id = generateUUID(),
                userLogin = carReceiveRemote.userLogin,
                number = carReceiveRemote.number,
                model = carReceiveRemote.model
            )
            Cars.insert(carDTO)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getAllCarsByUserLogin(login: String) {
        val cars = Cars.fetchAllCarsByUserLogin(login)
        call.respond(cars.map { car ->
                CarResponseRemote(
                    number = car.number,
                    model = car.model
                )
            }
        )
    }

    suspend fun deleteCar(login: String, number: String) {
        if (Cars.carExists(number)) {
            Cars.deleteCar(login, number)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound, "Car with the given number not found")
        }
    }
}