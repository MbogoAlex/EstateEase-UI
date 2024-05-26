package com.example.tenant_care.ui.screens.caretakerViews.units

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.FilterByNumOfRoomsBox
import com.example.tenant_care.util.FilterByRoomNameBox
import com.example.tenant_care.util.HouseUnitItem
import com.example.tenant_care.util.SearchFieldForTenants
import com.example.tenant_care.util.UndoFilteringBox
import com.example.tenant_care.util.properties
import com.example.tenant_care.util.tenant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitsScreenComposable(
    modifier: Modifier = Modifier
) {
    val viewModel: UnoccupiedUnitsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        UnoccupiedUnitsScreen(
            rooms = uiState.selectableRooms,
            numberOfRoomsSelected = uiState.rooms,
            properties = uiState.properties,
            onSelectNumOfRooms = {
                viewModel.updateRooms(it.toString())
            },
            selectedUnitName = uiState.roomName,
            onChangeSelectedUnitName = {
                viewModel.updateRoomName(it)
            },
            unfilterUnits = { viewModel.unfilter() },
            numberOfUnits = uiState.properties.size
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitsScreen(
    rooms: List<String>,
    numberOfRoomsSelected: String?,
    properties: List<PropertyUnit>,
    onSelectNumOfRooms: (rooms: Int) -> Unit,
    selectedUnitName: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    unfilterUnits: () -> Unit,
    numberOfUnits: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        SearchFieldForTenants(
            labelText = "Search Tenant name",
            value = "",
            onValueChange = {  },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            FilterByNumOfRoomsBox(
                selectedNumOfRooms = numberOfRoomsSelected,
                onSelectNumOfRooms = onSelectNumOfRooms
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterByRoomNameBox(
                rooms = rooms,
                selectedUnit = selectedUnitName,
                onChangeSelectedUnitName = onChangeSelectedUnitName
            )
            Spacer(modifier = Modifier.weight(1f))
            UndoFilteringBox(
                unfilterUnits = unfilterUnits
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$numberOfUnits unoccupied units",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(properties) {
                HouseUnitItem(
                    propertyUnit = it,
                    tenant = it.activeTenant,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun UnoccupiedUnitsScreenPreview() {
    Tenant_careTheme {
        UnoccupiedUnitsScreen(
            rooms = emptyList(),
            numberOfRoomsSelected = "",
            properties = properties,
            onSelectNumOfRooms = {},
            selectedUnitName = "",
            onChangeSelectedUnitName = {},
            unfilterUnits = { /*TODO*/ },
            numberOfUnits = 4
        )
    }
}