package com.example.tenant_care.model.pManager

import kotlinx.serialization.Serializable

@Serializable
data class RentPaymentRowUpdateRequestBody(
    val penaltyPerDay: Double
)

// single row
@Serializable
data class RentPaymentRowUpdateResponseBody(
    val statusCode: Int,
    val message: String,
    val data: RentPaymentRowUpdateResponseBodyData
)
@Serializable
data class RentPaymentRowUpdateResponseBodyData(
    val rentpayment: UpdatedRentPaymentDetails
)
@Serializable
data class UpdatedRentPaymentDetails (
    val rentPaymentTblId: Int,
    val transactionId: Int?,
    val monthlyRent: Double,
    val month: String,
    val year: String,
    val dueDate: String,
    val rentPaymentStatus: Boolean,
    val penaltyActive: Boolean,
    val daysLate: Int,
    val penaltyPerDay: Double,
    val paidAmount: Double?,
    val paidAt: String?,
    val paidLate: Boolean?,
    val tenant: UpdatedRentPaymentDetailsTenant,
    val unitName: String
)
@Serializable
data class UpdatedRentPaymentDetailsTenant (
    val tenantId: Int,
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean,
)

// multiple rows
@Serializable
data class RentPaymentRowsUpdateResponseBody (
    val statusCode: Int,
    val message: String,
    val data: RentPaymentRowsUpdateRequestBodyData
)

@Serializable
data class RentPaymentRowsUpdateRequestBodyData (
    val rentpayment: List<UpdatedRentPaymentDetails>
)
