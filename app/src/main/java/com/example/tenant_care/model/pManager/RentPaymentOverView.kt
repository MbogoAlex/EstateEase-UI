package com.example.tenant_care.model.pManager

import kotlinx.serialization.Serializable

@Serializable
data class RentPaymentOverView(
    val statusCode: Int,
    val message : String,
    val data: RentPaymentOverViewData
)
@Serializable
data class RentPaymentOverViewData(
    val rentpayment: RentPaymentData
)
@Serializable
data class RentPaymentData(
    val totalExpectedRent: Double,
    val totalUnits: Int,
    val paidAmount: Double,
    val clearedUnits: Int,
    val deficit: Double,
    val unclearedUnits: Int,
)
