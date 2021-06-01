package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject

@Entity(tableName = "address_table")
data class Address(

        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "phone_number")
        var phone_number: String  = "",

        @ColumnInfo(name = "line1")
        var line1: String = "",

        @ColumnInfo(name = "line2")
        var line2: String = "",

        @ColumnInfo(name = "city")
        val city: String = "Hyderabad",

        @ColumnInfo(name = "state")
        val state: String = "Telangana",

        @ColumnInfo(name = "latitude")
        val latitude: Double = 0.0,

        @ColumnInfo(name = "longitude")
        val longitude: Double = 0.0,

        @ColumnInfo(name = "pincode")
        var pincode: String = "500001"

)


fun Address.toJsonObject(): JsonObject {

        val addressObject = JsonObject()
        addressObject.addProperty("name", this.name)
        addressObject.addProperty("phone_number", this.phone_number)
        addressObject.addProperty("line1", this.line1)
        addressObject.addProperty("line2", this.line2)
        addressObject.addProperty("city", this.city)
        addressObject.addProperty("state", this.state)
        addressObject.addProperty("latitude", this.latitude)
        addressObject.addProperty("longitude", this.longitude)
        addressObject.addProperty("pincode", this.pincode)
        return addressObject

}
