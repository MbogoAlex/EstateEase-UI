package com.example.tenant_care.util

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