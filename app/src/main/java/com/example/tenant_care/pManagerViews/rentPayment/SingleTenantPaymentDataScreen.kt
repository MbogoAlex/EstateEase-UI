package com.example.tenant_care.pManagerViews.rentPayment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
    val routeWithArgs: String = "$route/{$tenantId}"

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleTenantPaymentDetailsComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SingleTenantPaymentDataScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()


    if(uiState.fetchingStatus == FetchingSingleTenantPaymentStatus.SUCCESS) {
        Box(modifier = modifier) {
            SingleTenantPaymentDetailsScreen(
                rentPaymentData = uiState.rentPaymentsData.rentpayment[0],
                tenantSince = uiState.tenantAddedAt,
                rentPaymentDueOn = uiState.rentPaymentDueOn,
                paidLate = uiState.rentPaymentsData.rentpayment[0].paidLate.takeIf { it != null } ?: false,
                rentPaid = uiState.rentPaymentsData.rentpayment[0].rentPaymentStatus,
                paidOn = uiState.rentPaymentsData.rentpayment[0].paidAt.takeIf { it != null } ?: "",
                navigateToPreviousScreen = navigateToPreviousScreen
            )
        }
    }
    
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleTenantPaymentDetailsScreen(
    rentPaymentData: TenantRentPaymentData,
    rentPaid: Boolean,
    paidLate: Boolean,
    tenantSince: String,
    rentPaymentDueOn: String,
    paidOn: String,

    navigateToPreviousScreen: () -> Unit,
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
                text = LocalDateTime.now().month.toString(),
                fontWeight = FontWeight.Bold
            )
        }
        if(paidLate) {
            TenantPaidLate(
                rentPaymentData = rentPaymentData,
                rentPaymentDueOn = rentPaymentDueOn,
                tenantSince = tenantSince
            )
        } else if(rentPaid && !paidLate) {
            TenantPaid(
                rentPaymentData = rentPaymentData,
                tenantSince = tenantSince,
                rentPaidOn = rentPaymentDueOn
            )
        } else if(!rentPaid) {
            TenantNotPaid(
                rentPaymentData = rentPaymentData,
                rentPaymentDueOn = rentPaymentDueOn,
                tenantSince = tenantSince
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
                    text = rentPaymentData.numberOfRooms.toString()
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
    rentPaymentData: TenantRentPaymentData,
    rentPaymentDueOn: String,
    tenantSince: String,
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
                text = "Col C2",
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
                    text = rentPaymentData.numberOfRooms.toString()
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
    rentPaymentData: TenantRentPaymentData,
    rentPaymentDueOn: String,
    tenantSince: String,
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
                    text = rentPaymentData.numberOfRooms.toString()
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