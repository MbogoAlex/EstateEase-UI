package com.example.tenant_care.ui.screens.caretakerViews.meterReading

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
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.FilterByMonthBox
import com.example.tenant_care.util.FilterByRoomNameBox
import com.example.tenant_care.util.FilterByYearBox
import com.example.tenant_care.util.PropertyDataCell
import com.example.tenant_care.util.SearchFieldForTenants
import com.example.tenant_care.util.UndoFilteringBox
import com.example.tenant_care.util.unreadWaterMeterReadings

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotUploadedScreenComposable(
    navigateToEditMeterReadingScreen: (month: String, year: String, propertyName: String, meterTableId: String, childScreen: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: NotUploadedScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    Box(modifier = modifier) {
        NotUploadedScreen(
            searchText = uiState.tenantName,
            onSearchTextChanged = {
                viewModel.updateTenantName(it)
            },
            rooms = uiState.selectableRooms,
            selectedUnitName = uiState.roomName,
            onChangeSelectedUnitName = {
                viewModel.updateRoomName(it)
            },
            years = uiState.years,
            selectedYear = uiState.year,
            onChangeSelectedYear = {
                viewModel.updateYear(it)
            },
            months = uiState.months,
            selectedMonth = uiState.month,
            onChangeSelectedMonth = {
                viewModel.updateMonth(it)
            },
            unfilterUnits = { viewModel.unfilter() },
            numberOfUnits = uiState.meterReadings.size,
            meterReadings = uiState.meterReadings,
            navigateToEditMeterReadingScreen = {propertyName, meterTableId, childScreen ->
                navigateToEditMeterReadingScreen(uiState.month, uiState.year, propertyName, meterTableId, childScreen)
            }
        )
    }
}

@Composable
fun NotUploadedScreen(
    searchText: String?,
    onSearchTextChanged: (newValue: String) -> Unit,
    rooms: List<String>,
    selectedUnitName: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    unfilterUnits: () -> Unit,
    numberOfUnits: Int,
    years: List<String>,
    selectedYear: String,
    onChangeSelectedYear: (year: String) -> Unit,
    months: List<String>,
    selectedMonth: String,
    onChangeSelectedMonth: (month: String) -> Unit,
    meterReadings: List<WaterMeterDt>,
    navigateToEditMeterReadingScreen: (propertyName: String, meterTableId: String, childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        SearchFieldForTenants(
            labelText = "Search Tenant name",
            value = searchText.takeIf { it != null } ?: "",
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
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
        Row {
            FilterByYearBox(
                years = years,
                selectedYear = selectedYear,
                onChangeSelectedYear = onChangeSelectedYear
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterByMonthBox(
                months = months,
                selectedMonth = selectedMonth,
                onChangeSelectedMonth = onChangeSelectedMonth
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$numberOfUnits units",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(meterReadings) {
                PropertyDataCell(
                    waterMeterData = it,
                    navigateToEditMeterReadingScreen = navigateToEditMeterReadingScreen,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotUploadedScreenPreview() {
    Tenant_careTheme {
        NotUploadedScreen(
            searchText = "",
            onSearchTextChanged = {},
            rooms = emptyList(),
            selectedUnitName = "",
            years = emptyList(),
            selectedYear = "",
            onChangeSelectedYear = {},
            months = emptyList(),
            selectedMonth = "",
            onChangeSelectedMonth = {},
            onChangeSelectedUnitName = {},
            unfilterUnits = { /*TODO*/ },
            numberOfUnits = 4,
            meterReadings = unreadWaterMeterReadings,
            navigateToEditMeterReadingScreen = {propertyName, meterTableId, childScreen ->  }
        )
    }
}