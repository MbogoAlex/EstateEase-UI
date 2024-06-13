package com.example.tenant_care.ui.screens.tenantViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.model.tenant.RentPayment
import com.example.tenant_care.model.tenant.RentPaymentRequestBody
import com.example.tenant_care.util.PaymentStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.waterMeterData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class FetchingInvoiceStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}


val rentPaymentPlaceHolder = RentPayment(
    rentPaymentTblId = 1,
    dueDate = "2024-09-10 11:44",
    month = "April",
    monthlyRent = 10000.0,
    paidAmount = 10400.0,
    paidAt = "",
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
    tenantActive = true,
    waterUnits = 2.0,
    meterReadingDate = "2024-04-02T17:42:24.352844",
    imageFile = "",
    pricePerUnit = 150.0
)

data class RentInvoiceScreenUiState(
    val rentPayment: RentPayment = rentPaymentPlaceHolder,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val paidAt: String = "",
    val waterUnitsConsumed: Double? = null,
    val totalWaterPrice: Double? = 0.0,
//    val waterUnitsCurrentMonth: String? = "",
//    val waterUnitsPreviousMonth: String? = "",
    val waterMeterDt: WaterMeterDt? = waterMeterData,
    val paymentStatus: PaymentStatus = PaymentStatus.INITIAL,
    val fetchingInvoiceStatus: FetchingInvoiceStatus = FetchingInvoiceStatus.INITIAL
)
@RequiresApi(Build.VERSION_CODES.O)
class RentInvoiceScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(RentInvoiceScreenUiState())
    val uiState: StateFlow<RentInvoiceScreenUiState> = _uiState.asStateFlow()

    val tenantId: String? = savedStateHandle[RentInvoiceScreenDestination.tenantId]
    val month: String? = savedStateHandle[RentInvoiceScreenDestination.month]
    val year: String? = savedStateHandle[RentInvoiceScreenDestination.year]
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    fun getMeterReadings() {
        Log.i("FETCHING", "$month, $year, ${uiState.value.userDetails.room}")
        viewModelScope.launch {
            try {
                val response = apiRepository.getMeterReadings(
                    month = month!!,
                    year = year!!,
                    meterReadingTaken = null,
                    tenantName = null,
                    propertyName = uiState.value.userDetails.room,
                    role = null
                )
                if (response.isSuccessful) {
                    val meterData: WaterMeterDt? = response.body()?.data?.waterMeter?.get(0)
                    Log.i("METER_DATA", meterData.toString())
                    var waterPrice: Double? = 0.0
                    var unitsConsumed: Double? = 0.0
                    if(meterData != null) {
                        unitsConsumed = if(meterData.waterUnitsReading != null && meterData.previousWaterMeterData?.waterUnitsReading != null) {
                            abs(meterData.waterUnitsReading - meterData.previousWaterMeterData.waterUnitsReading)
                        } else if(meterData.waterUnitsReading != null && meterData.previousWaterMeterData?.waterUnitsReading == null) {
                            meterData.waterUnitsReading
                        } else {
                            null
                        }
                        if(unitsConsumed != 0.0 && unitsConsumed != null) {
                            Log.i("PRICE_PER_UNIT:", "${meterData.pricePerUnit}")
                            waterPrice = meterData.pricePerUnit?.times(unitsConsumed)
                        } else if(unitsConsumed == 0.0) {
                            waterPrice = 0.0
                        }
                    }

                    _uiState.update {
                        it.copy(
                            waterMeterDt = meterData,
                            waterUnitsConsumed = unitsConsumed,
//                            waterUnitsCurrentMonth = response.body()?.data?.waterMeter!![0].month!!,
//                            waterUnitsPreviousMonth = response.body()?.data?.waterMeter!![0].previousWaterMeterData?.month!!,
                            totalWaterPrice = waterPrice
                        )
                    }
                    Log.i("METER_READING_FETCH", "SUCCESS")
                } else {
                    Log.e("METER_READING_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                Log.e("METER_READING_FETCH_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserInvoice() {

        _uiState.update {
            it.copy(
                fetchingInvoiceStatus = FetchingInvoiceStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.getRentPaymentRows(
                    tenantId = tenantId!!.toInt(),
                    month = month,
                    year = year,
                    roomName = null,
                    rooms = null,
                    tenantName = null,
                    rentPaymentStatus = null,
                    paidLate = null,
                    tenantActive = null
                )
                if(response.isSuccessful) {
                    if(response.body()?.data?.rentpayment!![0].paidAt != null) {
                        _uiState.update {
                            it.copy(
                                rentPayment = response.body()?.data?.rentpayment!![0],
                                paidAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.rentpayment!![0].paidAt!!),
                                fetchingInvoiceStatus = FetchingInvoiceStatus.SUCCESS
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                rentPayment = response.body()?.data?.rentpayment!![0],
                                fetchingInvoiceStatus = FetchingInvoiceStatus.SUCCESS
                            )
                        }
                    }
                    getMeterReadings()

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingInvoiceStatus = FetchingInvoiceStatus.FAILURE
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingInvoiceStatus = FetchingInvoiceStatus.FAILURE
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun payRent(payableAmount: Double) {
        _uiState.update {
            it.copy(
                paymentStatus = PaymentStatus.LOADING
            )
        }
        viewModelScope.launch {
            val rentPaymentRequestBody = RentPaymentRequestBody(
                waterMeterDataTableId = uiState.value.waterMeterDt!!.id!!,
                payableAmount = payableAmount,
                msisdn = uiState.value.userDetails.phoneNumber
            )
            try {
                val response = apiRepository.payRent(
                    rentPaymentTblId = _uiState.value.rentPayment.rentPaymentTblId,
                    rentPaymentRequestBody = rentPaymentRequestBody
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            paidAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.rentPayment?.paidAt!!),
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            paymentStatus = PaymentStatus.FAILURE
                        )
                    }
                    Log.e("RENT_PAYMENT_FAIL_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        paymentStatus = PaymentStatus.FAILURE
                    )
                }
                Log.e("RENT_PAYMENT_FAIL_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun checkRentPaymentStatus() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getRentPaymentStatus(uiState.value.rentPayment.rentPaymentTblId)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            paymentStatus = PaymentStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            paymentStatus = PaymentStatus.FAILURE
                        )
                    }
                    Log.e("RENT_PAYMENT_STATUS_CHECK_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        paymentStatus = PaymentStatus.FAILURE
                    )
                }
                Log.e("RENT_PAYMENT_STATUS_CHECK_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun resetRentPaymentStatus() {
        _uiState.update {
            it.copy(
                paymentStatus = PaymentStatus.INITIAL
            )
        }
    }

    init {
        loadStartupData()
        getUserInvoice()
    }
}