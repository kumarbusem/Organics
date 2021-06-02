package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.RoundingMode

@Entity(tableName = "address_table")
data class Address(

        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "phone_number")
        var phone_number: String = "",

        @ColumnInfo(name = "line1")
        var line1: String = "",

        @ColumnInfo(name = "line2")
        var line2: String = "",

        @ColumnInfo(name = "city")
        val city: String = "Hyderabad",

        @ColumnInfo(name = "state")
        val state: String = "Telangana",

        @ColumnInfo(name = "pincode")
        var pincode: String = "500001",

        @ColumnInfo(name = "landmark")
        var landmark: String = "",

        @ColumnInfo(name = "latitude")
        var latitude: Double = 0.0,

        @ColumnInfo(name = "longitude")
        var longitude: Double = 0.0


)

fun Address.to10DigitAddress(): Address{
        this.latitude = Math.round(this.latitude * 10000000) / 10000000.0
        this.longitude = Math.round(this.longitude * 10000000) / 10000000.0
        return this
}


fun Address.toJsonObject(): JsonObject {
        val address = this.to10DigitAddress()
        val addressObject = JsonObject()
        addressObject.addProperty("name", address.name)
        addressObject.addProperty("phone_number", address.phone_number)
        addressObject.addProperty("line1", address.line1)
        addressObject.addProperty("line2", address.line2)
        addressObject.addProperty("city", address.city)
        addressObject.addProperty("state", address.state)
        addressObject.addProperty("latitude", address.latitude)
        addressObject.addProperty("longitude", address.longitude)
        addressObject.addProperty("pincode", address.pincode)
        return addressObject

}
