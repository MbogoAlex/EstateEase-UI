package com.example.tenant_care.model.tenant

import kotlinx.serialization.Serializable

@Serializable
data class LoginTenantRequestBody(
    val tenantPhoneNumber: String,
    val tenantPassword: String
)
@Serializable
data class LoginTenantResponseBody(
    val statusCode: Int,
    val message: String,
    val data: LoginTenantResponseBodyData
)
@Serializable
data class LoginTenantResponseBodyData(
    val tenant: TenantDetails
)
@Serializable
data class TenantDetails(
    val tenantId: Int,
    val fullName: String,
    val nationalIdOrPassportNumber: String,
    val phoneNumber: String,
    val email: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean,
    val propertyUnit: TenantPropertyUnit,
)
@Serializable
data class TenantPropertyUnit (
    val propertyUnitId: Int,
    val rooms: String,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double
)

