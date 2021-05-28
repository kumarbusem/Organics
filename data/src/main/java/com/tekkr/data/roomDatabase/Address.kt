package com.tekkr.data.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
class Address(

        @PrimaryKey
        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "address")
        var address: String,

        @ColumnInfo(name = "city")
        val city: String = "Hyderabad",

        @ColumnInfo(name = "state")
        val state: String = "Telangana",

        @ColumnInfo(name = "latitude")
        val latitude: Double,

        @ColumnInfo(name = "longitude")
        val longitude: Double,

        @ColumnInfo(name = "pin")
        var pin: String = "500001"

)
