package com.example.tenant_care.ui.screens.tenantViews.rentPayment

import android.os.Build
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.tenant.RentPayment
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.PaymentDialog
import com.example.tenant_care.util.PaymentStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.waterMeterData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val totalWaterPrice = uiState.totalWaterPrice
    val daysLate = uiState.rentPayment.daysLate
    val monthlyRent = uiState.rentPayment.monthlyRent
    val formattedMonthlyRent = ReusableFunctions.formatMoneyValue(monthlyRent)
    val formattedDueDate = uiState.rentPayment.dueDate
    val dailyPenalty = uiState.rentPayment.penaltyPerDay
    val formattedDailyPenalty = ReusableFunctions.formatMoneyValue(dailyPenalty)
    if(daysLate > 0 && uiState.rentPayment.penaltyActive) {
        penaltyAccrued = uiState.rentPayment.penaltyPerDay * daysLate
    } else {
        penaltyAccrued = 0.0
    }


    var waterUnits = uiState.waterUnitsConsumed

    var formattedTotalWaterPrice = ""

    if(waterUnits != null) {
        formattedTotalWaterPrice = ReusableFunctions.formatMoneyValue(totalWaterPrice!!)
    }

    val formattedPenaltyAccrued = ReusableFunctions.formatMoneyValue(penaltyAccrued)
    val totalPayable = uiState.rentPayment.monthlyRent + penaltyAccrued + totalWaterPrice!!
    val formattedTotalPayable = ReusableFunctions.formatMoneyValue(totalPayable)

    var countdown by remember {
        mutableIntStateOf(30)
    }

    val scope = rememberCoroutineScope()

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    if(uiState.paymentStatus == PaymentStatus.SUCCESS) {
        Toast.makeText(context, "Rent paid successfully", Toast.LENGTH_LONG).show()
        navigateToTenantHomeScreen()
        viewModel.resetRentPaymentStatus()
    } else if(uiState.paymentStatus == PaymentStatus.FAILURE) {
        Toast.makeText(context, "Failed to pay rent. Try again later", Toast.LENGTH_LONG).show()
        countdown = 30
        viewModel.resetRentPaymentStatus()
    }

    if(showPaymentDialog) {
        PaymentDialog(
            title = "Pay ${ReusableFunctions.formatMoneyValue(totalPayable)} rent?",
            onConfirm = {
                showPaymentDialog = !showPaymentDialog
                scope.launch {
                    viewModel.payRent(totalPayable)
                    while(countdown > 0) {
                        delay(1000)
                        countdown--
                    }
                    viewModel.checkRentPaymentStatus()
                }
            },
            onDismissRequest = { showPaymentDialog = !showPaymentDialog }
        )
    }

    Box {
        RentInvoiceScreen(
            countdown = countdown,
            rentPayment = uiState.rentPayment,
            tenantName = uiState.userDetails.fullName,
            payRent = {
                showPaymentDialog = !showPaymentDialog
            },
            daysLate = daysLate,
            formattedDailyPenalty = formattedDailyPenalty,
            formattedMonthlyRent = formattedMonthlyRent,
            formattedPenaltyAccrued = formattedPenaltyAccrued,
            formattedTotalPayable = formattedTotalPayable,
            paymentStatus = uiState.paymentStatus,
            rentPaid = uiState.rentPayment.rentPaymentStatus,
            paidAt = uiState.paidAt,
            waterUnits = waterUnits,
            formattedTotalWaterPrice = formattedTotalWaterPrice,
            waterMeterDt = uiState.waterMeterDt,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentInvoiceScreen(
    countdown: Int,
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
    paymentStatus: PaymentStatus,
    waterUnits: Double?,
//    waterUnitsCurrentMonth: String,
//    waterUnitsPreviousMonth: String,
    formattedTotalWaterPrice: String?,
    waterMeterDt: WaterMeterDt?,
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            PaymentInvoice(
                tenantName = tenantName,
                daysLate = daysLate,
                formattedDailyPenalty = formattedDailyPenalty,
                formattedMonthlyRent = formattedMonthlyRent,
                formattedPenaltyAccrued = formattedPenaltyAccrued,
                formattedTotalPayable = formattedTotalPayable,
                paidAt = paidAt,
                rentPayment = rentPayment,
                waterUnits = waterUnits,
//                waterUnitsCurrentMonth = waterUnitsCurrentMonth,
//                waterUnitsPreviousMonth = waterUnitsPreviousMonth,
                formattedTotalWaterPrice = formattedTotalWaterPrice
            )
            Spacer(modifier = Modifier.height(10.dp))
            WaterMeterImages(waterMeterDt = waterMeterDt)
            Spacer(modifier = Modifier.height(20.dp))
            PayRentButton(
                payRent = payRent,
                rentPaid = rentPaid,
                countdown = countdown,
                paymentStatus = paymentStatus
            )
        }

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
    waterUnits: Double?,
//    waterUnitsCurrentMonth: String,
//    waterUnitsPreviousMonth: String,
    formattedTotalWaterPrice: String?,
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
            if(waterUnits != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Water units (${waterMeterData.waterUnitsReading}) : ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = waterUnits.toString())
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = formattedTotalWaterPrice!!)
                }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaterMeterImages(
    waterMeterDt: WaterMeterDt?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
//            .weight(10f)
            .horizontalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if(waterMeterDt?.meterReadingDate != null){
                Text(
                    text = "${waterMeterDt!!.month}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Taken at:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = ReusableFunctions.formatDateTimeValue(waterMeterDt.meterReadingDate),
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            } else {
                Text(
                    text = "Current",
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(waterMeterDt?.imageName != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(waterMeterDt.imageName)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Current meter reading",
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .alpha(0.5f)
                        .height(300.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(5.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(5.dp)
                        )
                ) {
                    Text(text = "No image")
                }
            }

        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            if(waterMeterDt?.previousWaterMeterData?.meterReadingDate != null){
                Text(
                    text = "${waterMeterDt.previousWaterMeterData.month}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Taken at:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = ReusableFunctions.formatDateTimeValue(waterMeterDt.previousWaterMeterData.meterReadingDate),
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            } else {
                Text(
                    text = "Previous month",
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Log.i("PREVIOUS_IMAGE_NULL", "${waterMeterDt?.previousWaterMeterData?.imageName != null}")
            if(waterMeterDt?.previousWaterMeterData?.imageName != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(waterMeterDt.previousWaterMeterData.imageName)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Previous meter reading",
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .alpha(0.5f)
                        .height(300.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(5.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(5.dp)
                        )
                ) {
                    Text(text = "No image")
                }
            }
        }


    }
}

@Composable
fun PayRentButton(
    payRent: () -> Unit,
    paymentStatus: PaymentStatus,
    rentPaid: Boolean,
    countdown: Int,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = paymentStatus != PaymentStatus.LOADING && !rentPaid,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = payRent
    ) {
        if(paymentStatus == PaymentStatus.LOADING) {
            Text(text = "Processing in $countdown seconds")
        } else {
            Text(text = "Pay rent")
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WaterMeterImagesPreview() {
    Tenant_careTheme {
        WaterMeterImages(
            waterMeterDt = waterMeterData
        )
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
        numberOfRooms = "Bedsitter",
        tenantId = 2,
        email = "tenant@gmail.com",
        fullName = "Mbogo AGM",
        nationalIdOrPassport = "234543234",
        phoneNumber = "0119987282",
        tenantAddedAt = "2023-09-23 10:32",
        waterUnits = 2.0,
        pricePerUnit = 200.0,
        imageFile = "",
        meterReadingDate = "2024-04-02T17:42:24.352844",
        tenantActive = true
    )
    Tenant_careTheme {
        RentInvoiceScreen(
            countdown = 30,
            tenantName = "Alex AG",
            rentPayment = rentPayment,
            payRent = {},
            formattedTotalPayable = "Ksh12,000",
            formattedMonthlyRent = "Ksh10,000",
            formattedDailyPenalty = "Ksh200",
            formattedPenaltyAccrued = "Ksh2,000",
            daysLate = 10,
            paymentStatus = PaymentStatus.INITIAL,
            navigateToPreviousScreen = {},
            paidAt = "2024-03-04",
            waterUnits = 2.0,
            formattedTotalWaterPrice = ReusableFunctions.formatMoneyValue(300.0),
            waterMeterDt = waterMeterData,
            rentPaid = false
        )
    }
}