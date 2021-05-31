package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
