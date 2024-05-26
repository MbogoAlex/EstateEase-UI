package com.example.tenant_care.model.caretaker

import kotlinx.serialization.Serializable

@Serializable
data class CaretakerLoginRequestBody (
    val phoneNumber: String,
    val password: String
)
@Serializable
data class CaretakerLoginResponseBody (
    val statusCode: Int,
    val message: String,
    val data: CaretakerData
)
@Serializable
data class CaretakerData(
    val caretaker: Caretaker
)
@Serializable
data class Caretaker(
    val caretakerId: Int,
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val caretakerAddedAt: String,
    val active: Boolean,
    val pmanager: PManagerDt
)
@Serializable
data class PManagerDt(
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val pmanagerId: Int
)
