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
@Serializable
data class CaretakersResponseBody(
    val statusCode: Int,
    val message: String,
    val data: CaretakersResponseData
)
@Serializable
data class CaretakersResponseData(
    val caretaker: List<CaretakerDT>
)
@Serializable
data class CaretakerDT(
    val caretakerId: Int,
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val caretakerAddedAt: String,
    val active: Boolean,
    val salary: Double,
    val payments: List<CaretakerPayment>,
    val pmanager: PManagerDt

)
@Serializable
data class CaretakerPayment(
    val caretakerId: Int,
    val transactionId: String,
    val fullName: String,
    val phoneNumber: String,
    val month: String,
    val year: String,
    val paidAmount: Double,
    val paidAt: String
)

@Serializable
data class CaretakerResponseBody(
    val statusCode: Int,
    val message: String,
    val data: CaretakerData2
)
@Serializable
data class CaretakerData2(
    val caretaker: CaretakerDT
)
@Serializable
data class CaretakerRegistrationRequestBody(
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val pManagerId: Int,
    val salary: Double
)
@Serializable
data class CaretakerRegistrationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: CaretakerData
)
@Serializable
data class CaretakerPaymentRequestBody(
    val caretakerId: Int,
    val amount: Double,
)
@Serializable
data class CaretakerPaymentResponseBody(
    val statusCode: Int,
    val message: String,
    val data: CaretakerPaymentData
)

@Serializable
data class CaretakerPaymentData(
    val caretaker: CaretakerPaymentDt
)
@Serializable
data class CaretakerPaymentDt(
    val caretakerId: Int,
    val transactionId: String,
    val fullName: String,
    val phoneNumber: String,
    val paidAmount: Double,
    val paidAt: String?
)

