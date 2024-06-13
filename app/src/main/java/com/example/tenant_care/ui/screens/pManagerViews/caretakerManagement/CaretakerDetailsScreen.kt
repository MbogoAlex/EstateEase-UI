package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.model.caretaker.CaretakerDT
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.EditAlertDialog
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.PaymentStatus
import com.example.tenant_care.util.caretakerExample
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CaretakerDetailsScreenDestination: AppNavigation{
    override val title: String = "Caretaker Details screen"
    override val route: String = "caretaker-details-screen"
    val caretakerId: String = "caretakerId"
    val routeWithArgs: String = "$route/{$caretakerId}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CaretakerDetailsScreenComposable(
    navigateToHomeScreenWithArgs: (childScreen: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: CaretakerDetailsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = navigateToPreviousScreen)

    val scope = rememberCoroutineScope()

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    var showRemovalDialog by remember {
        mutableStateOf(false)
    }

    var countdown by remember {
        mutableIntStateOf(30)
    }

    if(uiState.paymentStatus == PaymentStatus.SUCCESS) {
        Toast.makeText(context, "Payment successful", Toast.LENGTH_LONG).show()
        navigateToHomeScreenWithArgs("caretakers-screen")
        viewModel.resetExecutionStatus()
    } else if(uiState.paymentStatus == PaymentStatus.FAILURE) {
        Toast.makeText(context, "Failed. Try again later", Toast.LENGTH_LONG).show()
        viewModel.resetExecutionStatus()
    }

    if(uiState.executionStatus == ExecutionStatus.SUCCESS) {
        Toast.makeText(context, "Caretaker removed", Toast.LENGTH_LONG).show()
        navigateToHomeScreenWithArgs("caretakers-screen")
        viewModel.resetExecutionStatus()
    } else if(uiState.executionStatus == ExecutionStatus.FAILURE) {
        Toast.makeText(context, "Failed. Try again later", Toast.LENGTH_LONG).show()
        viewModel.resetExecutionStatus()
    }

    if(showPaymentDialog) {
        EditAlertDialog(
            title = "Pay caretaker?",
            onConfirm = {
                showPaymentDialog = !showPaymentDialog
                scope.launch {
                    viewModel.payCaretaker()
                    while(countdown > 0) {
                        delay(1000)
                        countdown--
                    }
                    viewModel.checkCaretakerPaymentStatus()
                }
            },
            onDismissRequest = { /*TODO*/ }
        )
    }

    if(showRemovalDialog) {
        EditAlertDialog(
            title = "Remove caretaker?",
            onConfirm = {
                showRemovalDialog = !showRemovalDialog
                viewModel.removeCaretaker()
            },
            onDismissRequest = { showRemovalDialog = !showRemovalDialog }
        )
    }

    CaretakerDetailsScreen(
        countdown = countdown,
        caretakerDT = uiState.caretakerDT,
        navigateToPreviousScreen = navigateToPreviousScreen,
        onPayCaretaker = { showPaymentDialog = !showPaymentDialog },
        onRemoveCaretaker = { showRemovalDialog = !showRemovalDialog },
        caretakerPaid = uiState.caretakerPaid,
        paymentStatus = uiState.paymentStatus
    )

}

@Composable
fun CaretakerDetailsScreen(
    caretakerDT: CaretakerDT,
    navigateToPreviousScreen: () -> Unit,
    onPayCaretaker: () -> Unit,
    onRemoveCaretaker: () -> Unit,
    countdown: Int,
    paymentStatus: PaymentStatus,
    caretakerPaid: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous screen")
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onRemoveCaretaker) {
                Text(text = "Remove caretaker")
            }
        }
        CaretakerDetailsCell(
            caretakerDT = caretakerDT
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = !caretakerPaid && paymentStatus != PaymentStatus.LOADING,
            onClick = onPayCaretaker,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(paymentStatus == PaymentStatus.LOADING) {
                Text(text = "Processing in $countdown seconds")
            } else {
                Text(text = "Pay")
            }
        }
    }
}

@Composable
fun CaretakerDetailsCell(
    caretakerDT: CaretakerDT,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Caretaker",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.fullName
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.phoneNumber
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = caretakerDT.email
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CaretakerDetailsScreenPreview() {
    Tenant_careTheme {
        CaretakerDetailsScreen(
            caretakerDT = caretakerExample,
            navigateToPreviousScreen = {},
            onRemoveCaretaker = {},
            countdown = 30,
            paymentStatus = PaymentStatus.INITIAL,
            caretakerPaid = false,
            onPayCaretaker = {}
        )
    }
}