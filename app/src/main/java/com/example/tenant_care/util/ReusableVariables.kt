package com.example.tenant_care.util

import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.model.amenity.AmenityImage
import com.example.tenant_care.model.caretaker.CaretakerDT
import com.example.tenant_care.model.caretaker.CaretakerPayment
import com.example.tenant_care.model.caretaker.PManagerDt
import com.example.tenant_care.model.caretaker.PreviousWaterMeterData
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.tenantData
import kotlinx.serialization.Serializable

val properties = listOf<PropertyUnit>(
    PropertyUnit(
        propertyUnitId = 1,
        rooms = "Bedsitter",
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList(),
        meterReadings = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        rooms = "Bedsitter",
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList(),
        meterReadings = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        rooms = "Bedsitter",
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList(),
        meterReadings = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        rooms = "Bedsitter",
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-05-22T11:14:57.236022",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList(),
        meterReadings = emptyList()
    ),
)

val tenant = PropertyTenant(
    tenantId = 1,
    fullName = "Alex Mbogo",
    phoneNumber = "123456789",
    email = "alex@gmail.com",
    tenantAddedAt = "2024-05-22T11:14:57.236022",
    tenantActive = true
)

val previousWaterMeterData: PreviousWaterMeterData = PreviousWaterMeterData(
    propertyName = "Col A4",
    tenantName = "Alex Mbogo",
    waterUnitsReading = 3.00,
    pricePerUnit = 150.00,
    meterReadingDate = "2024-04-15T19:36:38.168224",
    month = "MAY",
    year = "2024",
    imageName = "image",
    imageId = 1
)

val waterMeterData: WaterMeterDt = WaterMeterDt(
    id = 1,
    propertyName = "Col A4",
    tenantName = "Alex Mbogo",
    waterUnitsReading = 3.00,
    pricePerUnit = 150.00,
    meterReadingDate = "2024-04-15T19:36:38.168224",
    month = "MAY",
    year = "2024",
    imageName = null,
    previousWaterMeterData = previousWaterMeterData,
    imageId = 1,
)

val waterMeterReadings: List<WaterMeterDt> = listOf(
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
)

val unreadWaterMeterReadings: List<WaterMeterDt> = listOf(
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnitsReading = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData,
    ),
)


val sampleAmenityImages = listOf<AmenityImage>(
    AmenityImage(
        id = 1,
        name = ""
    ),
    AmenityImage(
        id = 2,
        name = ""
    ),
)

val sampleAmenity = Amenity(
    amenityId = 1,
    amenityName = "Cake bakery",
    amenityDescription = "Delver cakes of various size to your door",
    providerName = "Jane Gakii",
    providerPhoneNumber = "0786552621",
    providerEmail = "jane@gmail.com",
    addedBy = "Alex Mbogo",
    images = sampleAmenityImages,
    pmanagerId = 1,
)


val pManagerExample = PManagerDt(
    fullName = "John Doe",
    nationalIdOrPassportNumber = "23453234",
    phoneNumber = "567876545",
    email = "doe@gmail.com",
    pmanagerId = 2
)

val caretakerExample = CaretakerDT(
    caretakerId = 1,
    fullName = "Alex Mbogo",
    nationalIdOrPassportNumber = "123456543",
    phoneNumber = "098765699",
    email = "alex@gmail.com",
    caretakerAddedAt = "2024-04-15T19:36:38.168224",
    active = true,
    salary = 10.0,
    payments = emptyList(),
    pmanager = pManagerExample
)

val caretakersExample = listOf(
    CaretakerDT(
        caretakerId = 1,
        fullName = "Alex Mbogo",
        nationalIdOrPassportNumber = "123456543",
        phoneNumber = "098765699",
        email = "alex@gmail.com",
        caretakerAddedAt = "2024-04-15T19:36:38.168224",
        active = true,
        salary = 10.0,
        payments = emptyList(),
        pmanager = pManagerExample
    ),
    CaretakerDT(
        caretakerId = 1,
        fullName = "Alex Mbogo",
        nationalIdOrPassportNumber = "123456543",
        phoneNumber = "098765699",
        email = "alex@gmail.com",
        caretakerAddedAt = "2024-04-15T19:36:38.168224",
        active = true,
        salary = 10.0,
        payments = emptyList(),
        pmanager = pManagerExample
    ),
    CaretakerDT(
        caretakerId = 1,
        fullName = "Alex Mbogo",
        nationalIdOrPassportNumber = "123456543",
        phoneNumber = "098765699",
        email = "alex@gmail.com",
        caretakerAddedAt = "2024-04-15T19:36:38.168224",
        active = true,
        salary = 10.0,
        payments = emptyList(),
        pmanager = pManagerExample
    ),
    CaretakerDT(
        caretakerId = 1,
        fullName = "Alex Mbogo",
        nationalIdOrPassportNumber = "123456543",
        phoneNumber = "098765699",
        email = "alex@gmail.com",
        caretakerAddedAt = "2024-04-15T19:36:38.168224",
        active = true,
        salary = 10.0,
        payments = emptyList(),
        pmanager = pManagerExample
    ),
    CaretakerDT(
        caretakerId = 1,
        fullName = "Alex Mbogo",
        nationalIdOrPassportNumber = "123456543",
        phoneNumber = "098765699",
        email = "alex@gmail.com",
        caretakerAddedAt = "2024-04-15T19:36:38.168224",
        active = true,
        salary = 10.0,
        payments = emptyList(),
        pmanager = pManagerExample
    )
)

