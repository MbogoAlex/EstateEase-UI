package com.example.tenant_care.network

import com.example.tenant_care.model.amenity.AmenitiesResponseBody
import com.example.tenant_care.model.amenity.AmenityDeletionResponseBody
import com.example.tenant_care.model.amenity.AmenityRequestBody
import com.example.tenant_care.model.amenity.AmenityResponseBody
import com.example.tenant_care.model.caretaker.CaretakerLoginRequestBody
import com.example.tenant_care.model.caretaker.CaretakerLoginResponseBody
import com.example.tenant_care.model.caretaker.MeterReadingRequestBody
import com.example.tenant_care.model.caretaker.MeterReadingResponseBody
import com.example.tenant_care.model.caretaker.MeterReadingsResponseBody
import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateRequestBody
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateResponseBody
import com.example.tenant_care.model.pManager.RentPaymentRowsUpdateResponseBody
import com.example.tenant_care.model.penalty.PenaltyResponseBody
import com.example.tenant_care.model.penalty.PenaltyStatusChangeResponseBody
import com.example.tenant_care.model.property.ArchiveUnitResponseBody
import com.example.tenant_care.model.property.PropertyRequestBody
import com.example.tenant_care.model.property.PropertyResponseBody
import com.example.tenant_care.model.property.PropertyUnitResponseBody
import com.example.tenant_care.model.property.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.tenant.LoginTenantRequestBody
import com.example.tenant_care.model.tenant.LoginTenantResponseBody
import com.example.tenant_care.model.tenant.RentPaymentRequestBody
import com.example.tenant_care.model.tenant.RentPaymentResponseBody
import com.example.tenant_care.model.tenant.RentPaymentRowsResponse
import com.example.tenant_care.model.tenant.UnitAssignmentRequestBody
import com.example.tenant_care.model.tenant.UnitAssignmentResponseBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface ApiRepository {
    suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody) : Response<PManagerResponseBody>
    suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody>
    suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody>
    suspend fun fetchFilteredProperties(
        tenantName: String?,
        rooms: String?,
        roomName: String?,
        occupied: Boolean,
    ): Response<PropertyUnitResponseBody>

    suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView>

    suspend fun addNewUnit(propertyRequestBody: PropertyRequestBody): Response<PropertyResponseBody>

    suspend fun updatePropertyUnit(propertyRequestBody: PropertyRequestBody, propertyId: Int): Response<SinglePropertyUnitResponseBody>
    suspend fun assignPropertyUnit(assignmentDetails: UnitAssignmentRequestBody): Response<UnitAssignmentResponseBody>

    suspend fun archiveUnit(
        propertyId: String,
        tenantId: String
    ): Response<ArchiveUnitResponseBody>

    suspend fun fetchRentPaymentStatusForAllTenants(
        month: String,
        year: String,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        tenantId: Int?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<RentPaymentDetailsResponseBody>

    suspend fun activatePenaltyForSingleTenant(
        rentPayment: RentPaymentRowUpdateRequestBody,
        rentPaymentTblId: Int
    ): Response<RentPaymentRowUpdateResponseBody>

    suspend fun deActivatePenaltyForSingleTenant(
        rentPaymentTblId: Int
    ): Response<RentPaymentRowUpdateResponseBody>

    suspend fun activatePenaltyForMultipleTenants(
        rentPayment: RentPaymentRowUpdateRequestBody,
        month: String,
        year: String
    ): Response<RentPaymentRowsUpdateResponseBody>

    suspend fun deActivatePenaltyForMultipleTenants(
        month: String,
        year: String
    ): Response<RentPaymentRowsUpdateResponseBody>

    // login tenant
    suspend fun loginTenant(tenant: LoginTenantRequestBody): Response<LoginTenantResponseBody>

    // get rent payment rows for various tenants

    suspend fun getRentPaymentRows(
        tenantId: Int,
        month: String?,
        year: String?,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<RentPaymentRowsResponse>

    // pay rent
    suspend fun payRent(
        rentPaymentTblId: Int,
        rentPaymentRequestBody: RentPaymentRequestBody
    ): Response<RentPaymentResponseBody>

    suspend fun getRentPaymentRowsAndGenerateReport(
        tenantId: Int,
        month: String?,
        year: String?,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<ResponseBody>

    suspend fun loginAsCaretaker(caretaker: CaretakerLoginRequestBody): Response<CaretakerLoginResponseBody>
    suspend fun getMeterReadings(month: String, year: String, meterReadingTaken: Boolean, tenantName: String?, propertyName: String?): Response<MeterReadingsResponseBody>

    suspend fun uploadMeterReading(meterReadingRequestBody: MeterReadingRequestBody, image: MultipartBody.Part): Response<MeterReadingResponseBody>

    suspend fun getMeterReadingDataById(id: Int): Response<MeterReadingResponseBody>

    suspend fun updateMeterReading(
        oldImageId: Int,
        meterReadingDataTableId: Int,
        meterReadingRequestBody: MeterReadingRequestBody,
        image: MultipartBody.Part
    ): Response<MeterReadingResponseBody>

    suspend fun addAmenity(
        amenityRequestBody: AmenityRequestBody,
        images: List<MultipartBody.Part>
    ): Response<AmenityResponseBody>

    suspend fun updateAmenityWithImages(
        amenityRequestBody: AmenityRequestBody,
        newImages: List<MultipartBody.Part>?,
        amenityId: Int
    ): Response<AmenityResponseBody>

    suspend fun updateAmenityWithoutImages(
        amenityRequestBody: AmenityRequestBody,
        amenityId: Int,
    ): Response<AmenityResponseBody>

    suspend fun deleteAmenity(
        amenityId: Int
    ): Response<AmenityDeletionResponseBody>

    suspend fun fetchAmenities(): Response<AmenitiesResponseBody>

    suspend fun fetchAmenity(id: Int): Response<AmenityResponseBody>

    suspend fun fetchFilteredAmenities(searchText: String?): Response<AmenitiesResponseBody>

    suspend fun deleteAmenityImage(id: Int): Response<AmenityDeletionResponseBody>

    suspend fun fetchPenalty(id: Int): Response<PenaltyResponseBody>

    suspend fun activateLatePaymentPenalty(month: String, year: String): Response<PenaltyStatusChangeResponseBody>
    suspend fun deActivateLatePaymentPenalty(month: String, year: String): Response<PenaltyStatusChangeResponseBody>
}

class NetworkRepository(private val apiService: ApiService): ApiRepository {
    override suspend fun loginPManager(pManagerLoginDetails: PManagerRequestBody): Response<PManagerResponseBody> = apiService.loginPManager(pManagerLoginDetails)
    override suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody> = apiService.fetchAllProperties()

    override suspend fun fetchPropertyByPropertyId(propertyId: Int): Response<SinglePropertyUnitResponseBody> = apiService.fetchPropertyByPropertyId(propertyId)

    override suspend fun fetchFilteredProperties(
        tenantName: String?,
        rooms: String?,
        roomName: String?,
        occupied: Boolean,
    ): Response<PropertyUnitResponseBody> = apiService.fetchFilteredProperties(
        tenantName = tenantName,
        rooms = rooms,
        roomName = roomName,
        occupied = occupied
    )
    override suspend fun fetchRentPaymentOverview(month: String, year: String): Response<RentPaymentOverView> = apiService.fetchRentPaymentOverview(
        month = month,
        year = year
    )

    override suspend fun addNewUnit(propertyRequestBody: PropertyRequestBody): Response<PropertyResponseBody> = apiService.addNewUnit(
        propertyRequestBody = propertyRequestBody
    )

    override suspend fun updatePropertyUnit(
        propertyRequestBody: PropertyRequestBody,
        propertyId: Int
    ): Response<SinglePropertyUnitResponseBody> = apiService.updatePropertyUnit(
        propertyRequestBody = propertyRequestBody,
        propertyId = propertyId
    )

    override suspend fun assignPropertyUnit(assignmentDetails: UnitAssignmentRequestBody): Response<UnitAssignmentResponseBody> = apiService.assignPropertyUnit(assignmentDetails)
    override suspend fun archiveUnit(
        propertyId: String,
        tenantId: String
    ): Response<ArchiveUnitResponseBody> = apiService.archiveUnit(
        propertyId = propertyId,
        tenantId = tenantId
    )

    override suspend fun fetchRentPaymentStatusForAllTenants(
        month: String,
        year: String,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        tenantId: Int?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<RentPaymentDetailsResponseBody> = apiService.fetchRentPaymentStatusForAllTenants(
        month = month,
        year = year,
        roomName = roomName,
        rooms = rooms,
        tenantName = tenantName,
        tenantId = tenantId,
        rentPaymentStatus = rentPaymentStatus,
        paidLate = paidLate,
        tenantActive = tenantActive
    )

    override suspend fun activatePenaltyForSingleTenant(
        rentPayment: RentPaymentRowUpdateRequestBody,
        rentPaymentTblId: Int
    ): Response<RentPaymentRowUpdateResponseBody> = apiService.activateLatePaymentPenaltyForSingleTenant(
        rentPayment = rentPayment,
        rentPaymentTblId = rentPaymentTblId
    )

    override suspend fun deActivatePenaltyForSingleTenant(rentPaymentTblId: Int): Response<RentPaymentRowUpdateResponseBody> = apiService.deActivateLatePaymentPenaltyForSingleTenant(
        rentPaymentTblId = rentPaymentTblId
    )

    override suspend fun activatePenaltyForMultipleTenants(
        rentPayment: RentPaymentRowUpdateRequestBody,
        month: String,
        year: String
    ): Response<RentPaymentRowsUpdateResponseBody> = apiService.activateLatePaymentPenaltyForMultipleTenants(
        rentPayment = rentPayment,
        month = month,
        year = year,
    )

    override suspend fun deActivatePenaltyForMultipleTenants(
        month: String,
        year: String
    ): Response<RentPaymentRowsUpdateResponseBody> = apiService.deActivateLatePaymentPenaltyForMultipleTenants(
        month = month,
        year = year
    )

    override suspend fun loginTenant(tenant: LoginTenantRequestBody): Response<LoginTenantResponseBody> = apiService.loginTenant(
        tenant = tenant
    )

    override suspend fun getRentPaymentRows(
        tenantId: Int,
        month: String?,
        year: String?,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<RentPaymentRowsResponse> = apiService.getRentPaymentRows(
        tenantId = tenantId,
        month = month,
        year = year,
        roomName = roomName,
        rooms = rooms,
        tenantName = tenantName,
        rentPaymentStatus = rentPaymentStatus,
        paidLate = paidLate,
        tenantActive = tenantActive
    )

    override suspend fun payRent(
        rentPaymentTblId: Int,
        rentPaymentRequestBody: RentPaymentRequestBody
    ): Response<RentPaymentResponseBody> = apiService.payRent(
        rentPaymentTblId = rentPaymentTblId,
        rentPaymentRequestBody = rentPaymentRequestBody
    )

    override suspend fun getRentPaymentRowsAndGenerateReport(
        tenantId: Int,
        month: String?,
        year: String?,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ): Response<ResponseBody> = apiService.getRentPaymentRowsAndGenerateReport(
        tenantId = tenantId,
        month = month,
        year = year,
        roomName = roomName,
        rooms = rooms,
        tenantName = tenantName,
        rentPaymentStatus = rentPaymentStatus,
        paidLate = paidLate,
        tenantActive = tenantActive
    )

    override suspend fun loginAsCaretaker(caretaker: CaretakerLoginRequestBody): Response<CaretakerLoginResponseBody> = apiService.loginAsCaretaker(
        caretaker = caretaker
    )

    override suspend fun getMeterReadings(
        month: String,
        year: String,
        meterReadingTaken: Boolean,
        tenantName: String?,
        propertyName: String?
    ): Response<MeterReadingsResponseBody> = apiService.getMeterReadings(
        month = month,
        year = year,
        meterReadingTaken = meterReadingTaken,
        tenantName = tenantName,
        propertyName = propertyName
    )

    override suspend fun uploadMeterReading(
        meterReadingRequestBody: MeterReadingRequestBody,
        image: MultipartBody.Part
    ): Response<MeterReadingResponseBody> = apiService.uploadMeterReading(
        meterReadingRequestBody = meterReadingRequestBody,
        image = image
    )

    override suspend fun getMeterReadingDataById(id: Int): Response<MeterReadingResponseBody> = apiService.getMeterReadingDataById(id)
    override suspend fun updateMeterReading(
        oldImageId: Int,
        meterReadingDataTableId: Int,
        meterReadingRequestBody: MeterReadingRequestBody,
        image: MultipartBody.Part
    ): Response<MeterReadingResponseBody> = apiService.updateMeterReading(
        oldImageId = oldImageId,
        meterReadingDataTableId = meterReadingDataTableId,
        meterReadingRequestBody = meterReadingRequestBody,
        image = image
    )

    override suspend fun addAmenity(
        amenityRequestBody: AmenityRequestBody,
        images: List<MultipartBody.Part>
    ): Response<AmenityResponseBody> = apiService.addAmenity(
        amenityRequestBody = amenityRequestBody,
        images = images
    )

    override suspend fun updateAmenityWithImages(
        amenityRequestBody: AmenityRequestBody,
        newImages: List<MultipartBody.Part>?,
        amenityId: Int
    ): Response<AmenityResponseBody> = apiService.updateAmenityWithImages(
        amenityRequestBody = amenityRequestBody,
        newImages = newImages,
        amenityId = amenityId
    )

    override suspend fun updateAmenityWithoutImages(
        amenityRequestBody: AmenityRequestBody,
        amenityId: Int
    ): Response<AmenityResponseBody> = apiService.updateAmenityWithoutImages(
        amenityRequestBody = amenityRequestBody,
        amenityId = amenityId
    )

    override suspend fun deleteAmenity(amenityId: Int): Response<AmenityDeletionResponseBody> = apiService.deleteAmenity(
        amenityId = amenityId
    )

    override suspend fun fetchAmenities(): Response<AmenitiesResponseBody> = apiService.fetchAmenities()
    override suspend fun fetchAmenity(id: Int): Response<AmenityResponseBody> = apiService.fetchAmenity(id)
    override suspend fun fetchFilteredAmenities(searchText: String?): Response<AmenitiesResponseBody> = apiService.fetchFilteredAmenities(
        searchText = searchText
    )

    override suspend fun deleteAmenityImage(id: Int): Response<AmenityDeletionResponseBody> = apiService.deleteAmenityImage(
        id = id
    )

    override suspend fun fetchPenalty(id: Int): Response<PenaltyResponseBody> = apiService.fetchPenalty(
        id = id
    )

    override suspend fun activateLatePaymentPenalty(
        month: String,
        year: String
    ): Response<PenaltyStatusChangeResponseBody> = apiService.activateLatePaymentPenalty(
        month = month,
        year = year
    )

    override suspend fun deActivateLatePaymentPenalty(
        month: String,
        year: String
    ): Response<PenaltyStatusChangeResponseBody> = apiService.deActivateLatePaymentPenalty(
        month = month,
        year = year
    )


}