package com.example.tenant_care.tenantViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TenantRentViewScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
    ) {
        Text(
            text = "Room: Col C2",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        RentStatusCard()
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
                onRentPaymentButtonClicked = { /*TODO*/ }
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
    onRentPaymentButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        onClick = { onRentPaymentButtonClicked() }
    ) {
        Text(text = "PROCEED TO PAYMENT")
    }
}