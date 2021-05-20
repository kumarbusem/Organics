package com.tekkr.data.dataSources.repos

import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.internal.dataSourceImpls.DataSourceImplSharedPreferences
import com.tekkr.data.models.Order
import com.tekkr.data.models.User

class RepoSharedPreferences : DataSourceSharedPreferences() {

    private val mSpDS: DataSourceSharedPreferences by lazy { DataSourceImplSharedPreferences() }

    override fun saveLoggedInUser(user: User) = mSpDS.saveLoggedInUser(user)
    override fun getLoggedInUser(): User? = mSpDS.getLoggedInUser()
    override fun clearLoggedInUser() = mSpDS.clearLoggedInUser()

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

    override fun deleteAllPrefs() = mSpDS.deleteAllPrefs()
}