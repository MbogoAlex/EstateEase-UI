package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.pManager.TenantRentPaymentData
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime


object SingleTenantPaymentDetailsComposableDestination: AppNavigation {
    override val title: String = "Single Tenant Payment Details"
    override val route: String = "single-tenant-payment-detail"
    val tenantId: String = "tenantId"
    val month: String = "month"
    val year: String = "year"
    val roomName: String = "roomName"
    val routeWithArgs: String = "$route/{$roomName}/{$tenantId}/{$month}/{$year}"

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleTenantPaymentDetailsComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: SingleTenantPaymentDataScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()


    if(uiState.showPenaltyChangeDialog) {
        Log.i("SHOW_DIALOG_STATE", uiState.showPenaltyChangeDialog.toString())
        ChangePenaltyAmountDialog(
            penaltyPerDay = uiState.newPenaltyPerDay.toString().takeIf { uiState.newPenaltyPerDay != null } ?: uiState.penaltyPerDay.toString(),
            onChangePenaltyAmount = {
                viewModel.changePenaltyAmount(it)
            },
            onDismissRequest = {
                viewModel.dismissPenaltyChange()
                viewModel.togglePenaltyChangeDialog()
            },
            enabled = uiState.penaltyPerDay != uiState.newPenaltyPerDay && uiState.newPenaltyPerDay != null,
            onConfirmButtonClicked = {
                viewModel.activateLatePaymentPenalty(uiState.newPenaltyPerDay.toString())
                viewModel.togglePenaltyChangeDialog()
                if(uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.SUCCESS) {
                    Toast.makeText(context, "Penalty amount changed", Toast.LENGTH_SHORT).show()
                    viewModel.dismissPenaltyChange()
                    Log.i("SHOW_DIALOG_STATE", uiState.showPenaltyChangeDialog.toString())
                    viewModel.resetPenaltySwitchingStatus()

                } else if(uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.FAIL) {
                    Toast.makeText(context, "Failed to change penalty. Try again later", Toast.LENGTH_SHORT).show()
                    viewModel.togglePenaltyChangeDialog()
                    viewModel.resetPenaltySwitchingStatus()
                }
            })
    }


    if(uiState.fetchingStatus == FetchingSingleTenantPaymentStatus.SUCCESS) {
        Log.i("FORMATTED_DATE_IS", uiState.rentPaidOn)
        Box(modifier = modifier) {
            SingleTenantPaymentDetailsScreen(
                month = uiState.month,
                year = uiState.year,
                currentWaterReadingMonth = uiState.waterUnitsCurrentMonth,
                previousWaterReadingMonth = uiState.waterUnitsPreviousMonth,
                totalWaterPrice = uiState.totalWaterPrice!!,
                waterUnits = uiState.waterUnitsConsumed,
                rentPaymentData = uiState.rentPaymentsData.rentpayment[0],
                tenantSince = uiState.tenantAddedAt,
                rentPaymentDueOn = uiState.rentPaymentDueOn,
                paidLate = uiState.rentPaymentsData.rentpayment[0].paidLate.takeIf { it != null } ?: false,
                rentPaid = uiState.rentPaymentsData.rentpayment[0].rentPaymentStatus,
                paidOn = uiState.rentPaidOn,
                penaltyActive = uiState.penaltyActive,
                navigateToPreviousScreen = navigateToPreviousScreen,
                onPenaltyActiveStatusChanged = {
                    Log.i("PENALTY_ACTIVE", it.toString())
                    if(it) {
                        viewModel.activateLatePaymentPenalty(uiState.penaltyPerDay.toString())
                        if(uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.SUCCESS) {
                            Toast.makeText(context, "Penalty activated. Penalty amount: ${ReusableFunctions.formatMoneyValue(uiState.penaltyPerDay)}", Toast.LENGTH_SHORT).show()
                            viewModel.resetPenaltySwitchingStatus()
                        } else if (uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.FAIL) {
                            Toast.makeText(context, "Failed to activate penalty. Try again later", Toast.LENGTH_SHORT).show()
                            viewModel.resetPenaltySwitchingStatus()
                        }
                    }
                    else if(!it) {
                        viewModel.deActivateLatePaymentPenalty()
                        if(uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.SUCCESS) {
                            Toast.makeText(context, "Penalty deactivated", Toast.LENGTH_SHORT).show()
                            viewModel.resetPenaltySwitchingStatus()
                        } else if (uiState.singleTenantPenaltyToggleStatus == SingleTenantPenaltyToggleStatus.FAIL) {
                            Toast.makeText(context, "Failed to deactivate penalty. Try again later", Toast.LENGTH_SHORT).show()
                            viewModel.resetPenaltySwitchingStatus()
                        }

                    }

                },
                onChangePenaltyAmount = {
                    viewModel.togglePenaltyChangeDialog()
                    Log.i("SHOW_DIALOG_STATE", uiState.showPenaltyChangeDialog.toString())
                },
                penaltyPerDay = uiState.penaltyPerDay
            )
        }
    }
    
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleTenantPaymentDetailsScreen(
    month: String?,
    year: String?,
    currentWaterReadingMonth: String,
    previousWaterReadingMonth: String,
    totalWaterPrice: Double,
    waterUnits: Double?,
    rentPaymentData: TenantRentPaymentData,
    rentPaid: Boolean,
    paidLate: Boolean,
    tenantSince: String,
    rentPaymentDueOn: String,
    paidOn: String,
    penaltyPerDay: Double,
    penaltyActive: Boolean,
    navigateToPreviousScreen: () -> Unit,
    onPenaltyActiveStatusChanged: (penaltyActive: Boolean) -> Unit,
    onChangePenaltyAmount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navigateToPreviousScreen() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$month, $year",
                fontWeight = FontWeight.Bold
            )
        }
        if(paidLate) {
            TenantPaidLate(
                currentWaterReadingMonth = currentWaterReadingMonth,
                previousWaterReadingMonth = previousWaterReadingMonth,
                totalWaterPrice = totalWaterPrice,
                waterUnits = waterUnits,
                rentPaymentData = rentPaymentData,
                rentPaymentDueOn = rentPaymentDueOn,
                tenantSince = tenantSince,
                rentPaidOn = paidOn
            )
        } else if(rentPaid && !paidLate) {
            TenantPaid(
                currentWaterReadingMonth = currentWaterReadingMonth,
                previousWaterReadingMonth = previousWaterReadingMonth,
                waterUnits = waterUnits,
                totalWaterPrice = totalWaterPrice,
                rentPaymentData = rentPaymentData,
                tenantSince = tenantSince,
                rentPaidOn = paidOn
            )
        } else if(!rentPaid) {
            TenantNotPaid(
                currentWaterReadingMonth = currentWaterReadingMonth,
                previousWaterReadingMonth = previousWaterReadingMonth,
                waterUnits = waterUnits,
                totalWaterPrice = totalWaterPrice,
                rentPaymentData = rentPaymentData,
                rentPaymentDueOn = rentPaymentDueOn,
                tenantSince = tenantSince,
                penaltyActive = penaltyActive,
                penaltyPerDay = penaltyPerDay,
                onPenaltyActiveStatusChanged = onPenaltyActiveStatusChanged,
                onChangePenaltyAmount = onChangePenaltyAmount
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        GenerateReportButton(
            onGenerateReportButtonClicked = {}
        )
    }
}

@Composable
fun TenantPaid(
    currentWaterReadingMonth: String,
    previousWaterReadingMonth: String,
    totalWaterPrice: Double,
    waterUnits: Double?,
    rentPaymentData: TenantRentPaymentData,
    tenantSince: String,
    rentPaidOn: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = rentPaymentData.propertyNumberOrName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Tenant: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.fullName
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Tenant since: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tenantSince
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Room: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.propertyNumberOrName
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "No.Rooms: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.numberOfRooms
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Monthly Rent: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(rentPaymentData.monthlyRent)
                )
            }
            if(waterUnits != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Water units ($currentWaterReadingMonth): $waterUnits",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = ReusableFunctions.formatMoneyValue(totalWaterPrice)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Payment Status: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PAID",
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Paid on: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaidOn,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun TenantNotPaid(
    currentWaterReadingMonth: String,
    previousWaterReadingMonth: String,
    totalWaterPrice: Double,
    waterUnits: Double?,
    rentPaymentData: TenantRentPaymentData,
    rentPaymentDueOn: String,
    tenantSince: String,
    penaltyActive: Boolean,
    penaltyPerDay: Double,
    onPenaltyActiveStatusChanged: (penaltyActive: Boolean) -> Unit,
    onChangePenaltyAmount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val penaltyAccrued: Double
    val payableAmount: Double
    if(penaltyActive) {
        if(rentPaymentData.daysLate != 0) {
            penaltyAccrued = penaltyPerDay * rentPaymentData.daysLate
        } else {
            penaltyAccrued = 0.0
        }

        
        if(penaltyAccrued != 0.0) {
            payableAmount = rentPaymentData.monthlyRent + penaltyAccrued
        } else {
            payableAmount = rentPaymentData.monthlyRent
        }
    } else if(!penaltyActive) {
        penaltyAccrued = 0.0
        payableAmount = rentPaymentData.monthlyRent
    } else {
        penaltyAccrued = 0.0
        payableAmount = rentPaymentData.monthlyRent
    }

    
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = rentPaymentData.propertyNumberOrName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Tenant: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.fullName
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Tenant since: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tenantSince
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Room: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.propertyNumberOrName
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "No.Rooms: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.numberOfRooms
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Monthly Rent: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(rentPaymentData.monthlyRent)
                )
            }
            if(waterUnits != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Water units ($currentWaterReadingMonth): $waterUnits",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = ReusableFunctions.formatMoneyValue(totalWaterPrice)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Payment Status: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "UNPAID",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Due on: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentDueOn,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Days late: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.daysLate.toString(),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily penalty: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(penaltyPerDay),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    enabled = penaltyActive,
                    onClick = onChangePenaltyAmount
                ) {
                    Text(text = "CHANGE")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Penalty accrued ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(penaltyAccrued),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Total payable amount: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(payableAmount),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            if(penaltyActive) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Deactivate penalty",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    ToggleLatePaymentPenalty(
                        penaltyActive = true,
                        onPenaltyActiveStatusChanged = {
                            onPenaltyActiveStatusChanged(false)
                        }
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Activate penalty",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    ToggleLatePaymentPenalty(
                        penaltyActive = false,
                        onPenaltyActiveStatusChanged = {
                            onPenaltyActiveStatusChanged(true)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Remind tenant")
            }
        }
    }
}

@Composable
fun TenantPaidLate(
    currentWaterReadingMonth: String,
    previousWaterReadingMonth: String,
    totalWaterPrice: Double,
    waterUnits: Double?,
    rentPaymentData: TenantRentPaymentData,
    rentPaymentDueOn: String,
    tenantSince: String,
    rentPaidOn: String,
    modifier: Modifier = Modifier
) {
    val penaltyAccrued: Double
    if(rentPaymentData.daysLate != 0) {
        penaltyAccrued = rentPaymentData.penaltyPerDay * rentPaymentData.daysLate
    } else {
        penaltyAccrued = 0.0
    }

    val payableAmount: Double
    if(penaltyAccrued != 0.0) {
        payableAmount = rentPaymentData.monthlyRent + penaltyAccrued
    } else {
        payableAmount = rentPaymentData.monthlyRent
    }
    Card {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = rentPaymentData.propertyNumberOrName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Tenant: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.fullName
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Tenant since: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tenantSince
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Room: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.propertyNumberOrName
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "No.Rooms: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentData.numberOfRooms
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Monthly Rent: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(rentPaymentData.monthlyRent)
                )
            }
            if(waterUnits != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Water units ($currentWaterReadingMonth): $waterUnits",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = ReusableFunctions.formatMoneyValue(totalWaterPrice)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Payment Status: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PAID LATE",
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Due on: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaymentDueOn,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Paid on: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPaidOn,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Days late: ",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = rentPaymentData.daysLate.toString(),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Daily penalty: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(rentPaymentData.penaltyPerDay),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Penalty accrued ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(penaltyAccrued),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "Total amount paid: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ReusableFunctions.formatMoneyValue(payableAmount),
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light
                )
            }

        }
    }
}

@Composable
fun ToggleLatePaymentPenalty(
    onPenaltyActiveStatusChanged: (checked: Boolean) -> Unit,
    penaltyActive: Boolean,
    modifier: Modifier = Modifier
) {
    Switch(checked = penaltyActive, onCheckedChange = onPenaltyActiveStatusChanged)
}

@Composable
fun ChangePenaltyAmountDialog(
    penaltyPerDay: String,
    onChangePenaltyAmount: (penaltyPerDay: String) -> Unit,
    onDismissRequest: () -> Unit,
    enabled: Boolean,
    onConfirmButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
                Text(text = "Change penalty amount")
        },
        text = {
            OutlinedTextField(
                label = {
                        Text(text = "Daily penalty (ksh)")
                },
                value = penaltyPerDay,
                onValueChange = onChangePenaltyAmount,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                )
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
             Button(
                 enabled = enabled,
                 onClick = onConfirmButtonClicked
             ) {
                 Text(text = "Confirm")
             }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun GenerateReportButton(
    onGenerateReportButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { /*TODO*/ }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Generate report")
            Icon(
                painter = painterResource(id = R.drawable.report),
                contentDescription = "Generate report"
            )
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun SingleTenantPaymentDetailsScreenPreview() {
//    Tenant_careTheme {
//        SingleTenantPaymentDetailsScreen(
//            rentPaymentData = TenantRentPaymentData(
//
//            ),
//            navigateToPreviousScreen = {}
//        )
//    }
//}