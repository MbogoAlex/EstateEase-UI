package com.example.tenant_care.container

import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.PropertyUnitResponseBody
import com.example.tenant_care.model.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.network.ApiService
import retrofit2.Response

interface ApiRepository {
    suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody) : Response<PManagerResponseBody>
    suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody>
    suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody>
    suspend fun fetchAllOccupiedProperties(): Response<PropertyUnitResponseBody>

    suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView>
}

class NetworkRepository(private val apiService: ApiService): ApiRepository {
    override suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody): Response<PManagerResponseBody> = apiService.loginPManager(pManagerLoginDetails)
    override suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody> = apiService.fetchAllProperties()

    override suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody> = apiService.fetchPropertyByPropertyId(propertyId)

    override suspend fun fetchAllOccupiedProperties(): Response<PropertyUnitResponseBody> = apiService.fetchAllOccupiedProperties()
    override suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView> = apiService.fetchRentPaymentOverview(
        month = month,
        year = year
    )

}