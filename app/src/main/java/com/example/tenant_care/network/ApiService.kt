package com.example.tenant_care.network

import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.PropertyUnitResponseBody
import com.example.tenant_care.model.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.model.property.NewPropertyRequestBody
import com.example.tenant_care.model.property.NewPropertyResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // login pmanager
    @POST("pmanager/login")
    suspend fun loginPManager(
        @Body pManagerLoginDetails: PManagerRequestBody
    ) : Response<PManagerResponseBody>

    // fetch all properties

    @GET("propertyunit")
    suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody>

    // fetch property by propertyId
    @GET("propertyunit/propertyId={propertyId}")
    suspend fun fetchPropertyByPropertyId(@Path("propertyId") propertyId: Int): Response<SinglePropertyUnitResponseBody>

    // fetch all occupied properties
    @GET("propertyunit/occupied")
    suspend fun fetchAllOccupiedProperties(): Response<PropertyUnitResponseBody>
    @GET("rentpayment/overview/month={month}/year={year}")
    suspend fun fetchRentPaymentOverview(
        @Path("month") month: String,
        @Path("year") year: String
    ): Response<RentPaymentOverView>

    // Add a new unit
    @POST("propertyunit")
    suspend fun addNewUnit(
        @Body propertyRequestBody: NewPropertyRequestBody
    ): Response<NewPropertyResponseBody>
}