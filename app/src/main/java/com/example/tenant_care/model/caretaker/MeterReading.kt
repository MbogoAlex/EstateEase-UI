package com.example.tenant_care.model.caretaker

import kotlinx.serialization.Serializable
@Serializable
data class MeterReadingRequestBody(
    val meterDtTableId: Int,
    val waterUnits: Double,
    val month: String,
    val year: String
)
@Serializable
data class MeterReadingResponseBody(
    val statusCode: Int,
    val message: String,
    val data: WaterMeterResponseDT
)
@Serializable
data class WaterMeterResponseDT(
    val waterMeter: WaterMeterDt
)
@Serializable
data class MeterReadingsResponseBody(
    val statusCode: Int,
    val message: String,
    val data: MeterReadingData
)
@Serializable
data class MeterReadingData(
    val waterMeter: List<WaterMeterDt>
)
@Serializable
data class WaterMeterDt(
    val id: Int,
    val propertyName: String,
    val tenantName: String,
    val waterUnits: Double?,
    val pricePerUnit: Double?,
    val meterReadingDate: String?,
    val month: String,
    val year: String,
    val imageName: String?,
    val imageId: Int?,
    val previousWaterMeterData: PreviousWaterMeterData?
)
@Serializable
data class PreviousWaterMeterData(
    val propertyName: String?,
    val tenantName: String?,
    val waterUnits: Double?,
    val pricePerUnit: Double?,
    val meterReadingDate: String?,
    val month: String?,
    val year: String?,
    val imageName: String?,
    val imageId: Int?
)
