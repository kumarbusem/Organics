package com.tekkr.data.models


data class Customer(
        val name: String,
        val phone_number: String,
        val id: String = "",
        val username: String = "",
        val email: String = ""
) {
    companion object {
        const val NAME: String = "name"
        const val PHONE_NUMBER: String = "phone_number"
    }
}

