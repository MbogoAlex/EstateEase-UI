package com.example.tenant_care.pManagerViews

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun PManagerHomeScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            PManagerHomeTopBar()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                RentPaymentCard()
                Spacer(modifier = Modifier.height(20.dp))
                PManagerUnitsHomeScreenBody()
            }
        }
    }
}

@Composable
fun RentPaymentCard(
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
                    text = "February, 2024",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Change month")
                }
            }
            Row {
                Text(text = "Occupied units: 50")
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
                    text = "Ksh, 50,000,000.00",
                    style = TextStyle(
                        color = Color.Green
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "50 units")
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
                    text = "Ksh, 30,000",
                    style = TextStyle(
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "30 units")
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
                    text = "Ksh, 20,000",
                    style = TextStyle(
                        color = Color.Red
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "20 units")
                Icon(
                    painter = painterResource(id = R.drawable.navigate_next),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { /*TODO*/ },
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
fun PManagerUnitsHomeScreenBody(
    modifier: Modifier = Modifier
) {
    Column {
        Row {
            ElevatedCard(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(150.dp)
                ) {
                    Text(
                        text = "Add Unit",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(150.dp)
                ) {
                    Text(
                        text = "Tenants Management",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = "Units Info",
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = "Notifications Management",
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.notifications),
                    contentDescription = null
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerHomeTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = "PropEase",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "PManager: Samuel Kairu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RentPaymentCardPreview() {
    Tenant_careTheme {
        RentPaymentCard()
    }
}

@Preview(showBackground = true)
@Composable
fun PManagerHomeScreenPreview() {
    Tenant_careTheme {
        PManagerHomeScreen()
    }
}