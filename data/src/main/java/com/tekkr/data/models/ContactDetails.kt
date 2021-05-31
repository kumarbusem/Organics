package com.tekkr.data.models


data class ContactDetails(
        val name: String,
        val phone: String
) {
    companion object {
        const val NAME: String = "name"
        const val PHONE: String = "phone"
    }
}

