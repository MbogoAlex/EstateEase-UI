package com.example.tenant_care.model.penalty

import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.tenant.TenantData
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PenaltyRequestBody(
    val penaltyPerDay: Double
)
@Serializable
data class PenaltyUpdateRequestBody(
    val name: String,
    val cost: Double
)
@Serializable
data class PenaltyResponseBody(
    val statusCode: Int,
    val message: String,
    val data: PenaltyData
)
@Serializable
data class PenaltyData(
    val penalty: PenaltyDT
)
@Serializable
data class PenaltyDT(
    val id: Int,
    val name: String,
    val status: Boolean,
    val cost: Double
)
@Serializable
data class PenaltyStatusChangeResponseBody(
    val statusCode: Int,
    val message: String,
    val data: RentpaymentData
)
@Serializable
data class RentpaymentData(
    val rentpayment: List<RentPaymentDT>
)
@Serializable
data class RentPaymentDT(
    val rentPaymentTblId: Int,
    val transactionId: String?,
    val monthlyRent: Double,
    val month: String,
    val year: String,
    val dueDate: String,
    val rentPaymentStatus: Boolean,
    val penaltyActive: Boolean,
    val daysLate: Int,
    val penaltyPerDay: Double,
    val paidAmount: Double?,
    val paidAt: Double?,
    val paidLate: Boolean?,
    val tenant: TenantData,
    val unitName: String,
    val numberOfRooms: Int,
    val waterMeterDataDTO: WaterMeterDt
)
