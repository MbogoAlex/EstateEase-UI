package com.example.tenant_care.tenantViews.rentPayment

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier
) {
    val viewModel: RentInvoiceScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box {
        RentInvoiceScreen(
            rentPayment = uiState.rentPayment,
            tenantName = uiState.userDetails.fullName,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun RentInvoiceScreen(
    navigateToPreviousScreen: () -> Unit,
    rentPayment: RentPayment,
    tenantName: String,
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
            rentPayment = rentPayment
        )
        Spacer(modifier = Modifier.weight(1f))
        PayRentButton()
    }
}

@Composable
fun PaymentInvoice(
    rentPayment: RentPayment,
    tenantName: String,
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
                Text(text = "Ksh15,500")
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
                    text = formattedMonthlyRent,
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
        }
    }
}

@Composable
fun PayRentButton(
    modifier: Modifier = Modifier
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { /*TODO*/ }
    ) {
        Text(text = "Pay rent")
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
            navigateToPreviousScreen = {}
        )
    }
}