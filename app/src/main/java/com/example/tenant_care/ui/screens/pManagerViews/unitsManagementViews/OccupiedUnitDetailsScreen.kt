package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.util.ReusableFunctions

object OccupiedUnitDetailsComposableDestination: AppNavigation {
    override val title: String = "Occupied Unit Details"
    override val route: String = "occupied-unit-details"
    val propertyId: String = "propertyId"
    val routeWithArgs = "$route/{$propertyId}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitDetailsComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToUnoccupiedUnits: (unoccupiedUnits: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewmodel: OccupiedUnitDetailsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewmodel.uiState.collectAsState()

    if(uiState.archivingStatus == ArchivingStatus.SUCCESS) {
        Toast.makeText(context, "Unit archived successfully", Toast.LENGTH_SHORT).show()
        navigateToUnoccupiedUnits("unoccupied-units")
        viewmodel.resetArchivingStatus()
    }

    OccupiedUnitDetailsScreen(
        propertyUnit = uiState.propertyUnit,
        tenant = uiState.tenant,
        tenantAddedAt = uiState.tenantAddedAt,
        propertyAddedAt = uiState.propertyAddedAt,
        navigateToPreviousScreen = { navigateToPreviousScreen() },
        archivingStatus = uiState.archivingStatus,
        archiveUnit = {
            viewmodel.archiveUnit()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitDetailsScreen(
    archivingStatus: ArchivingStatus,
    propertyUnit: PropertyUnit,
    tenant: PropertyTenant,
    tenantAddedAt: String,
    propertyAddedAt: String,
    navigateToPreviousScreen: () -> Unit,
    archiveUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = { navigateToPreviousScreen() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Text(
                    text = propertyUnit.propertyNumberOrName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Uploaded on: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyAddedAt)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "No. Rooms: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyUnit.numberOfRooms.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Monthly rent: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = ReusableFunctions.formatMoneyValue(propertyUnit.monthlyRent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Current tenant: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = tenant.fullName)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Tenant since: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = tenantAddedAt)
                }

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            enabled = archivingStatus != ArchivingStatus.LOADING,
            onClick = archiveUnit,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(archivingStatus == ArchivingStatus.LOADING) {
                CircularProgressIndicator()
            } else {
                Text(text = "Archive unit")
            }

        }

    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun OccupiedUnitsDetailsScreenPreview() {
//    Tenant_careTheme {
//        OccupiedUnitsDetailsScreen()
//    }
//}