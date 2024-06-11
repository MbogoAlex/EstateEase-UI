package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.model.additionalExpense.AdditionalExpenseUpdateRequestBody
import com.example.tenant_care.model.penalty.PenaltyRequestBody
import com.example.tenant_care.model.penalty.PenaltyUpdateRequestBody
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.ui.screens.pManagerViews.PManagerHomeScreenUiState
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.ExpenseUpdateStatus
import com.example.tenant_care.util.PenaltyUpdateStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class EstateEaseRentOverview(
    val totalExpectedRent: Double = 0.0,
    val totalUnits: Int = 0,
    val paidAmount: Double = 0.0,
    val clearedUnits: Int = 0,
    val deficit: Double = 0.0,
    val unclearedUnits: Int = 1
)

data class RentPaymentsInfoHomeScreenUiState(
    val estateEaseRentOverview: EstateEaseRentOverview = EstateEaseRentOverview(),
    val userDSDetails: UserDSDetails = UserDSDetails(0, 0, "", "", "", "", "", ""),
    val penaltyStatus: Boolean = false,
    val penaltyAmount: Double = 0.0,
    val pricePerWaterUnit: Double = 0.0,
    val newPricePerWaterUnit: String = "",
    val newPenaltyAmount: String = "",
    val month: String = "",
    val year: String = "",
    val executionStatus: ExecutionStatus = ExecutionStatus.INITIAL,
    val penaltyUpdateStatus: PenaltyUpdateStatus = PenaltyUpdateStatus.INITIAL,
    val expenseUpdateStatus: ExpenseUpdateStatus = ExpenseUpdateStatus.INITIAL
)
@RequiresApi(Build.VERSION_CODES.O)
class RentPaymentsInfoHomeScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = RentPaymentsInfoHomeScreenUiState())
    val uiState: StateFlow<RentPaymentsInfoHomeScreenUiState> = _uiState.asStateFlow()

    // load user details

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() { userDsDetails ->
                _uiState.update {
                    it.copy(
                        userDSDetails = userDsDetails,
                    )
                }
            }
        }
        _uiState.update {
            it.copy(
                month = LocalDateTime.now().month.toString(),
                year = LocalDateTime.now().year.toString()
            )
        }
        fetchPenalty()
        fetchPricePerWaterUnit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRentOverview() {
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchRentPaymentOverview(uiState.value.month, uiState.value.year)
                if(response.isSuccessful) {
                    val estateEaseRentOverview =
                        EstateEaseRentOverview(
                            totalExpectedRent = response.body()?.data?.rentpayment?.totalExpectedRent!!,
                            totalUnits = response.body()?.data?.rentpayment?.totalUnits!!,
                            paidAmount = response.body()?.data?.rentpayment?.paidAmount!!,
                            clearedUnits = response.body()?.data?.rentpayment?.clearedUnits!!,
                            deficit = response.body()?.data?.rentpayment?.deficit!!,
                            unclearedUnits = response.body()?.data?.rentpayment?.unclearedUnits!!
                        )
                    _uiState.update {
                        it.copy(
                            estateEaseRentOverview = estateEaseRentOverview
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun fetchPenalty() {
        viewModelScope.launch {
            try {
               val response = apiRepository.fetchPenalty(2)
               if(response.isSuccessful) {
                   _uiState.update {
                       it.copy(
                           penaltyStatus = response.body()?.data?.penalty?.status!!,
                           penaltyAmount = response.body()?.data?.penalty?.cost!!,
                           newPenaltyAmount = response.body()?.data?.penalty?.cost!!.toString()
                       )
                   }
               }
            } catch (e: Exception) {

            }
        }
    }

    fun activateLatePaymentPenalty() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
            
        }
        val penaltyRequestBody = PenaltyRequestBody(
            penaltyPerDay = uiState.value.penaltyAmount
        )
        viewModelScope.launch {
            try {
                val response = apiRepository.activateLatePaymentPenalty(
                    month = LocalDateTime.now().month.toString(),
                    year = LocalDateTime.now().year.toString(),
                    penaltyRequestBody = penaltyRequestBody
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            penaltyStatus = true,
                            executionStatus = ExecutionStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.FAILURE
                        )
                    }
                    Log.e("ACTIVATE_PENALTY_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("ACTIVATE_PENALTY_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun deActivateLatePaymentPenalty() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.deActivateLatePaymentPenalty(
                    month = LocalDateTime.now().month.toString(),
                    year = LocalDateTime.now().year.toString()
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            penaltyStatus = false,
                            executionStatus = ExecutionStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            executionStatus = ExecutionStatus.FAILURE
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
            }
        }
    }

    fun updatePenaltyAmount(amount: String) {
        _uiState.update {
            it.copy(
                newPenaltyAmount = amount
            )
        }
    }

    fun resetPenaltyAmount() {
        _uiState.update {
            it.copy(
                newPenaltyAmount = uiState.value.penaltyAmount.toString()
            )
        }
    }

    fun updatePenalty() {
        _uiState.update {
            it.copy(
                penaltyUpdateStatus = PenaltyUpdateStatus.LOADING
            )
        }
        val penaltyUpdateRequestBody = PenaltyUpdateRequestBody(
            name = "Late payment",
            cost = uiState.value.newPenaltyAmount.toDouble()
        )

        viewModelScope.launch {
            try {
                val response = apiRepository.updatePenalty(
                    penaltyUpdateRequestBody = penaltyUpdateRequestBody,
                    id = 2
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            penaltyAmount = uiState.value.newPenaltyAmount.toDouble(),
                            penaltyUpdateStatus = PenaltyUpdateStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            penaltyUpdateStatus = PenaltyUpdateStatus.FAILURE
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        penaltyUpdateStatus = PenaltyUpdateStatus.FAILURE
                    )
                }
            }
        }
    }

    fun fetchPricePerWaterUnit() {
        viewModelScope.launch {
            try {
                val response = apiRepository.getExpense(1);
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            pricePerWaterUnit = response.body()?.data?.expense?.cost!!,
                            newPricePerWaterUnit = response.body()?.data?.expense?.cost!!.toString()
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updatePricePerWaterUnit(amount: String) {
        _uiState.update {
            it.copy(
                newPricePerWaterUnit = amount
            )
        }
    }

    fun resetNewPricePerWaterUnit() {
        _uiState.update {
            it.copy(
                newPricePerWaterUnit = ""
            )
        }
    }

    fun savePricePerWaterUnit() {
        _uiState.update {
            it.copy(
                expenseUpdateStatus = ExpenseUpdateStatus.INITIAL
            )
        }
        val expenseUpdateRequestBody = AdditionalExpenseUpdateRequestBody(
            name = "water",
            cost = uiState.value.newPricePerWaterUnit.toDouble()
        )
        viewModelScope.launch {
            try {
               val response = apiRepository.updateExpense(
                   additionalExpenseUpdateRequestBody = expenseUpdateRequestBody,
                   id = 1
               )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            pricePerWaterUnit = response.body()?.data?.expense?.cost!!,
                            expenseUpdateStatus = ExpenseUpdateStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            expenseUpdateStatus = ExpenseUpdateStatus.FAILURE
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        expenseUpdateStatus = ExpenseUpdateStatus.FAILURE
                    )
                }
            }
        }
    }

    fun resetExecutionStatus() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.INITIAL,
                penaltyUpdateStatus = PenaltyUpdateStatus.INITIAL,
                expenseUpdateStatus = ExpenseUpdateStatus.INITIAL
            )
        }
    }

    init {
        loadUserDetails()
        fetchRentOverview()
    }
}