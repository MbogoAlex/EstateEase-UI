package com.example.tenant_care.tenantViews.rentPayment

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun InvoiceComposable(
    modifier: Modifier = Modifier
) {

}

@Composable
fun InvoiceScreen(
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate to previous screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "APRIL, 2024",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PaymentInvoice()
        Spacer(modifier = Modifier.weight(1f))
        PayRentButton()
    }
}

@Composable
fun PaymentInvoice(
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
                    text = "Col C2",
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "No.Rooms: 2")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tenant name: ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Alice Njoki")
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
                Text(text = "PAID")
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
                    text = "0",
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Penalty active: ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "TRUE")
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
                    text = "Ksh,200",
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
                    text = "Ksh,0.0",
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total payable amount: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ksh15,500.0",
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

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    Tenant_careTheme {
        InvoiceScreen()
    }
}