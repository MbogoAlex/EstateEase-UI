package com.example.tenant_care.datastore

data class UserDSDetails(
    val roleId: Int?,
    val userId: Int?,
    val fullName: String,
    val email: String,
    val userAddedAt: String,
    val phoneNumber: String,
)