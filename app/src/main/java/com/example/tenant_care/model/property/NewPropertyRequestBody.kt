package com.example.tenant_care.model.property

import kotlinx.serialization.Serializable

@Serializable
data class PropertyRequestBody(
    val numberOfRooms: Int,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double,
    val propertyManagerId: Int
)
@Serializable
data class PropertyResponseBody(
    val statusCode: Int,
    val message: String,
    val data: PropertyRequestBodyData
)
@Serializable
data class PropertyRequestBodyData(
    val property: NewProperty
)
@Serializable
data class NewProperty(
    val propertyUnitId: Int,
    val numberOfRooms: Int,
    val propertyNumberOrName: Int,
    val propertyDescription: String,
    val monthlyRent: Double,
    val propertyAddedAt: String,
    val propertyAssignmentStatus: Boolean
)