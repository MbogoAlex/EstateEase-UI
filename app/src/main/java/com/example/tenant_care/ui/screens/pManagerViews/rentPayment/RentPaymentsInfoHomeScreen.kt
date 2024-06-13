package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.EditAlertDialog
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.ExpenseUpdateStatus
import com.example.tenant_care.util.FilterByMonthBox
import com.example.tenant_care.util.FilterByYearBox
import com.example.tenant_care.util.PenaltyUpdateStatus
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsInfoHomeScreenComposable(
    navigateToRentPaymentsScreen: (month: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: RentPaymentsInfoHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showChangePenaltyStatusDialog by remember {
        mutableStateOf(false)
    }

    var showEditPenaltyAmountDialog by remember {
        mutableStateOf(false)
    }

    var showEditPricePerWaterUnit by remember {
        mutableStateOf(false)
    }

    var showChangeDurationDialog by remember {
        mutableStateOf(false)
    }


    if(uiState.executionStatus == ExecutionStatus.SUCCESS) {
        Toast.makeText(context, "Late rent payment penalty status changed", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    } else if(uiState.executionStatus == ExecutionStatus.FAILURE) {
        Toast.makeText(context, "Failed. Check your connection", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    }

    if(uiState.penaltyUpdateStatus == PenaltyUpdateStatus.SUCCESS) {
        Toast.makeText(context, "Late rent payment penalty amount changed", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    } else if(uiState.penaltyUpdateStatus == PenaltyUpdateStatus.FAILURE) {
        Toast.makeText(context, "Failed. Check your connection", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    }

    if(uiState.expenseUpdateStatus == ExpenseUpdateStatus.SUCCESS) {
        Toast.makeText(context, "Price per water unit changed. The change will take effect next month", Toast.LENGTH_LONG).show()
        viewModel.resetExecutionStatus()
    } else if(uiState.expenseUpdateStatus == ExpenseUpdateStatus.FAILURE) {
        Toast.makeText(context, "Failed. Check your connection", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    }

    if(showChangePenaltyStatusDialog) {
        if(uiState.penaltyStatus) {
            EditAlertDialog(
                title = "Deactivate late rent payment penalty",
                onConfirm = {
                    showChangePenaltyStatusDialog = !showChangePenaltyStatusDialog
                    viewModel.deActivateLatePaymentPenalty()
                },
                onDismissRequest = { showChangePenaltyStatusDialog = !showChangePenaltyStatusDialog }
            )
        } else {
            EditAlertDialog(
                title = "Activate late rent payment penalty",
                onConfirm = {
                    showChangePenaltyStatusDialog = !showChangePenaltyStatusDialog
                    viewModel.activateLatePaymentPenalty()
                },
                onDismissRequest = { showChangePenaltyStatusDialog = !showChangePenaltyStatusDialog }
            )
        }
    }

    if(showEditPenaltyAmountDialog) {
        EditAmountAlertDialog(
            title = "Edit penalty amount",
            amount = uiState.newPenaltyAmount,
            label = "Amount",
            onValueChange = {
                viewModel.updatePenaltyAmount(it)
            },
            onConfirm = {
                showEditPenaltyAmountDialog = !showEditPenaltyAmountDialog
                viewModel.updatePenalty()
            },
            onDismissRequest = {
                showEditPenaltyAmountDialog = !showEditPenaltyAmountDialog
                viewModel.resetPenaltyAmount()
            }
        )
    }

    if(showEditPricePerWaterUnit) {
        EditAmountAlertDialog(
            title = "Edit price per water unit",
            amount = uiState.newPricePerWaterUnit,
            label = "Amount",
            onValueChange = {
                viewModel.updatePricePerWaterUnit(it)
            },
            onConfirm = {
                showEditPricePerWaterUnit = !showEditPricePerWaterUnit
                viewModel.savePricePerWaterUnit()
            },
            onDismissRequest = {
                showEditPricePerWaterUnit = !showEditPricePerWaterUnit
                viewModel.resetNewPricePerWaterUnit()
            }
        )
    }



    if(showChangeDurationDialog) {
        ChangeDurationDialog(
            onConfirm = {
                showChangeDurationDialog = !showChangeDurationDialog
                viewModel.fetchRentOverview()
            },
            onDismissRequest = {
                showChangeDurationDialog = !showChangeDurationDialog
            },
            years = uiState.years,
            selectedYear = uiState.year,
            selectedMonth = uiState.month,
            onChangeSelectedYear = {
                viewModel.updateYear(it)
            },
            onChangeSelectedMonth = {
                viewModel.updateMonth(it)
            },
            months = uiState.months
        )
    }

    Box {
        RentPaymentsInfoHomeScreen(
            penaltyButtonEnabled = uiState.penaltyButtonEnabled,
            waterUnitsButtonEnabled = uiState.waterUnitButtonEnabled,
            month = uiState.month,
            year = uiState.year,
            penaltyActive = uiState.penaltyStatus,
            penaltyAmount = uiState.penaltyAmount,
            pricePerWaterUnit = uiState.pricePerWaterUnit,
            pManagerName = uiState.userDSDetails.fullName,
            totalUnits = uiState.estateEaseRentOverview.totalUnits,
            expectedRent = uiState.estateEaseRentOverview.totalExpectedRent,
            paidAmount = uiState.estateEaseRentOverview.paidAmount,
            clearedUnits = uiState.estateEaseRentOverview.clearedUnits,
            deficit = uiState.estateEaseRentOverview.deficit,
            unclearedUnits = uiState.estateEaseRentOverview.unclearedUnits,
            executionStatus = uiState.executionStatus,
            penaltyUpdateStatus = uiState.penaltyUpdateStatus,
            expenseUpdateStatus = uiState.expenseUpdateStatus,
            onPenaltyStatusChanged = {
                showChangePenaltyStatusDialog = !showChangePenaltyStatusDialog
            },
            onEditPenaltyAmount = {
                showEditPenaltyAmountDialog = !showEditPenaltyAmountDialog
            },
            onEditPricePerWaterUnit = {
                showEditPricePerWaterUnit = !showEditPricePerWaterUnit
            },
            navigateToRentPaymentsScreen = navigateToRentPaymentsScreen,
            onChangeDuration = {
                showChangeDurationDialog = !showChangeDurationDialog
            },
            onReset = {
                viewModel.unfilter()
            },
            navigateToAddUnitScreen = {}
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsInfoHomeScreen(
    penaltyButtonEnabled: Boolean,
    waterUnitsButtonEnabled: Boolean,
    month: String,
    year: String,
    penaltyActive: Boolean,
    penaltyAmount: Double,
    pricePerWaterUnit: Double,
    pManagerName: String,
    totalUnits: Int,
    expectedRent: Double,
    paidAmount: Double,
    clearedUnits: Int,
    deficit: Double,
    unclearedUnits: Int,
    onEditPenaltyAmount: () -> Unit,
    onEditPricePerWaterUnit: () -> Unit,
    executionStatus: ExecutionStatus,
    penaltyUpdateStatus: PenaltyUpdateStatus,
    expenseUpdateStatus: ExpenseUpdateStatus,
    onPenaltyStatusChanged: (status: Boolean) -> Unit,
    onChangeDuration: () -> Unit,
    onReset: () -> Unit,
    navigateToRentPaymentsScreen: (month: String, year: String) -> Unit,
    navigateToAddUnitScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        RentPaymentCard(
            penaltyButtonEnabled = penaltyButtonEnabled,
            waterUnitsButtonEnabled = waterUnitsButtonEnabled,
            month = month,
            year = year,
            totalUnits = totalUnits,
            expectedRent = expectedRent,
            paidAmount = paidAmount,
            clearedUnits = clearedUnits,
            deficit = deficit,
            unclearedUnits = unclearedUnits,
            onChangeDuration = onChangeDuration,
            onReset = onReset,
            navigateToRentPaymentsScreen = navigateToRentPaymentsScreen
        )
        Spacer(modifier = Modifier.height(20.dp))
        AdditionalPayments(
            penaltyButtonEnabled = penaltyButtonEnabled,
            waterUnitsButtonEnabled = waterUnitsButtonEnabled,
            penaltyActive = penaltyActive,
            penaltyAmount = penaltyAmount,
            pricePerWaterUnit = pricePerWaterUnit,
            penaltyUpdateStatus = penaltyUpdateStatus,
            executionStatus = executionStatus,
            expenseUpdateStatus = expenseUpdateStatus,
            onEditPenaltyAmount = onEditPenaltyAmount,
            onPenaltyStatusChanged = onPenaltyStatusChanged,
            onEditPricePerWaterUnit = onEditPricePerWaterUnit,
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentCard(
    penaltyButtonEnabled: Boolean,
    waterUnitsButtonEnabled: Boolean,
    month: String,
    year: String,
    totalUnits: Int,
    expectedRent: Double,
    paidAmount: Double,
    clearedUnits: Int,
    deficit: Double,
    unclearedUnits: Int,
    onChangeDuration: () -> Unit,
    onReset: () -> Unit,
    navigateToRentPaymentsScreen: (month: String, year: String) -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$month, $year",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onChangeDuration) {
                    Text(text = "Change")
                }
                if(!penaltyButtonEnabled && !waterUnitsButtonEnabled) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onReset) {
                        Text(text = "Reset")
                    }
                }

            }
            Row {
                Text(text = "Occupied units: $totalUnits")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "RENT:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "UNITS:",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Expected:",
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .clickable {  }
            ) {

                Text(
                    text = ReusableFunctions.formatMoneyValue(expectedRent),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        color = Color.Green
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$totalUnits units")
                Icon(
                    painter = painterResource(id = R.drawable.navigate_next),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Paid:",
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .clickable {  }
            ) {
                Text(
                    text = ReusableFunctions.formatMoneyValue(paidAmount),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$clearedUnits units")
                Icon(
                    painter = painterResource(id = R.drawable.navigate_next),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Deficit:",
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .clickable {  }
            ) {
                Text(
                    text = ReusableFunctions.formatMoneyValue(deficit),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        color = Color.Red
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$unclearedUnits units")
                Icon(
                    painter = painterResource(id = R.drawable.navigate_next),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { navigateToRentPaymentsScreen(month, year) },
                modifier = Modifier
                    .widthIn(250.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "More Details")
            }
        }
    }
}

@Composable
fun AdditionalPayments(
    penaltyButtonEnabled: Boolean,
    waterUnitsButtonEnabled: Boolean,
    penaltyActive: Boolean,
    penaltyAmount: Double,
    pricePerWaterUnit: Double,
    executionStatus: ExecutionStatus,
    penaltyUpdateStatus: PenaltyUpdateStatus,
    expenseUpdateStatus: ExpenseUpdateStatus,
    onEditPenaltyAmount: () -> Unit,
    onEditPricePerWaterUnit: () -> Unit,
    onPenaltyStatusChanged: (status: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "Penalty status")
                Spacer(modifier = Modifier.weight(1f))
                if(executionStatus == ExecutionStatus.LOADING) {
                    CircularProgressIndicator()
                } else {
                    Switch(
                        enabled = penaltyButtonEnabled,
                        checked = penaltyActive,
                        onCheckedChange = onPenaltyStatusChanged
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "Penalty amount")
                Spacer(modifier = Modifier.weight(1f))
                if(penaltyUpdateStatus == PenaltyUpdateStatus.LOADING) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = ReusableFunctions.formatMoneyValue(penaltyAmount))
                        Spacer(modifier = Modifier.width(3.dp))
                        IconButton(
                            enabled = penaltyActive && penaltyButtonEnabled,
                            onClick = onEditPenaltyAmount
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit), 
                                contentDescription = "Edit penalty amount"
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "Price/water unit")
                Spacer(modifier = Modifier.weight(1f))
                if(expenseUpdateStatus == ExpenseUpdateStatus.LOADING) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = ReusableFunctions.formatMoneyValue(pricePerWaterUnit))
                        Spacer(modifier = Modifier.width(3.dp))
                        IconButton(
                            enabled = waterUnitsButtonEnabled,
                            onClick = onEditPricePerWaterUnit
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = "Edit price per water unit"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditAmountAlertDialog(
    title: String,
    amount: String,
    label: String,
    onValueChange: (newValue: String) -> Unit,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Column {
                TextField(
                    value = amount,
                    label = {
                        Text(text = label)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Decimal
                    ),
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                )

            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun ChangeDurationDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    years: List<String>,
    selectedYear: String,
    selectedMonth: String,
    onChangeSelectedYear: (year: String) -> Unit,
    onChangeSelectedMonth: (month: String) -> Unit,
    months: List<String>,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        text = {
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
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = { 
            Button(onClick = onConfirm) {
                Text(text = "Filter")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RentPaymentsInfoHomeScreenPreview() {
    Tenant_careTheme {
        RentPaymentsInfoHomeScreen(
            month = "JANUARY",
            year = "2024",
            penaltyActive = true,
            penaltyAmount = 0.0,
            pricePerWaterUnit = 0.0,
            pManagerName = "Alex Mbogo",
            totalUnits = 10,
            expectedRent = 10000.0,
            paidAmount = 5000.0,
            clearedUnits = 5,
            deficit = 5000.0,
            unclearedUnits = 5,
            executionStatus = ExecutionStatus.INITIAL,
            penaltyUpdateStatus = PenaltyUpdateStatus.INITIAL,
            expenseUpdateStatus = ExpenseUpdateStatus.INITIAL,
            onPenaltyStatusChanged = {},
            navigateToRentPaymentsScreen = { month, year ->  },
            onEditPenaltyAmount = {},
            onEditPricePerWaterUnit = {},
            onChangeDuration = {},
            onReset = {},
            penaltyButtonEnabled = true,
            waterUnitsButtonEnabled = true,
            navigateToAddUnitScreen = { /*TODO*/ }
        )
    }
}