package com.example.tenant_care.model.pManager

import kotlinx.serialization.Serializable

@Serializable
data class RentPaymentDetailsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: RentPaymentDetailsResponseBodyData
)
@Serializable
data class RentPaymentDetailsResponseBodyData (
    val rentpayment: List<TenantRentPaymentData>
)
@Serializable
data class TenantRentPaymentData(
    val rentPaymentTblId: Int,
    val dueDate: String,
    val month: String,
    val monthlyRent: Double,
    val paidAmount: Double?,
    val paidAt: String?,
    val paidLate: Boolean?,
    val daysLate: Int,
    val rentPaymentStatus: Boolean,
    val penaltyActive: Boolean,
    val penaltyPerDay: Double,
    val transactionId: String?,
    val year: String,
    val propertyNumberOrName: String,
    val numberOfRooms: String,
    val tenantId: Int,
    val email: String,
    val fullName: String,
    val nationalIdOrPassport: String,
    val phoneNumber: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean
)
