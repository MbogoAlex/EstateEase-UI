package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.CaretakerDT
import com.example.tenant_care.model.caretaker.CaretakerPaymentRequestBody
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.PaymentStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.caretakerExample
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class CaretakerDetailsScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val caretakerDT: CaretakerDT = caretakerExample,
    val caretakerPaid: Boolean = false,
    val paymentStatus: PaymentStatus = PaymentStatus.INITIAL,
    val executionStatus: ExecutionStatus = ExecutionStatus.INITIAL
)
@RequiresApi(Build.VERSION_CODES.O)
class CaretakerDetailsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedSavedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = CaretakerDetailsScreenUiState())
    val uiState: StateFlow<CaretakerDetailsScreenUiState> = _uiState.asStateFlow()

    private val caretakerId: String? = savedSavedStateHandle[CaretakerDetailsScreenDestination.caretakerId]

    private var transactionId = ""

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStartUpData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect(){dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
        getCaretaker()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCaretaker() {
        Log.i("CARETAKER_ID", caretakerId!!)
        viewModelScope.launch {
            try {
               val response = apiRepository.getCaretaker(caretakerId!!.toInt())
                if(response.isSuccessful) {

                    _uiState.update {
                        it.copy(
                            caretakerDT = response.body()?.data?.caretaker!!
                        )
                    }
                    checkIfCaretakerIsPaid()
                }
            } catch (e: Exception) {

            }
        }
    }

    fun payCaretaker() {
        _uiState.update {
            it.copy(
                paymentStatus = PaymentStatus.LOADING
            )
        }
        val caretakerPaymentResponseBody = CaretakerPaymentRequestBody(
            caretakerId = uiState.value.caretakerDT.caretakerId,
            amount = uiState.value.caretakerDT.salary
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.payCaretaker(caretakerPaymentResponseBody)
                if(response.isSuccessful) {
                    transactionId = response.body()?.data?.caretaker?.transactionId!!

                } else {
                    _uiState.update {
                        it.copy(
                            paymentStatus = PaymentStatus.FAILURE
                        )
                    }
                    Log.e("RENT_PAYMENT_INITIALIZATION_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        paymentStatus = PaymentStatus.FAILURE
                    )
                }
                Log.e("RENT_PAYMENT_INITIALIZATION_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun checkCaretakerPaymentStatus() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getCaretakerPaymentStatus(transactionId)
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            paymentStatus = PaymentStatus.SUCCESS,
                            caretakerPaid = true
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

    fun removeCaretaker() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.removeCaretaker(caretakerId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.FAILURE
                        )
                    }
                    Log.e("REMOVE_CARETAKER_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("REMOVE_CARETAKER_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkIfCaretakerIsPaid() {
        val month = LocalDateTime.now().month.toString()
        val year = LocalDateTime.now().year.toString()

        Log.i("CARETAKER", "${uiState.value.caretakerDT}")

        Log.i("PAYMNENTS:", "${uiState.value.caretakerDT.payments}")

        for(payment in uiState.value.caretakerDT.payments) {
            if(payment.month == month && payment.year == year) {
                Log.i("CARETAKER_PAID?", "${payment.month == month && payment.year == year}")
                _uiState.update {
                    it.copy(
                        caretakerPaid = true
                    )
                }
            }
        }
    }

    fun resetExecutionStatus() {
        _uiState.update {
            it.copy(
                paymentStatus = PaymentStatus.INITIAL,
                executionStatus = ExecutionStatus.INITIAL
            )
        }
    }

    init {
        loadStartUpData()
    }

}