package com.example.tenant_care.pManagerViews.rentPayment
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.tenant_care.model.pManager.TenantRentPaymentData
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantsNotPaidScreenComposable(

    navigateToSingleTenantPaymentDetails: (tenantId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TenantsNotPaidScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiStatus.collectAsState()


    Box(
        modifier = modifier
    ) {
        TenantsNotPaidScreen(
            rentPayments = uiState.rentPaymentsData.rentpayment,
            navigateToSingleTenantPaymentDetails = navigateToSingleTenantPaymentDetails
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TenantsNotPaidScreen(
    rentPayments: List<TenantRentPaymentData>,
    navigateToSingleTenantPaymentDetails: (tenantId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn() {
            items(rentPayments.size) {
                IndividualNotPaidTenantCell(
                    rentPayment = rentPayments[it],
                    paymentStatus = rentPayments[it].rentPaymentStatus,
                    paidLate = rentPayments[it].paidLate.takeIf { paidLate ->  paidLate != null } ?: false,
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                        .clickable {
                            navigateToSingleTenantPaymentDetails(rentPayments[it].tenantId!!.toString())
                        }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualNotPaidTenantCell(
    rentPayment: TenantRentPaymentData,
    paymentStatus: Boolean,
    paidLate: Boolean,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
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
                    text = "Rent due: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rentPayment.dueDate
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
                        text = "On: ${ReusableFunctions.formatDateTimeValue(rentPayment.paidAt!!)}",
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
                onClick = { /*TODO*/ }
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
fun IndividualNotPaidTenantCellPreview() {
    val rentPayments: List<TenantRentPaymentData> = emptyList()
    Tenant_careTheme {
        IndividualNotPaidTenantCell(
            rentPayment = rentPayments[0],
            paymentStatus = false,
            paidLate = true
        )
    }
}
