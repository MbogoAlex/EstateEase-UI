package com.example.tenant_care

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tenant_care.ui.screens.account.LoginScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.CaretakerHomeScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.meterReading.EditMeterReadingScreenComposable
import com.example.tenant_care.ui.screens.caretakerViews.units.OccupiedUnitsScreenComposable
import com.example.tenant_care.ui.theme.Tenant_careTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tenant_careTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    TenantHomeScreen()
//                    PManagerLoginScreen()
                    EstateEaseApp()
//                    CaretakerHomeScreenComposable()
//                    EditMeterReadingScreenComposable()
//                    OccupiedUnitsScreenComposable()
//                    LoginScreenComposable()
//                 a   TenantsManagementScreen()
//                    PManagerAddUnitScreen()
//                    UnitsManagementComposable()
                }
            }
        }
    }
}
