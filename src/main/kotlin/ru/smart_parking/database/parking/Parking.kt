package ru.smart_parking.database.parking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Parking : Table("parking") {
    private val id = Parking.varchar("id", 50)
    private val name = Parking.varchar("name", 100)
    private val image = Parking.varchar("image", 250)
    private val address = Parking.varchar("address", 100)
    private val location = Parking.varchar("location", 40)
    private val description = Parking.varchar("description", 200)
    private val totalPlaces = Parking.integer("total_places")
    private val availablePlaces = Parking.integer("available_places")
    private val costPerHour = Parking.integer("cost_per_hour")
    private val chargingStation = Parking.bool("charging_station")

    fun insert(parkingDTO: ParkingDTO) {
        transaction {
            Parking.insert {
                it[id] = parkingDTO.id
                it[name] = parkingDTO.name
                it[image] = parkingDTO.image
                it[address] = parkingDTO.address
                it[location] = parkingDTO.location
                it[description] = parkingDTO.description
                it[totalPlaces] = parkingDTO.totalPlaces
                it[availablePlaces] = parkingDTO.availablePlaces
                it[costPerHour] = parkingDTO.costPerHour
                it[chargingStation] = parkingDTO.chargingStation
            }
        }
    }

    fun fetchParking(id: String): ParkingDTO? {
        return try {
            transaction {
                val parkingModel = Parking.select { Parking.id.eq(id) }.single()
                ParkingDTO(
                    id = parkingModel[Parking.id],
                    name = parkingModel[name],
                    image = parkingModel[image],
                    address = parkingModel[address],
                    location = parkingModel[location],
                    description = parkingModel[description],
                    totalPlaces = parkingModel[totalPlaces],
                    availablePlaces = parkingModel[availablePlaces],
                    costPerHour = parkingModel[costPerHour],
                    chargingStation = parkingModel[chargingStation]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchAllParking(): List<ParkingDTO> {
        return try {
            transaction {
                Parking.selectAll().map { parkingRow ->
                    ParkingDTO(
                        id = parkingRow[Parking.id],
                        name = parkingRow[name],
                        image = parkingRow[image],
                        address = parkingRow[address],
                        location = parkingRow[location],
                        description = parkingRow[description],
                        totalPlaces = parkingRow[totalPlaces],
                        availablePlaces = parkingRow[availablePlaces],
                        costPerHour = parkingRow[costPerHour],
                        chargingStation = parkingRow[chargingStation]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun deleteParking(id: String): Boolean = try {
        transaction {
            Parking.deleteWhere { Parking.id.eq(id) } > 0
        }
    } catch (e: Exception) {
        false
    }
}