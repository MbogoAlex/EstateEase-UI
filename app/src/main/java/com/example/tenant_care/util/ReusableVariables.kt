package com.example.tenant_care.util

import com.example.tenant_care.model.caretaker.PreviousWaterMeterData
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews.tenantData

val properties = listOf<PropertyUnit>(
    PropertyUnit(
        propertyUnitId = 1,
        numberOfRooms = 2,
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        numberOfRooms = 2,
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        numberOfRooms = 2,
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-02-03 14:44",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList()
    ),
    PropertyUnit(
        propertyUnitId = 1,
        numberOfRooms = 2,
        propertyNumberOrName = "Col A1",
        propertyDescription = "2 bedrooms property",
        monthlyRent = 2000.00,
        propertyAddedAt = "2024-05-22T11:14:57.236022",
        propertyAssignmentStatus = true,
        activeTenant = tenantData,
        tenants = emptyList()
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
    waterUnits = 3.00,
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
    waterUnits = 3.00,
    pricePerUnit = 150.00,
    meterReadingDate = "2024-04-15T19:36:38.168224",
    month = "MAY",
    year = "2024",
    imageName = null,
    imageId = 1,
    previousWaterMeterData = previousWaterMeterData
)

val waterMeterReadings: List<WaterMeterDt> = listOf(
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = 3.00,
        pricePerUnit = 150.00,
        meterReadingDate = "2024-04-15T19:36:38.168224",
        month = "MAY",
        year = "2024",
        imageName = "image",
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
)

val unreadWaterMeterReadings: List<WaterMeterDt> = listOf(
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
    WaterMeterDt(
        id = 1,
        propertyName = "Col A4",
        tenantName = "Alex Mbogo",
        waterUnits = null,
        pricePerUnit = null,
        meterReadingDate = null,
        month = "MAY",
        year = "2024",
        imageName = null,
        imageId = 1,
        previousWaterMeterData = previousWaterMeterData
    ),
)



