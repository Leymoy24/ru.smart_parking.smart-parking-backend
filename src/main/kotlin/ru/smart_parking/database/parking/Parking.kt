package ru.smart_parking.database.parking

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Parking : Table("parking") {
    internal val id = Parking.varchar("id", 50)
    internal val name = Parking.varchar("name", 100)
    internal val image = Parking.varchar("image", 250)
    internal val address = Parking.varchar("address", 100)
    internal val location = Parking.varchar("location", 40)
    internal val description = Parking.varchar("description", 200)
    internal val totalPlaces = Parking.integer("total_places")
    internal val availablePlaces = Parking.integer("available_places")
    internal val costPerHour = Parking.integer("cost_per_hour")
    internal val chargingStation = Parking.bool("charging_station")

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

    fun parkingExists(parkingId: String): Boolean {
        return transaction {
            Parking.select { Parking.id.eq(parkingId) }.count() > 0
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