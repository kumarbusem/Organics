package com.tekkr.data.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(BigItem::class), version = 1, exportSchema = false)
public abstract class TekkrRoomDatabase : RoomDatabase() {

    abstract fun recentAddressDao(): ItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TekkrRoomDatabase? = null

        fun getDatabase(context: Context): TekkrRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TekkrRoomDatabase::class.java,
                        "uni_room_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
