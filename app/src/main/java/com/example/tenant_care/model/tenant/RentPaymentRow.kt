package com.example.tenant_care.model.tenant

import kotlinx.serialization.Serializable

@Serializable
data class RentPaymentRowsResponse(
    val statusCode: Int,
    val message: String,
    val data: RentPaymentRowData
)
@Serializable
data class RentPaymentRowData(
    val rentpayment: List<RentPayment>
)
@Serializable
data class RentPayment(
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
    val numberOfRooms: Int,
    val tenantId: Int,
    val email: String,
    val fullName: String,
    val nationalIdOrPassport: String,
    val phoneNumber: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean,
    val waterUnits: Double?,
    val pricePerUnit: Double?,
    val meterReadingDate: String?,
    val imageFile: String?
)