package com.example.tenant_care.model.property

import kotlinx.serialization.Serializable

@Serializable
data class NewPropertyRequestBody(
    val numberOfRooms: Int,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double,
    val propertyManagerId: Int
)
@Serializable
data class NewPropertyResponseBody(
    val statusCode: Int,
    val message: String,
    val data: NewPropertyRequestBodyData
)
@Serializable
data class NewPropertyRequestBodyData(
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