package com.example.tenant_care.model.amenity

import kotlinx.serialization.Serializable

@Serializable
data class AmenityRequestBody(
    val amenityName: String,
    val amenityDescription: String,
    val providerName: String,
    val providerPhoneNumber: String,
    val providerEmail: String?,
    val addedBy: String,
    val propertyManagerId: Int
)
@Serializable
data class AmenityResponseBody (
    val statusCode: Int,
    val message: String,
    val data: AmenityData
)
@Serializable
data class AmenityData(
    val amenity: Amenity
)
@Serializable
data class Amenity(
    val amenityId: Int,
    val amenityName: String,
    val amenityDescription: String,
    val providerName: String,
    val providerPhoneNumber: String,
    val providerEmail: String?,
    val addedBy: String,
    val images: List<AmenityImage>,
    val pmanagerId: Int,
)
@Serializable
data class AmenityImage(
    val id: Int,
    val name: String
)
@Serializable
data class AmenitiesResponseBody(
    val statusCode: Int,
    val message: String,
    val data: AmenitiesData
)
@Serializable
data class AmenitiesData(
    val amenities: List<Amenity>
)

data class AmenityDeletionResponseBody(
    val statusCode: Int,
    val message: String,
    val data: DeletionData
)

data class DeletionData(
    val amenity: String
)
