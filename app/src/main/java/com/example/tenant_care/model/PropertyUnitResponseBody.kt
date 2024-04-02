package com.example.tenant_care.model

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
    val numberOfRooms: Int,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double,
    val propertyAddedAt: String,
    val propertyAssignmentStatus: Boolean,
    val tenants: List<PropertyTenant>
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