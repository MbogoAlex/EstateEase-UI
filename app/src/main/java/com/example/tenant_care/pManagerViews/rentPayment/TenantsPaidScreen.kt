package com.example.tenant_care.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.pManager.TenantRentPaymentData
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableComposables
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantsPaidScreenComposable(

    navigateToSingleTenantPaymentDetails: (tenantId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TenantsPaidScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiStatus.collectAsState()

    var showMenuPopup by remember {
        mutableStateOf(false)
    }

    val popUpItems = listOf<String>("Active tenants", "Removed tenants")

    Box(
        modifier = modifier
    ) {
        TenantsPaidScreen(
            activeTenantsSelected = uiState.activeTenantsSelected,
            inActiveTenantsSelected = uiState.inactiveTenantsSelected,
            tenantName = uiState.tenantName,
            onSearchTextChanged = {
                viewModel.filterByTenantName(
                    tenantName = it,
                )
            },
            numberOfRoomsSelected = uiState.selectedNumOfRooms,
            onSelectNumOfRooms = {
                viewModel.filterByNumberOfRooms(
                    selectedNumOfRooms = it.toString(),
                )
            },
            rooms = uiState.rooms,
            selectedUnitName = uiState.selectedUnitName,
            onChangeSelectedUnitName = {
                viewModel.filterByUnitName(
                    roomName = it,
                )
            },
            unfilterUnits = {
                viewModel.unfilterUnits()
            },
            numberOfUnits = uiState.rentPaymentsData.rentpayment.size,
            rentPayments = uiState.rentPaymentsData.rentpayment,
            paidAt = uiState.rentPaidAt,
            navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
            onDismissRequest = {
                showMenuPopup = !showMenuPopup
            },
            onMenuButtonClicked = {
                showMenuPopup = !showMenuPopup
            },
            showMenuPopup = showMenuPopup,
            popUpItems = popUpItems,
            onPopupMenuItemClicked = {item ->
                if(item == "Active tenants") {
                    viewModel.filterByActiveTenants(true)
                } else if(item == "Removed tenants") {
                    viewModel.filterByActiveTenants(false)
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantsPaidScreen(
    activeTenantsSelected: Boolean,
    inActiveTenantsSelected: Boolean,
    tenantName: String?,
    onSearchTextChanged: (searchText: String) -> Unit,
    numberOfRoomsSelected: String?,
    onSelectNumOfRooms: (rooms: Int) -> Unit,
    rooms: List<String>,
    selectedUnitName: String?,
    onChangeSelectedUnitName: (unitName: String) -> Unit,
    unfilterUnits: () -> Unit,
    numberOfUnits: Int?,
    paidAt: String,
    rentPayments: List<TenantRentPaymentData>,
    navigateToSingleTenantPaymentDetails: (tenantId: String) -> Unit,
    onPopupMenuItemClicked: (item: String) -> Unit,
    onMenuButtonClicked: () -> Unit,
    showMenuPopup: Boolean,
    onDismissRequest: () -> Unit,
    popUpItems: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ReusableComposables.SearchFieldForTenants(
            labelText = "Search Tenant name",
            value = tenantName.takeIf { it != null } ?: "",
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReusableComposables.FilterByNumOfRoomsBox(
                selectedNumOfRooms = numberOfRoomsSelected,
                onSelectNumOfRooms = onSelectNumOfRooms
            )
            Spacer(modifier = Modifier.width(10.dp))
            ReusableComposables.FilterByRoomNameBox(
                rooms = rooms,
                selectedUnit = selectedUnitName,
                onChangeSelectedUnitName = onChangeSelectedUnitName
            )
            Spacer(modifier = Modifier.weight(1f))
            ReusableComposables.UndoFilteringBox(
                unfilterUnits = unfilterUnits
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$numberOfUnits units",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(5.dp))
            if(activeTenantsSelected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = Color.Green,
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Active tenants")
                }

            } else if(inActiveTenantsSelected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = Color.Red,
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Removed tenants")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ElevatedCard {
                if(showMenuPopup) {
                    PopupMenu(
                        onDismissRequest = onDismissRequest,
                        popupMenuItems = popUpItems,
                        onPopupMenuItemClicked = onPopupMenuItemClicked
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            modifier = Modifier,
//                            .padding(10.dp),
                            onClick = onMenuButtonClicked
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = null,
                            )
                        }
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(rentPayments.size) {
                IndividualTenantPaidCell(
                    rentPayment = rentPayments[it],
                    paymentStatus = rentPayments[it].rentPaymentStatus,
                    paidLate = rentPayments[it].paidLate.takeIf { paidLate ->  paidLate != null } ?: false,
                    navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails,
                    paidAt = paidAt,
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
fun IndividualTenantPaidCell(
    rentPayment: TenantRentPaymentData,
    paymentStatus: Boolean,
    paidLate: Boolean,
    paidAt: String,
    navigateToSingleTenantPaymentDetails: (tenantId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToSingleTenantPaymentDetails(rentPayment.tenantId!!.toString())
            }
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPayment.propertyNumberOrName,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "No.Rooms: ")
                Text(
                    text = rentPayment.numberOfRooms.toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tenant: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPayment.fullName
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly rent: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(rentPayment.monthlyRent)
                )



            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                if(paymentStatus && !paidLate) {
                    Text(
                        text = "PAID",
                        fontWeight = FontWeight.Bold,
                        color = Color.Green,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "On: $paidAt",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                } else if(!paymentStatus) {
                    Text(
                        text = "UNPAID",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Due on: ${rentPayment.dueDate}",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )

                }

            }
            if(paymentStatus && paidLate) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Column {
                        Text(
                            text = "PAID LATE",
                            fontWeight = FontWeight.Bold,
                            color = Color.Blue,
                            fontSize = 18.sp
                        )

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "On: ${ReusableFunctions.formatDateTimeValue(rentPayment.paidAt!!)}",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Text(text = "${rentPayment.daysLate} days")
                    Spacer(modifier = Modifier.width(10.dp))
                    Row {
                        Text(text = "Penalty: ")
                        Text(
                            text = ReusableFunctions.formatMoneyValue(rentPayment.penaltyPerDay * rentPayment.daysLate),
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
            if(!paymentStatus) {
                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Text(text = "0 days")
                    Spacer(modifier = Modifier.width(10.dp))
                    Row {
                        Text(text = "Penalty: ")
                        Text(
                            text = ReusableFunctions.formatMoneyValue(0.0),
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navigateToSingleTenantPaymentDetails(rentPayment.tenantId!!.toString())
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "More",
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "More"
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun IndividualTenantPaidCellPreview() {
    val rentPayments: List<TenantRentPaymentData> = emptyList()
    Tenant_careTheme {
        IndividualTenantPaidCell(
            rentPayment = rentPayments[0],
            paymentStatus = false,
            paidLate = true,
            paidAt = "",
            navigateToSingleTenantPaymentDetails = {}
        )
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun AllTenantsPaidScreenPreview() {
//    val rentPayments: List<TenantRentPaymentData> = emptyList()
//    Tenant_careTheme {
//        TenantsPaidScreen(
//            rentPayments = rentPayments,
//            navigateToSingleTenantPaymentDetails = {}
//        )
//    }
//}