package com.tekkr.data.dataSources.definitions

import com.tekkr.data.models.ContactDetails
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderBody
import com.tekkr.data.models.User
import com.tekkr.data.roomDatabase.Address
import org.json.JSONObject

abstract class DataSourceSharedPreferences {

    abstract fun saveLoggedInUser(user: User)
    abstract fun getLoggedInUser(): User?
    abstract fun clearLoggedInUser()

    abstract fun saveAddress(address: Address)
    abstract fun getAddress(): Address?
    abstract fun clearAddress()

    abstract fun saveContactDetails(contactDetails: ContactDetails)
    abstract fun getContactDetails(): ContactDetails?
    abstract fun clearContactDetails()

    abstract fun saveTempAddress(address: Address)
    abstract fun getTempAddress(): Address?
    abstract fun clearTempAddress()

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


    abstract fun saveOrderBody(body: OrderBody)
    abstract fun clearOrderBody()
    abstract fun getOrderBody(): OrderBody?


    abstract fun deleteAllPrefs()
}