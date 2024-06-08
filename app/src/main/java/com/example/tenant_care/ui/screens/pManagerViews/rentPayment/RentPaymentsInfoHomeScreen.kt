package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsInfoHomeScreenComposable(
    navigateToRentPaymentsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: RentPaymentsInfoHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box {
        RentPaymentsInfoHomeScreen(
            penaltyActive = uiState.penaltyStatus,
            pManagerName = uiState.userDSDetails.fullName,
            totalUnits = uiState.estateEaseRentOverview.totalUnits,
            expectedRent = uiState.estateEaseRentOverview.totalExpectedRent,
            paidAmount = uiState.estateEaseRentOverview.paidAmount,
            clearedUnits = uiState.estateEaseRentOverview.clearedUnits,
            deficit = uiState.estateEaseRentOverview.deficit,
            unclearedUnits = uiState.estateEaseRentOverview.unclearedUnits,
            onPenaltyStatusChanged = {},
            navigateToRentPaymentsScreen = navigateToRentPaymentsScreen,
            navigateToAddUnitScreen = {}
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentsInfoHomeScreen(
    penaltyActive: Boolean,
    pManagerName: String,
    totalUnits: Int,
    expectedRent: Double,
    paidAmount: Double,
    clearedUnits: Int,
    deficit: Double,
    unclearedUnits: Int,
    onPenaltyStatusChanged: (status: Boolean) -> Unit,
    navigateToRentPaymentsScreen: () -> Unit,
    navigateToAddUnitScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        RentPaymentCard(
            totalUnits = totalUnits,
            expectedRent = expectedRent,
            paidAmount = paidAmount,
            clearedUnits = clearedUnits,
            deficit = deficit,
            unclearedUnits = unclearedUnits,
            navigateToRentPaymentsScreen = navigateToRentPaymentsScreen)
        Spacer(modifier = Modifier.height(20.dp))
        AdditionalPayments(
            penaltyActive = penaltyActive,
            onPenaltyStatusChanged = {}
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentCard(
    totalUnits: Int,
    expectedRent: Double,
    paidAmount: Double,
    clearedUnits: Int,
    deficit: Double,
    unclearedUnits: Int,
    navigateToRentPaymentsScreen: () -> Unit,
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
                    text = "${LocalDateTime.now().month}, ${LocalDateTime.now().year}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Change month")
                }
            }
            Row {
                Text(text = "Occupied units: $totalUnits")
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
                    text = ReusableFunctions.formatMoneyValue(expectedRent),
                    style = TextStyle(
                        color = Color.Green
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$totalUnits units")
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
                    text = ReusableFunctions.formatMoneyValue(paidAmount),
                    style = TextStyle(
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$clearedUnits units")
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
                    text = ReusableFunctions.formatMoneyValue(deficit),
                    style = TextStyle(
                        color = Color.Red
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "$unclearedUnits units")
                Icon(
                    painter = painterResource(id = R.drawable.navigate_next),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { navigateToRentPaymentsScreen() },
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
fun AdditionalPayments(
    penaltyActive: Boolean,
    onPenaltyStatusChanged: (status: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Penalty")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = penaltyActive, onCheckedChange = onPenaltyStatusChanged)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RentPaymentsInfoHomeScreenPreview() {
    Tenant_careTheme {
        RentPaymentsInfoHomeScreen(
            penaltyActive = true,
            pManagerName = "Alex Mbogo",
            totalUnits = 10,
            expectedRent = 10000.0,
            paidAmount = 5000.0,
            clearedUnits = 5,
            deficit = 5000.0,
            unclearedUnits = 5,
            onPenaltyStatusChanged = {},
            navigateToRentPaymentsScreen = { /*TODO*/ },
            navigateToAddUnitScreen = { /*TODO*/ }
        )
    }
}