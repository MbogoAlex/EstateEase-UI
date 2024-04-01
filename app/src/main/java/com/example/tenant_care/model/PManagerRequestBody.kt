package com.example.tenant_care.model

import kotlinx.serialization.Serializable

@Serializable
data class PManagerRequestBody(
    val email: String,
    val password: String,
)
@Serializable
data class PManagerResponseBody(
    val statusCode: Int,
    val message: String,
    val data: PManagerData,
)
@Serializable
data class PManagerData(
    val pmanager: PManagerDataBody,
)

@Serializable
data class PManagerDataBody(
    val pmanagerId: Int,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val propertyManagerAddedAt: String,
    val propertyUnits: List<PManagerProperty>,
    val tenants: List<PManagerTenant>
)
@Serializable
data class PManagerProperty(
    val propertyUnitId: Int,
    val propertyNumberOrName: String,
    val propertyDescription: String,
    val monthlyRent: Double
)
@Serializable
data class PManagerTenant(
    val tenantId: Int,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val tenantAddedAt: String,
    val tenantActive: Boolean
)
