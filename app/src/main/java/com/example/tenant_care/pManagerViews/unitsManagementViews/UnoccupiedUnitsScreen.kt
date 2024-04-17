package com.example.tenant_care.pManagerViews.unitsManagementViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitsComposable(
    navigateToUnoccupiedPropertyDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: UnoccupiedUnitsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = modifier
    ) {
        UnoccupiedUnitsScreen(
            selectedNumOfRooms = uiState.numOfRoomsSelected,
            onSelectNumOfRooms = {
                viewModel.filterByNumberOfRooms(it)
            },
            rooms = uiState.roomNames,
            selectedUnit = uiState.unitName,
            onChangeSelectedUnitName = {
                viewModel.filterByRoomName(it)
            },
            undoFilter = {
                viewModel.unfilterProperties()
            },
            properties = uiState.properties,
            numberOfUnits = uiState.properties.size,
            navigateToUnoccupiedPropertyDetailsScreen = navigateToUnoccupiedPropertyDetailsScreen
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitsScreen(
    selectedNumOfRooms: String?,
    onSelectNumOfRooms: (rooms: Int) -> Unit,
    rooms: List<String>,
    selectedUnit: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    undoFilter: () -> Unit,
    properties: List<PropertyUnit>,
    numberOfUnits: Int,
    navigateToUnoccupiedPropertyDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            FilterUnOccupiedUnitsByNumOfRoomsBox(
                selectedNumOfRooms = selectedNumOfRooms,
                onSelectNumOfRooms = onSelectNumOfRooms,
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterUnoccupiedUnitsByNameBox(
                rooms = rooms,
                selectedUnit = selectedUnit,
                onChangeSelectedUnitName = onChangeSelectedUnitName
            )
            Spacer(modifier = Modifier.weight(1f))
            UndoFilteringForUnoccupiedUnitsBox(
                undoFilter = undoFilter
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$numberOfUnits units",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(properties.size) {
                UnoccupiedUnitItem(
                    propertyUnit = properties[it],
                    propertyIndex = it,
                    navigateToUnoccupiedPropertyDetailsScreen = navigateToUnoccupiedPropertyDetailsScreen,
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitItem(
    propertyUnit: PropertyUnit,
    propertyIndex: Int,
    navigateToUnoccupiedPropertyDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToUnoccupiedPropertyDetailsScreen(propertyUnit.propertyUnitId.toString())
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Text(text = "Room No: ")
                    Text(
                        text = propertyUnit.propertyNumberOrName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Uploaded on: ")
                Text(
                    text = ReusableFunctions.formatDateTimeValue(propertyUnit.propertyAddedAt),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Unit actions"
                )
            }
        }
    }
}

@Composable
fun FilterUnOccupiedUnitsByNumOfRoomsBox(
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
                    text = "No. Rooms".takeIf { selectedNumOfRooms == null } ?: "$selectedNumOfRooms room".takeIf { selectedNumOfRooms?.toInt() == 1 } ?: "$selectedNumOfRooms rooms",
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
                }
            }
        }
    }
}

@Composable
fun FilterUnoccupiedUnitsByNameBox(
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
                    text = "Room name".takeIf { selectedUnit == null } ?: "$selectedUnit",
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
                }
            }
        }
    }
}

@Composable
fun UndoFilteringForUnoccupiedUnitsBox(
    undoFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .clickable {
                undoFilter()
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
fun SearchFieldForUnoccupiedUnits(
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
                painter = painterResource(id = R.drawable.house),
                contentDescription = null
            )
        },
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}


@Composable
fun AssignUnitScreen(
    modifier: Modifier = Modifier
) {

}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun UnOccupiedUnitDetailsPreview() {
//    Tenant_careTheme {
//        UnOccupiedUnitDetails()
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun UnOccupiedUnitsScreenPreview() {
    Tenant_careTheme {
        UnoccupiedUnitsScreen(
            selectedNumOfRooms = "1",
            onSelectNumOfRooms = {},
            rooms = emptyList(),
            selectedUnit = "",
            onChangeSelectedUnitName = {},
            undoFilter = { /*TODO*/ },
            properties = mutableListOf(),
            numberOfUnits = 5,
            navigateToUnoccupiedPropertyDetailsScreen = {}
        )
    }
}