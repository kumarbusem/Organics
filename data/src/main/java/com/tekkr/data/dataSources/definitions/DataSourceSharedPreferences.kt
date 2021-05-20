package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.Order
import com.tekkr.data.models.User

abstract class DataSourceSharedPreferences {

    abstract fun saveLoggedInUser(user: User)
    abstract fun getLoggedInUser(): User?
    abstract fun clearLoggedInUser()

    abstract fun saveSelectedRunsheetId(runsheetId: String)
    abstract fun clearSelectedRunsheetId()
    abstract fun getSelectedRunsheetId(): String?

    abstract fun saveSelectedPickupId(pickupId: String)
    abstract fun clearSelectedPickupId()
    abstract fun getSelectedPickupId(): String?

    abstract fun isProfilePicUpdated(updated: Boolean)
    abstract fun isProfilePicUpdated(): Boolean

    abstract fun saveSelectedOrder(order: Order)
    abstract fun clearSelectedOrder()
    abstract fun getSelectedOrder(): Order?


    abstract fun deleteAllPrefs()
}