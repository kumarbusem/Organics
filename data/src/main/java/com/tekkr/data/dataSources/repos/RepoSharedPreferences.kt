package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplSharedPreferences
import com.tekkr.data.models.ContactDetails
import com.tekkr.data.models.Order
import com.tekkr.data.models.OrderBody
import com.tekkr.data.models.User
import com.tekkr.data.roomDatabase.Address
import org.json.JSONObject

class RepoSharedPreferences : DataSourceSharedPreferences() {

    private val mSpDS: DataSourceSharedPreferences by lazy { DataSourceImplSharedPreferences() }

    override fun saveLoggedInUser(user: User) = mSpDS.saveLoggedInUser(user)
    override fun getLoggedInUser(): User? = mSpDS.getLoggedInUser()
    override fun clearLoggedInUser() = mSpDS.clearLoggedInUser()

    override fun saveAddress(address: Address) =  mSpDS.saveAddress(address)
    override fun getAddress(): Address? = mSpDS.getAddress()
    override fun clearAddress() = mSpDS.clearAddress()

    override fun saveContactDetails(contactDetails: ContactDetails) =  mSpDS.saveContactDetails(contactDetails)
    override fun getContactDetails(): ContactDetails? = mSpDS.getContactDetails()
    override fun clearContactDetails() = mSpDS.clearContactDetails()
    
    override fun saveTempAddress(address: Address) =  mSpDS.saveTempAddress(address)
    override fun getTempAddress(): Address? = mSpDS.getTempAddress()
    override fun clearTempAddress() = mSpDS.clearTempAddress()

    override fun saveSelectedRunsheetId(runsheetId: String) = mSpDS.saveSelectedRunsheetId(runsheetId)
    override fun clearSelectedRunsheetId() = mSpDS.clearSelectedRunsheetId()
    override fun getSelectedRunsheetId(): String? = mSpDS.getSelectedRunsheetId()

    override fun saveSelectedPickupId(pickupId: String) = mSpDS.saveSelectedPickupId(pickupId)
    override fun clearSelectedPickupId() = mSpDS.clearSelectedPickupId()
    override fun getSelectedPickupId(): String? = mSpDS.getSelectedPickupId()

    override fun isProfilePicUpdated(updated: Boolean) = mSpDS.isProfilePicUpdated(updated)
    override fun isProfilePicUpdated(): Boolean = mSpDS.isProfilePicUpdated()

    override fun saveSelectedOrder(order: Order) = mSpDS.saveSelectedOrder(order)
    override fun getSelectedOrder(): Order? = mSpDS.getSelectedOrder()
    override fun clearSelectedOrder() = mSpDS.clearSelectedOrder()

    override fun saveOrderBody(body: OrderBody) = mSpDS.saveOrderBody(body)
    override fun getOrderBody(): OrderBody? = mSpDS.getOrderBody()
    override fun clearOrderBody() = mSpDS.clearOrderBody()

    override fun deleteAllPrefs() = mSpDS.deleteAllPrefs()
}