package com.example.tenant_care.pManagerViews

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
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions
import java.time.LocalDateTime

object PManagerHomeScreenDestination: AppNavigation {
    override val title: String = "PManager Home Screen"
    override val route: String = "pmanager-home-screen"

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PManagerHomeComposable(
    navigateToUnitsManagementScreen: () -> Unit,
) {
    val viewModel: PManagerHomeScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box {
        PManagerHomeScreen(
            uiState = uiState,
            navigateToUnitsManagementScreen = navigateToUnitsManagementScreen
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PManagerHomeScreen(
    uiState: PManagerHomeScreenUiState,
    navigateToUnitsManagementScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            PManagerHomeTopBar(
                uiState = uiState
            )
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
                RentPaymentCard(
                    uiState = uiState
                )
                Spacer(modifier = Modifier.height(20.dp))
                PManagerUnitsHomeScreenBody(
                    navigateToAddUnitScreen = navigateToUnitsManagementScreen
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentPaymentCard(
    uiState: PManagerHomeScreenUiState,
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
                Text(text = "Occupied units: ${uiState.estateEaseRentOverview.totalUnits}")
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
                    text = ReusableFunctions.formatMoneyValue(uiState.estateEaseRentOverview.totalExpectedRent),
                    style = TextStyle(
                        color = Color.Green
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${uiState.estateEaseRentOverview.totalUnits} units")
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
                    text = ReusableFunctions.formatMoneyValue(uiState.estateEaseRentOverview.paidAmount),
                    style = TextStyle(
                        color = Color.Blue
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${uiState.estateEaseRentOverview.clearedUnits} units")
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
                    text = ReusableFunctions.formatMoneyValue(uiState.estateEaseRentOverview.deficit),
                    style = TextStyle(
                        color = Color.Red
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${uiState.estateEaseRentOverview.unclearedUnits} units")
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
    navigateToAddUnitScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row {
            ElevatedCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navigateToAddUnitScreen() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(150.dp)
                ) {
                    Text(
                        text = "Units Management",
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
    uiState: PManagerHomeScreenUiState,
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
                    text = "PManager: ${uiState.userDSDetails.fullName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RentPaymentCardPreview() {
    Tenant_careTheme {
        RentPaymentCard(
            uiState = PManagerHomeScreenUiState()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PManagerHomeScreenPreview() {
    Tenant_careTheme {
        PManagerHomeScreen(
            navigateToUnitsManagementScreen = {},
            uiState = PManagerHomeScreenUiState()
        )
    }
}