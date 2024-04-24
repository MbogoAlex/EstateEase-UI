package com.example.tenant_care.tenantViews.rentPayment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun PaymentHomeScreenComposable(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PaymentHomeScreen()
    }
}
@Composable
fun PaymentHomeScreen(
    modifier: Modifier = Modifier
) {
    val items = arrayListOf<Int>(1, 2, 3)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
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
                    .padding(20.dp)
            ) {
                Text(
                    text = "EstateEase",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                    Text(
                        text = "Col C2",
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
                                text = "Martin Mwaura",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(text = "4 days")
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Tenant since: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "2024-05-05 11:44",
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    items.forEach {
                        RentStatusCard()
                        Spacer(modifier = Modifier.height(10.dp))
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


@Composable
fun RentStatusCard(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
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
                    text = "February",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Payment status:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Unpaid",
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            RentStatusCardItem(
                key = "Water reading: 2 units",
                value = "Ksh, 300.00"
            )
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(
                key = "Dustbin collection:",
                value = "Ksh, 150.00"
            )
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(
                key = "Rent:",
                value = "Ksh, 10, 550.00"
            )
            Spacer(modifier = Modifier.height(10.dp))
            RentStatusCardItem(
                key = "Total payable:",
                value = "Ksh,10, 950.00"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    )
            ) {
                Column {
                    Text(
                        text = "Rent due on:",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "05/05/2024",
                        color = Color.Green,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Countdown:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "2 days",
                        color = Color.Green
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            RentPaymentButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                navigateToRentInvoice = { /*TODO*/ }
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
    navigateToRentInvoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        onClick = { navigateToRentInvoice() }
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

@Preview(showBackground = true)
@Composable
fun TenantRentViewScreenPreview() {
    Tenant_careTheme {
        PaymentHomeScreen()
    }
}