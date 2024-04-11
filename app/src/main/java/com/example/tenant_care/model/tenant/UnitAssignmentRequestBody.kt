package com.example.tenant_care.model.tenant

import kotlinx.serialization.Serializable

@Serializable
data class UnitAssignmentRequestBody(
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val propertyUnitId: Int,
    val tenantAddedByPManagerId: Int,
)
@Serializable
data class UnitAssignmentResponseBody (
    val statusCode: Int,
    val message: String,
    val data: AssignmentResponseData
)
@Serializable
data class AssignmentResponseData (
    val tenant: TenantData
)
@Serializable
data class TenantData (
    val tenantId: Int,
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean
)


