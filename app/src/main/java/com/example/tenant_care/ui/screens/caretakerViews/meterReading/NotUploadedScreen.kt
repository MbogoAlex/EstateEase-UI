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
import com.example.tenant_care.util.unreadWaterMeterReadings

@Composable
fun NotUploadedScreenComposable(
    navigateToUploadMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        NotUploadedScreen(
            navigateToUploadMeterReadingScreen = navigateToUploadMeterReadingScreen
        )
    }
}

@Composable
fun NotUploadedScreen(
    navigateToUploadMeterReadingScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        LazyColumn {
            items(unreadWaterMeterReadings) {
                PropertyDataCell(
                    waterMeterData = it,
                    navigateToUploadMeterReadingScreen = navigateToUploadMeterReadingScreen,
                    navigateToUpdateMeterReadingScreen = {},
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
            navigateToUploadMeterReadingScreen = {}
        )
    }
}