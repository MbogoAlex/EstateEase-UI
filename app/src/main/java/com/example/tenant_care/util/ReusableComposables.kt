package com.example.tenant_care.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tenant_care.R
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit

// filter by name
@Composable
fun FilterByNumOfRoomsBox(
    selectedNumOfRooms: String?,
    onSelectNumOfRooms: (rooms: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var rooms = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var expanded by remember {
        mutableStateOf(false)
    }

    var icon: ImageVector
    if(expanded) {
        icon = Icons.Default.KeyboardArrowUp
    } else {
        icon = Icons.Default.KeyboardArrowDown
    }

    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .widthIn(min = 100.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No. Rooms".takeIf { selectedNumOfRooms.isNullOrEmpty() } ?: "$selectedNumOfRooms room".takeIf { selectedNumOfRooms?.toInt() == 1 } ?: "$selectedNumOfRooms rooms",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 250.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    rooms.forEachIndexed { index, i ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "$i room".takeIf { i == 1 } ?: "$i rooms"
                                )
                            },
                            onClick = {
                                onSelectNumOfRooms(i)
                                expanded = false
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun FilterByRoomNameBox(
    rooms: List<String>,
    selectedUnit: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var icon: ImageVector
    if(expanded) {
        icon = Icons.Default.KeyboardArrowUp
    } else {
        icon = Icons.Default.KeyboardArrowDown
    }
    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .widthIn(min = 100.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room name".takeIf { selectedUnit.isNullOrEmpty() } ?: "$selectedUnit",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 250.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    rooms.forEachIndexed { index, i ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = i
                                )
                            },
                            onClick = {
                                onChangeSelectedUnitName(i)
                                expanded = false
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun UndoFilteringBox(
    unfilterUnits: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .clickable {
                unfilterUnits()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search"
            )
        }
    }
}

@Composable
fun SearchFieldForTenants(
    labelText: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = {
            Text(text = labelText)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null
            )
        },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HouseUnitItem(
    propertyUnit: PropertyUnit,
    tenant: PropertyTenant?,
//    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row {
                Text(text = "Room No: ")
                Text(
                    text = propertyUnit.propertyNumberOrName,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(text = "Rooms:")
                Text(text = propertyUnit.numberOfRooms.toString())
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(tenant != null) {
                Column {
                    Row {
                        Text(text = "Occupant: ")
                        Text(text = tenant.fullName)
                    }
                    Row {
                        Text(text = "Phone no: ")
                        Text(
                            text = tenant.phoneNumber,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(text = "Occupant Since: ")
                    Text(
                        text = ReusableFunctions.formatDateTimeValue(tenant.tenantAddedAt),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Composable
fun PropertyDataCell(
    waterMeterData: WaterMeterDt,
    navigateToEditMeterReadingScreen: (meterTableId: String, childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row {
                Text(text = "Property name: ")
                Text(text = waterMeterData.propertyName)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(text = "Tenant name: ")
                Text(text = waterMeterData.tenantName)
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(waterMeterData.waterUnits != null) {
                Row {
                    Text(text = "Water units: ")
                    Text(text = waterMeterData.waterUnits.toString())
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${ReusableFunctions.formatMoneyValue(waterMeterData.pricePerUnit!!)}/unit")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        navigateToEditMeterReadingScreen(waterMeterData.id.toString(),"update-screen")
                    }
                ) {
                    Text(text = "UPDATE")
                }

            } else {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        navigateToEditMeterReadingScreen(waterMeterData.id.toString(),"upload-screen")
                    }
                ) {
                    Text(text = "UPLOAD")
                }
            }
        }
    }
}