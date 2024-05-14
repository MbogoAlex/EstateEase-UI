package com.example.tenant_care.tenantViews.rentPayment

import android.os.Build
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.tenant.RentPayment
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

object RentInvoiceScreenDestination: AppNavigation {
    override val title: String = "Invoice Screen"
    override val route: String = "invoice-screen"
    val tenantId: String = "tenantId"
    val month: String = "month"
    val year: String = "year"
    val routeWithArgs: String = "$route/{$tenantId}/{$month}/{$year}"

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentInvoiceScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToTenantHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: RentInvoiceScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    val penaltyAccrued: Double
    val formattedPaidAt: String

    val daysLate = uiState.rentPayment.daysLate
    val monthlyRent = uiState.rentPayment.monthlyRent
    val formattedMonthlyRent = ReusableFunctions.formatMoneyValue(monthlyRent)
    val formattedDueDate = uiState.rentPayment.dueDate
    val dailyPenalty = uiState.rentPayment.penaltyPerDay
    val formattedDailyPenalty = ReusableFunctions.formatMoneyValue(dailyPenalty)
    if(daysLate > 0) {
        penaltyAccrued = uiState.rentPayment.penaltyPerDay * daysLate
    } else {
        penaltyAccrued = 0.0
    }
    val formattedPenaltyAccrued = ReusableFunctions.formatMoneyValue(penaltyAccrued)
    val totalPayable = uiState.rentPayment.monthlyRent + penaltyAccrued
    val formattedTotalPayable = ReusableFunctions.formatMoneyValue(totalPayable)



    if(uiState.payRentStatus == PayRentStatus.SUCCESS) {
        Toast.makeText(context, "Rent paid successfully", Toast.LENGTH_SHORT).show()
        navigateToTenantHomeScreen()
        viewModel.resetRentPaymentStatus()
    } else if(uiState.payRentStatus == PayRentStatus.FAILURE) {
        Toast.makeText(context, "Failed to pay rent. Try again later", Toast.LENGTH_LONG).show()
        viewModel.resetRentPaymentStatus()
    }

    Box {
        RentInvoiceScreen(
            rentPayment = uiState.rentPayment,
            tenantName = uiState.userDetails.fullName,
            payRent = {
                 viewModel.payRent(totalPayable)
            },
            daysLate = daysLate,
            formattedDailyPenalty = formattedDailyPenalty,
            formattedMonthlyRent = formattedMonthlyRent,
            formattedPenaltyAccrued = formattedPenaltyAccrued,
            formattedTotalPayable = formattedTotalPayable,
            payRentStatus = uiState.payRentStatus,
            rentPaid = uiState.rentPayment.rentPaymentStatus,
            paidAt = uiState.paidAt,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentInvoiceScreen(
    navigateToPreviousScreen: () -> Unit,
    rentPayment: RentPayment,
    tenantName: String,
    daysLate: Int,
    formattedDailyPenalty: String,
    formattedMonthlyRent: String,
    formattedPenaltyAccrued: String,
    formattedTotalPayable: String,
    rentPaid: Boolean,
    paidAt: String,
    payRent: () -> Unit,
    payRentStatus: PayRentStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate to previous screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${rentPayment.month.uppercase()}, ${rentPayment.year.uppercase()}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PaymentInvoice(
            tenantName = tenantName,
            daysLate = daysLate,
            formattedDailyPenalty = formattedDailyPenalty,
            formattedMonthlyRent = formattedMonthlyRent,
            formattedPenaltyAccrued = formattedPenaltyAccrued,
            formattedTotalPayable = formattedTotalPayable,
            paidAt = paidAt,
            rentPayment = rentPayment
        )
        Spacer(modifier = Modifier.weight(1f))
        PayRentButton(
            payRent = payRent,
            rentPaid = rentPaid,
            payRentStatus = payRentStatus
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentInvoice(
    rentPayment: RentPayment,
    tenantName: String,
    daysLate: Int,
    formattedDailyPenalty: String,
    formattedMonthlyRent: String,
    formattedPenaltyAccrued: String,
    formattedTotalPayable: String,
    paidAt: String,
    modifier: Modifier = Modifier
) {



    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "INVOICE",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPayment.propertyNumberOrName,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "No.Rooms: ${rentPayment.numberOfRooms}")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tenant name: ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = tenantName)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly rent: ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = formattedMonthlyRent)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment Status: ",
                    fontWeight = FontWeight.Bold
                )
                if(rentPayment.rentPaymentStatus) {
                    Text(
                        text = "PAID",
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "UNPAID",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due date: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPayment.dueDate,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Days late: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = daysLate.toString(),
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Penalty status: ",
                    fontWeight = FontWeight.Bold
                )
                if(rentPayment.penaltyActive) {
                    Text(text = "ACTIVE")
                } else {
                    Text(text = "INACTIVE")
                }

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
                    text = formattedDailyPenalty,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Penalty Accrued: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedPenaltyAccrued,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(rentPayment.rentPaymentStatus) {
                    Text(
                        text = "Total amount paid: ",
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Total payable: ",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formattedTotalPayable,
                    fontStyle = FontStyle.Italic
                )
            }
            if(rentPayment.rentPaymentStatus) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Paid at: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = paidAt,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun PayRentButton(
    payRent: () -> Unit,
    payRentStatus: PayRentStatus,
    rentPaid: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = payRentStatus != PayRentStatus.LOADING && !rentPaid,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = payRent
    ) {
        if(payRentStatus == PayRentStatus.LOADING) {
            CircularProgressIndicator()
        } else {
            Text(text = "Pay rent")
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
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
        tenantActive = true
    )
    Tenant_careTheme {
        RentInvoiceScreen(
            tenantName = "Alex AG",
            rentPayment = rentPayment,
            payRent = {},
            formattedTotalPayable = "Ksh12,000",
            formattedMonthlyRent = "Ksh10,000",
            formattedDailyPenalty = "Ksh200",
            formattedPenaltyAccrued = "Ksh2,000",
            daysLate = 10,
            payRentStatus = PayRentStatus.INITIAL,
            navigateToPreviousScreen = {},
            paidAt = "2024-03-04",
            rentPaid = false
        )
    }
}