package ru.smart_parking.database.cars

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Cars: Table("cars") {
    private val id = Cars.varchar("id", 50)
    private val userLogin = Cars.varchar("user_login", 30)
    private val number = Cars.varchar("number", 20)
    private val model = Cars.varchar("model", 30)

    fun insert(carDTO: CarDTO) {
        transaction {
            Cars.insert {
                it[id] = carDTO.id
                it[userLogin] = carDTO.userLogin
                it[number] = carDTO.number
                it[model] = carDTO.model
            }
        }
    }

    fun carExists(number: String): Boolean {
        return transaction {
            Cars.select { Cars.number.eq(number) }.count() > 0
        }
    }

    fun fetchAllCarsByUserLogin(userLogin: String): List<CarDTO> {
        return try {
            transaction {
                Cars.selectAll().where { Cars.userLogin.eq(userLogin) }.map { carsRow ->
                    CarDTO(
                        id = carsRow[Cars.id],
                        userLogin = carsRow[Cars.userLogin],
                        number = carsRow[number],
                        model = carsRow[model]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchCar(id: String): CarDTO? {
        return try {
            transaction {
                val carModel = Cars.select { Cars.id.eq(id) }.single()
                CarDTO(
                    id = carModel[Cars.id],
                    userLogin = carModel[userLogin],
                    number = carModel[number],
                    model = carModel[model]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAllCars(): List<CarDTO> {
        return try {
            transaction {
                Cars.selectAll().map { carsRow ->
                    CarDTO(
                        id = carsRow[Cars.id],
                        userLogin = carsRow[userLogin],
                        number = carsRow[number],
                        model = carsRow[model]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}