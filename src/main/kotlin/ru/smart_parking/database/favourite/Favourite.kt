package ru.smart_parking.database.favourite

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.smart_parking.database.parking.Parking
import ru.smart_parking.database.parking.ParkingDTO
import ru.smart_parking.database.users.Users

object Favourite : Table("favourite_parking") {
    private val login = Favourite.varchar("login", 30).references(Users.login)
    private val parkingId = Favourite.varchar("parking_id", 50).references(Parking.id)

    fun addFavouriteParkingForUser(userLogin: String, parkingId: String) {
        transaction {
            Favourite.insert {
                it[login] = userLogin
                it[this.parkingId] = parkingId
            }
        }
    }

    fun parkingExistsInFavourites(userLogin: String, parkingId: String): Boolean {
        return transaction {
            Favourite.select { Favourite.login.eq(userLogin) and (Favourite.parkingId.eq(parkingId)) }.count() > 0
        }
    }

    fun getFavouriteParkingForUser(userLogin: String): List<ParkingDTO> {
        return transaction {
            (Favourite innerJoin Parking)
                .slice(Parking.columns)
                .select { Favourite.login.eq(userLogin) }
                .map { rowData ->
                    ParkingDTO(
                        id = rowData[Parking.id],
                        name = rowData[Parking.name],
                        image = rowData[Parking.image],
                        address = rowData[Parking.address],
                        location = rowData[Parking.location],
                        description = rowData[Parking.description],
                        totalPlaces = rowData[Parking.totalPlaces],
                        availablePlaces = rowData[Parking.availablePlaces],
                        costPerHour = rowData[Parking.costPerHour],
                        chargingStation = rowData[Parking.chargingStation],
                        opened = rowData[Parking.opened],
                        closed = rowData[Parking.closed],
                    )
                }
        }
    }

}