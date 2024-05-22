package com.example.tenant_care.ui.screens.pManagerViews.tenantsManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun AddTenantsScreenViewModelScreen() {

}

@Composable
fun AddTenantScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun AddTenantScreenPreview() {
    Tenant_careTheme {
        AddTenantScreen()
    }
}