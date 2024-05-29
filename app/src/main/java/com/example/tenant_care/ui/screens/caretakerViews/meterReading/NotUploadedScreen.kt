package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.PropertyDataCell
import com.example.tenant_care.util.unreadWaterMeterReadings

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotUploadedScreenComposable(
    navigateToEditMeterReadingScreen: (meterTableId: String, childScreen: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: NotUploadedScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    Box(modifier = modifier) {
        NotUploadedScreen(
            meterReadings = uiState.meterReadings,
            navigateToEditMeterReadingScreen = navigateToEditMeterReadingScreen
        )
    }
}

@Composable
fun NotUploadedScreen(
    meterReadings: List<WaterMeterDt>,
    navigateToEditMeterReadingScreen: (meterTableId: String, childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
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
            meterReadings = unreadWaterMeterReadings,
            navigateToEditMeterReadingScreen = {meterTableId, childScreen ->  }
        )
    }
}