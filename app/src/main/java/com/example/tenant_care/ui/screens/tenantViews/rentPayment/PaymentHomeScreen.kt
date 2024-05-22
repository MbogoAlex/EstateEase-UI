package com.example.tenant_care.ui.screens.tenantViews.rentPayment

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.tenant.RentPayment
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentHomeScreenComposable(
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    navigateToTenantReportScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as? Activity)
    BackHandler(onBack = {
        activity?.finish()
    })
    val viewModel: PaymentHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val tenantSince = uiState.userDetails.userAddedAt
    val tenantSinceDateTime = LocalDateTime.parse(tenantSince.ifEmpty { "2024-04-15T18:33:11.927027" }, DateTimeFormatter.ISO_DATE_TIME)
    val currentDateTime = LocalDateTime.now()
    val difference = Duration.between(tenantSinceDateTime, currentDateTime).toDays()

    Box(modifier = modifier) {
        PaymentHomeScreen(
            tenantName = uiState.userDetails.fullName,
            tenantSince = ReusableFunctions.formatDateTimeValue(uiState.userDetails.userAddedAt.ifEmpty { "2024-04-15T18:33:11.927027" }),
            tenancyDays = difference.toString(),
            roomName = uiState.roomName,
            rentPayments = uiState.rentPayments,
            navigateToRentInvoiceScreen = navigateToRentInvoiceScreen,
            navigateToTenantReportScreen = navigateToTenantReportScreen
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentHomeScreen(
    rentPayments: List<RentPayment>,
    roomName: String,
    tenantName: String,
    tenancyDays: String,
    tenantSince: String,
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    navigateToTenantReportScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToTenantReportScreen) {
                Icon(
                    painter = painterResource(id = R.drawable.report),
                    contentDescription = "Payments record"
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                    Text(
                        text = roomName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                            Text(
                                text = tenantName,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if(tenancyDays.toInt() == 1) {
                            Text(text = "$tenancyDays day")
                        } else {
                            Text(text = "$tenancyDays days")
                        }

                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Tenant since: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tenantSince,
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn {
                    items(rentPayments) { rentPayment ->
                        RentStatusCard(
                            navigateToRentInvoiceScreen = navigateToRentInvoiceScreen,
                            rentPayment = rentPayment,
                            modifier = Modifier
                                .padding(
                                    top = 10.dp
                                )
                        )
                    }
                }

//                Spacer(modifier = Modifier.weight(1f))
//                PaymentsReportButton(
//                    navigateToPaymentsReportScreen = {}
//                )
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentStatusCard(
    rentPayment: RentPayment,
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val penaltyAccrued: Double

    val daysLate = rentPayment.daysLate
    val monthlyRent = rentPayment.monthlyRent
    val formattedMonthlyRent = ReusableFunctions.formatMoneyValue(monthlyRent)
    val formattedDueDate = rentPayment.dueDate
    val dailyPenalty = rentPayment.penaltyPerDay
    val formattedDailyPenalty = ReusableFunctions.formatMoneyValue(dailyPenalty)
    if(daysLate > 0) {
        penaltyAccrued = rentPayment.penaltyPerDay * daysLate
    } else {
        penaltyAccrued = 0.0
    }
    val formattedPenaltyAccrued = ReusableFunctions.formatMoneyValue(penaltyAccrued)
    val totalPayable = rentPayment.monthlyRent + penaltyAccrued
    val formattedTotalPayable = ReusableFunctions.formatMoneyValue(totalPayable)
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = rentPayment.month.uppercase(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Payment status:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                if(rentPayment.rentPaymentStatus) {
                    Text(
                        text = "PAID",
                        color = Color.Blue
                    )
                } else if(!rentPayment.rentPaymentStatus) {
                    Text(
                        text = "UNPAID",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            Spacer(modifier = Modifier.height(30.dp))
//            RentStatusCardItem(
//                key = "Water reading: 2 units",
//                value = "Ksh, 300.00"
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            RentStatusCardItem(
//                key = "Dustbin collection:",
//                value = "Ksh, 150.00"
//            )

            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(
                key = "Rent:",
                value = formattedMonthlyRent
            )
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(key = "Due On", value = formattedDueDate)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Penalty status")
                Spacer(modifier = Modifier.weight(1f))
                if(rentPayment.penaltyActive) {
                    Text(
                        text = "ACTIVE",
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "INACTIVE",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(key = "Daily penalty", value = formattedDailyPenalty)
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(key = "Days late", value = rentPayment.daysLate.toString())
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(key = "Penalty accrued", value = formattedPenaltyAccrued)
            Spacer(modifier = Modifier.height(10.dp))
            if(rentPayment.rentPaymentStatus) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total paid",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formattedTotalPayable,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total payable",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formattedTotalPayable,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            RentPaymentButton(
                navigateToRentInvoiceScreen = navigateToRentInvoiceScreen,
                tenantId = rentPayment.tenantId.toString(),
                month = rentPayment.month,
                year = rentPayment.year,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun RentStatusCardItem(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = key)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
        )
    }
}

@Composable
fun RentPaymentButton(
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    tenantId: String,
    month: String,
    year: String,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        onClick = { navigateToRentInvoiceScreen(tenantId, month, year) }
    ) {
        Text(text = "PROCEED TO PAYMENT")
    }
}

@Composable
fun PaymentsReportButton(
    navigateToPaymentsReportScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = navigateToPaymentsReportScreen
    ) {
        Text(text = "Payments record")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TenantRentViewScreenPreview() {
    val rentPayments = mutableListOf<RentPayment>(
        RentPayment(
            rentPaymentTblId = 1,
            dueDate = "2024-09-10 11:44",
            month = "April",
            monthlyRent = 10000.0,
            paidAmount = 10400.0,
            paidAt = LocalDateTime.now().toString(),
            paidLate = true,
            daysLate = 2,
            rentPaymentStatus = true,
            penaltyActive = true,
            penaltyPerDay = 200.0,
            transactionId = "123432111",
            year = "2024",
            propertyNumberOrName = "Col C2",
            numberOfRooms = 2,
            tenantId = 2,
            email = "tenant@gmail.com",
            fullName = "Mbogo AGM",
            nationalIdOrPassport = "234543234",
            phoneNumber = "0119987282",
            tenantAddedAt = "2023-09-23 10:32",
            tenantActive = true
        ),
        RentPayment(
            rentPaymentTblId = 1,
            dueDate = "2024-09-10 11:44",
            month = "April",
            monthlyRent = 10000.0,
            paidAmount = 10400.0,
            paidAt = LocalDateTime.now().toString(),
            paidLate = true,
            daysLate = 2,
            rentPaymentStatus = true,
            penaltyActive = true,
            penaltyPerDay = 200.0,
            transactionId = "123432111",
            year = "2024",
            propertyNumberOrName = "Col C2",
            numberOfRooms = 2,
            tenantId = 2,
            email = "tenant@gmail.com",
            fullName = "Mbogo AGM",
            nationalIdOrPassport = "234543234",
            phoneNumber = "0119987282",
            tenantAddedAt = "2023-09-23 10:32",
            tenantActive = true
        ),
        RentPayment(
            rentPaymentTblId = 1,
            dueDate = "2024-09-10 11:44",
            month = "April",
            monthlyRent = 10000.0,
            paidAmount = 10400.0,
            paidAt = LocalDateTime.now().toString(),
            paidLate = true,
            daysLate = 2,
            rentPaymentStatus = true,
            penaltyActive = true,
            penaltyPerDay = 200.0,
            transactionId = "123432111",
            year = "2024",
            propertyNumberOrName = "Col C2",
            numberOfRooms = 2,
            tenantId = 2,
            email = "tenant@gmail.com",
            fullName = "Mbogo AGM",
            nationalIdOrPassport = "234543234",
            phoneNumber = "0119987282",
            tenantAddedAt = "2023-09-23 10:32",
            tenantActive = true
        )
    )

    Tenant_careTheme {
        PaymentHomeScreen(
            tenancyDays = "20",
            tenantSince = "2024-04-04 1-:32",
            roomName = "Col C2",
            tenantName = "Mbogo AG",
            rentPayments = rentPayments,
            navigateToRentInvoiceScreen = {tenantId, month, year ->  },
            navigateToTenantReportScreen = {}
        )
    }
}