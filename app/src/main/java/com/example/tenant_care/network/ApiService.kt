package com.example.tenant_care.network

import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.property.PropertyUnitResponseBody
import com.example.tenant_care.model.property.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.model.property.NewPropertyRequestBody
import com.example.tenant_care.model.property.NewPropertyResponseBody
import com.example.tenant_care.model.tenant.UnitAssignmentRequestBody
import com.example.tenant_care.model.tenant.UnitAssignmentResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    @GET("propertyunit/filter")
    suspend fun fetchFilteredProperties(
        @Query("tenantName") tenantName: String?,
        @Query("rooms") rooms: String?,
        @Query("roomName") roomName: String?,
        @Query("occupied") occupied: Boolean
    ): Response<PropertyUnitResponseBody>
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

    // fetch unoccupied properties
    @GET("propertyunit/unoccupied")
    suspend fun fetchUnoccupiedUnits(): Response<PropertyUnitResponseBody>

    // assign unit

    @POST("tenant")
    suspend fun assignPropertyUnit(
        @Body assignmentDetails: UnitAssignmentRequestBody
    ): Response<UnitAssignmentResponseBody>
}