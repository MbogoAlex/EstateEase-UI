package com.example.tenant_care.container

import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.property.PropertyUnitResponseBody
import com.example.tenant_care.model.property.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.model.property.NewPropertyRequestBody
import com.example.tenant_care.model.property.NewPropertyResponseBody
import com.example.tenant_care.network.ApiService
import retrofit2.Response

interface ApiRepository {
    suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody) : Response<PManagerResponseBody>
    suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody>
    suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody>
    suspend fun fetchAllOccupiedProperties(
        tenantName: String?,
        rooms: String?,
        roomName: String?
    ): Response<PropertyUnitResponseBody>

    suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView>

    suspend fun addNewUnit(propertyRequestBody: NewPropertyRequestBody): Response<NewPropertyResponseBody>

    suspend fun fetchUnoccupiedUnits(): Response<PropertyUnitResponseBody>
}

class NetworkRepository(private val apiService: ApiService): ApiRepository {
    override suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody): Response<PManagerResponseBody> = apiService.loginPManager(pManagerLoginDetails)
    override suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody> = apiService.fetchAllProperties()

    override suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody> = apiService.fetchPropertyByPropertyId(propertyId)

    override suspend fun fetchAllOccupiedProperties(
        tenantName: String?,
        rooms: String?,
        roomName: String?
    ): Response<PropertyUnitResponseBody> = apiService.fetchAllOccupiedProperties(
        tenantName = tenantName,
        rooms = rooms,
        roomName = roomName
    )
    override suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView> = apiService.fetchRentPaymentOverview(
        month = month,
        year = year
    )

    override suspend fun addNewUnit(propertyRequestBody: NewPropertyRequestBody): Response<NewPropertyResponseBody> = apiService.addNewUnit(
        propertyRequestBody = propertyRequestBody
    )

    override suspend fun fetchUnoccupiedUnits(): Response<PropertyUnitResponseBody> = apiService.fetchUnoccupiedUnits()

}