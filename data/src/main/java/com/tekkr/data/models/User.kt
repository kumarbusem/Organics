package com.tekkr.data.models

data class User(
        val status: String,
        val id: Int,
        val username: String = "",
        val phone_number: String = "",
        val refresh: String = "",
        var access: String,
        val new_user: String = "",
        val name: String = "",
        val email: String = ""
) {
    companion object {

        const val STATUS: String = "status"
        const val ID: String = "phone_number"
        const val USERNAME: String = "name"
        const val PHONE_NUMBER: String = "rider_id"
        const val REFRESH: String = "alternate_no"
        const val ACCESS: String = "sf_id"
        const val NEW_USER: String = "rider_name"
        const val NAME: String = "token"
        const val EMAIL: String = "is_profile_pic_uploaded"
    }
}