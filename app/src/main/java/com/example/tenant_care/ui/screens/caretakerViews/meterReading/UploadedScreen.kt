package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.PropertyDataCell
import com.example.tenant_care.util.waterMeterReadings

@Composable
fun UploadedScreenComposable(
    navigateToUpdateMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        UploadedScreen(
            navigateToUpdateMeterReadingScreen = navigateToUpdateMeterReadingScreen
        )
    }
}

@Composable
fun UploadedScreen(
    navigateToUpdateMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(waterMeterReadings) {
                PropertyDataCell(
                    waterMeterData = it,
                    navigateToUploadMeterReadingScreen = { /*TODO*/ },
                    navigateToUpdateMeterReadingScreen = navigateToUpdateMeterReadingScreen,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun UploadedScreenPreview() {
    Tenant_careTheme {
        UploadedScreen(
            navigateToUpdateMeterReadingScreen = {}
        )
    }
}