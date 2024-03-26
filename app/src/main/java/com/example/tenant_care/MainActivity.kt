package com.example.tenant_care

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tenant_care.pManagerViews.PManagerAddUnitScreen
import com.example.tenant_care.pManagerViews.PManagerHomeScreen
import com.example.tenant_care.pManagerViews.PManagerLoginScreen
import com.example.tenant_care.tenantViews.TenantHomeScreen
import com.example.tenant_care.ui.theme.Tenant_careTheme

class MainActivity : ComponentActivity() {
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
//                    PManagerHomeScreen()
                    PManagerAddUnitScreen()
                }
            }
        }
    }
}
