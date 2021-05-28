package com.tekkr.data.internal.dataSourceImpls

import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.internal.common.SharedPreferenceHelper
import com.tekkr.data.models.Order
import com.tekkr.data.models.User
import com.tekkr.data.roomDatabase.Address

class DataSourceImplSharedPreferences : DataSourceSharedPreferences() {

    private val mSpHelper: SharedPreferenceHelper by lazy { SharedPreferenceHelper.getInstance() }

    override fun saveAddress(address: Address) = mSpHelper.putObject(SP_ADDRESS, address)
    override fun getAddress(): Address? = mSpHelper.getObject(SP_ADDRESS)
    override fun clearAddress() = mSpHelper.remove(SP_ADDRESS)

    override fun saveTempAddress(address: Address) = mSpHelper.putObject(SP_TEMP_ADDRESS, address)
    override fun getTempAddress(): Address? = mSpHelper.getObject(SP_TEMP_ADDRESS)
    override fun clearTempAddress() = mSpHelper.remove(SP_TEMP_ADDRESS)

    override fun saveLoggedInUser(user: User) = mSpHelper.putObject(SP_LOGGED_IN_USER, user)
    override fun getLoggedInUser(): User? = mSpHelper.getObject(SP_LOGGED_IN_USER)
    override fun clearLoggedInUser() = mSpHelper.remove(SP_LOGGED_IN_USER)

    override fun saveSelectedRunsheetId(runsheetId: String) = mSpHelper.putString(SP_SELECTED_RUNSHEET_ID, runsheetId)
    override fun clearSelectedRunsheetId() = mSpHelper.remove(SP_SELECTED_RUNSHEET_ID)
    override fun getSelectedRunsheetId(): String? = mSpHelper.getString(SP_SELECTED_RUNSHEET_ID)

    override fun saveSelectedPickupId(pickupId: String) = mSpHelper.putString(SP_SELECTED_PICKUP_ID, pickupId)
    override fun clearSelectedPickupId() = mSpHelper.remove(SP_SELECTED_PICKUP_ID)
    override fun getSelectedPickupId(): String? = mSpHelper.getString(SP_SELECTED_PICKUP_ID)

    override fun isProfilePicUpdated(updated: Boolean) = mSpHelper.putBoolean(SP_IS_PROFILE_PIC_UPDATED, updated)
    override fun isProfilePicUpdated(): Boolean = mSpHelper.getBoolean(SP_IS_PROFILE_PIC_UPDATED)

    override fun saveSelectedOrder(order: Order) = mSpHelper.putObject(SP_SELECTED_ORDER, order)
    override fun clearSelectedOrder() = mSpHelper.remove(SP_SELECTED_ORDER)
    override fun getSelectedOrder(): Order? = mSpHelper.getObject(SP_SELECTED_ORDER)

    override fun deleteAllPrefs() = mSpHelper.clear()

    companion object {

        private const val SP_TEMP_ADDRESS: String = "SP_TEMP_ADDRESS"
        private const val SP_ADDRESS: String = "SP_ADDRESS"
        private const val SP_LOGGED_IN_USER: String = "SP_LOGGED_IN_USER"
        private const val SP_SELECTED_RUNSHEET_ID: String = "SP_SELECTED_RUNSHEET_ID"
        private const val SP_SELECTED_PICKUP_ID: String = "SP_SELECTED_PICKUP_ID"
        private const val SP_SELECTED_ORDER: String = "SP_SELECTED_ORDER"
        private const val SP_IS_PROFILE_PIC_UPDATED: String = "SP_IS_PROFILE_PIC_UPDATED"
    }
}