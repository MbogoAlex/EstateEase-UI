package com.example.tenant_care.model.tenant

import kotlinx.serialization.Serializable

@Serializable
data class RentPaymentRequestBody(
    val waterMeterDataTableId: Int,
    val msisdn: String,
    val payableAmount: Double
)
@Serializable
data class RentPaymentResponseBody(
    val statusCode: Int,
    val message: String,
    val data: RentPaymentResponseBodyData
)
@Serializable
data class RentPaymentResponseBodyData(
    val rentPayment: RentPaymentData
)
@Serializable
data class RentPaymentData(
    val rentPaymentTblId: Int,
    val transactionId: String,
    val monthlyRent: Double,
    val month: String,
    val year: String,
    val dueDate: String,
    val rentPaymentStatus: Boolean,
    val penaltyActive: Boolean,
    val daysLate: Int,
    val penaltyPerDay: Double,
    val paidAmount: Double,
    val paidAt: String,
    val paidLate: Boolean,
    val tenant: TenantData,
    val unitName: String
)

@Serializable
data class PaymentStatusResponseBody(
    val statusCode: Int,
    val message: String,
    val data: PaymentStatusDT
)

@Serializable
data class PaymentStatusDT(
    val payment: Boolean
)

