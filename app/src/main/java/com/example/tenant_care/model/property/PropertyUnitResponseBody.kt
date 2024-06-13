package com.example.tenant_care.model.property

import com.example.tenant_care.model.caretaker.WaterMeterDt
import kotlinx.serialization.Serializable

// multiple properties
@Serializable
data class PropertyUnitResponseBody (
    val statusCode: Int,
    val message: String,
    val data: PropertyUnitData,
)
@Serializable
data class PropertyUnitData (
    val property: List<PropertyUnit>,
)
@Serializable
data class PropertyUnit (
    val propertyUnitId: Int,
    val rooms: String,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double,
    val propertyAddedAt: String,
    val propertyAssignmentStatus: Boolean,
    val activeTenant: PropertyTenant?,
    val tenants: List<PropertyTenant>,
    val meterReadings: List<WaterMeterDt>
)
@Serializable
data class PropertyTenant (
    val tenantId: Int,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean
)

// single property
@Serializable
data class SinglePropertyUnitResponseBody (
    val statusCode: Int,
    val message: String,
    val data: SinglePropertyUnitData,
)
@Serializable
data class SinglePropertyUnitData (
    val property: PropertyUnit,
)

// archive unit
@Serializable
data class ArchiveUnitResponseBody (
    val statusCode: Int,
    val message: String
)