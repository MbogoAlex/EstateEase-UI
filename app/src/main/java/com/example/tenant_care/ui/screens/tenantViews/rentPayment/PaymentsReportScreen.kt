package com.example.tenant_care.ui.screens.tenantViews.rentPayment

import android.os.Build
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.DownloadingStatus
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime

object PaymentsReportScreenDestination: AppNavigation {
    override val title: String = "Tenant Report Screen"
    override val route: String = "tenant-report-screen"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentsReportScreenComposable(
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = LocalContext.current as ComponentActivity
    val viewModel: PaymentsReportScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    Box {
        PaymentsReportScreen(
            rentPayments = uiState.rentPayments,
            downloadingStatus = uiState.downloadingStatus,
            downloadReport = {
                viewModel.fetchReport(
                    month = null,
                    year = null,
                    roomName = null,
                    rooms = null,
                    tenantName = null,
                    rentPaymentStatus = null,
                    paidLate = null,
                    tenantActive = null,
                    context = context
                )
            },
            navigateToRentInvoiceScreen = navigateToRentInvoiceScreen
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentsReportScreen(
    rentPayments: List<RentPayment>,
    downloadingStatus: DownloadingStatus,
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    downloadReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
//        floatingActionButton = {
//
//            FloatingActionButton(
//
//                onClick = { /*TODO*/ }
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.download),
//                    contentDescription = "Download report"
//                )
//            }
//
//        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transactions",
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    enabled = rentPayments.isNotEmpty() && downloadingStatus != DownloadingStatus.LOADING,
                    onClick = downloadReport
                ) {
                    if(downloadingStatus == DownloadingStatus.LOADING) {
                        CircularProgressIndicator()
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.download),
                                contentDescription = "Download statement"
                            )
                            Text(text = "Statement")
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { /*TODO*/ }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date range"
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Change date range")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "January to December, 2024")
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(rentPayments) { rentPayment ->
                    TransactionItem(
                        rentPayment = rentPayment,
                        navigateToRentInvoiceScreen = navigateToRentInvoiceScreen
                    )
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionItem(
    rentPayment: RentPayment,
    navigateToRentInvoiceScreen: (tenantId: String, month: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val paidAt = ReusableFunctions.formatDateTimeValue(rentPayment.paidAt!!)
    val amountPaid = ReusableFunctions.formatMoneyValue(rentPayment.paidAmount!!)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Amount paid",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = amountPaid.toString()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Column {
                    Text(
                        text = "Date time:",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = paidAt!!,
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    shape = RoundedCornerShape(10.dp),
                    onClick = { 
                        navigateToRentInvoiceScreen(
                            rentPayment.tenantId.toString(),
                            rentPayment.month,
                            rentPayment.year
                        )
                    }
                ) {
                    Text(text = "BREAKDOWN")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    val rentPayment = RentPayment(
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
        tenantActive = true,
        waterUnits = 2.0,
        meterReadingDate = "2024-04-02T17:42:24.352844",
        imageFile = "",
        pricePerUnit = 150.0
    )
    Tenant_careTheme {
        TransactionItem(
            rentPayment = rentPayment,
            navigateToRentInvoiceScreen = {tenantId, month, year ->  }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TenantReportScreenPreview() {
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
            tenantActive = true,
            waterUnits = 2.0,
            meterReadingDate = "2024-04-02T17:42:24.352844",
            imageFile = "",
            pricePerUnit = 150.0
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
            tenantActive = true,
            waterUnits = 2.0,
            meterReadingDate = "2024-04-02T17:42:24.352844",
            imageFile = "",
            pricePerUnit = 150.0
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
            tenantActive = true,
            waterUnits = 2.0,
            meterReadingDate = "2024-04-02T17:42:24.352844",
            imageFile = "",
            pricePerUnit = 150.0
        )
    )
    Tenant_careTheme {
        PaymentsReportScreen(
            rentPayments = rentPayments,
            downloadingStatus = DownloadingStatus.INITIAL,
            downloadReport = {},
            navigateToRentInvoiceScreen = {tenantId, month, year ->  }
        )
    }
}