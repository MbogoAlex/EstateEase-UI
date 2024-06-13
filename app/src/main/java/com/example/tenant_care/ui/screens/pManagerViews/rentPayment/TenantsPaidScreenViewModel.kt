package com.example.tenant_care.ui.screens.pManagerViews.rentPayment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBodyData
import com.example.tenant_care.util.DownloadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

enum class FetchingTenantsPaidStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}

val paidRentPaymentsDataInit = RentPaymentDetailsResponseBodyData(
    rentpayment = emptyList()
)

data class TenantsPaidScreenUiState(
    val month: String = "",
    val year: String = "",
    val rentPaymentsData: RentPaymentDetailsResponseBodyData = paidRentPaymentsDataInit,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val fetchingStatus: FetchingTenantsPaidStatus = FetchingTenantsPaidStatus.INITIAL,
    val rentPaidAt: String = "",
    val tenantName: String? = null,
    val selectedNumOfRooms: String? = null,
    val rooms: List<String> = emptyList(),
    val selectedUnitName: String? = null,
    val tenantActive: Boolean? = null,
    val paidLate: Boolean? = null,
    val activeTenantsSelected: Boolean = false,
    val inactiveTenantsSelected: Boolean = false,
    val latePaymentsSelected: Boolean = false,
    val earlyPaymentsSelected: Boolean = false,
    val downloadingStatus: DownloadingStatus = DownloadingStatus.INITIAL
)

@RequiresApi(Build.VERSION_CODES.O)
class TenantsPaidScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = TenantsPaidScreenUiState())
    val uiStatus: StateFlow<TenantsPaidScreenUiState> = _uiState.asStateFlow()

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 101 // Arbitrary integer for the permission request code
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchRentPaymentsData(
        month: String,
        year: String,
        roomName: String?,
        rooms: Int?,
        tenantName: String?,
        tenantId: Int?,
        rentPaymentStatus: Boolean?,
        paidLate: Boolean?,
        tenantActive: Boolean?
    ) {
        Log.i("FETCHING_WITH_TENANT_ID", tenantId.toString())
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingTenantsPaidStatus.FETCHING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchRentPaymentStatusForAllTenants(
                    month = month,
                    year = year,
                    roomName = roomName,
                    rooms = rooms,
                    tenantName = tenantName,
                    tenantId = tenantId,
                    rentPaymentStatus = rentPaymentStatus,
                    paidLate = paidLate,
                    tenantActive = tenantActive
                )
                if(response.isSuccessful) {
                    val rooms = mutableListOf<String>()
                    for(rentPayment in response.body()?.data!!.rentpayment) {
                        rooms.add(rentPayment.propertyNumberOrName)
                    }
                    _uiState.update {
                        it.copy(
                            rentPaymentsData = response.body()?.data!!,
                            rooms = rooms,
                            rentPaidAt = ReusableFunctions.formatDateTimeValue(response.body()?.data?.rentpayment!![0].paidAt!!),
                            fetchingStatus = FetchingTenantsPaidStatus.SUCCESS
                        )
                    }

                    Log.i("PAYMENTS_FETCHED", "PAYMENTS FETCHED SUCCESSFULLY")

                } else {
                    _uiState.update {
                        it.copy(
                            fetchingStatus = FetchingTenantsPaidStatus.FAIL
                        )
                    }
                    Log.e("PAYMENTS_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        fetchingStatus = FetchingTenantsPaidStatus.FAIL
                    )
                }
                Log.e("PAYMENTS_FETCH_EXCEPTION", e.toString())
            }
        }
    }

    fun filterByTenantName(tenantName: String?) {
        Log.i("TENANT_NAME", tenantName!!)
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }
        _uiState.update {
            it.copy(
                tenantName = tenantName
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = true,
            paidLate = _uiState.value.paidLate,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun filterByNumberOfRooms(selectedNumOfRooms: String?) {
        Log.i("NoRooms", selectedNumOfRooms!!.toString())
        _uiState.update {
            it.copy(
                selectedNumOfRooms = selectedNumOfRooms
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = selectedNumOfRooms!!.toInt(),
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = true,
            paidLate = _uiState.value.paidLate,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun filterByUnitName(roomName: String?) {
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }
        _uiState.update {
            it.copy(
                selectedUnitName = roomName
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = roomName,
            rentPaymentStatus = true,
            paidLate = _uiState.value.paidLate,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun filterByActiveTenants(tenantActive: Boolean) {
        _uiState.update {
            it.copy(
                tenantActive = tenantActive,
                activeTenantsSelected = tenantActive,
                inactiveTenantsSelected = !tenantActive
            )
        }
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }

        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = true,
            paidLate = _uiState.value.paidLate,
            tenantActive = tenantActive
        )
    }

    fun filterByTimeOfPayment(paidLate: Boolean) {
        _uiState.update {
            it.copy(
                paidLate = paidLate,
                latePaymentsSelected = paidLate,
                earlyPaymentsSelected = !paidLate
            )
        }
        var rooms: Int?
        if(_uiState.value.selectedNumOfRooms == null) {
            rooms = null
        } else {
            rooms = _uiState.value.selectedNumOfRooms!!.toInt()
        }

        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = rooms,
            roomName = _uiState.value.selectedUnitName,
            rentPaymentStatus = true,
            paidLate = paidLate,
            tenantActive = _uiState.value.tenantActive
        )
    }

    fun setMonthAndYear(month: String, year: String) {
        _uiState.update {
            it.copy(
                month = month,
                year = year
            )
        }
        fetchRentPaymentsData(
            month = month,
            year = year,
            tenantName = null,
            tenantId = null,
            rooms = null,
            roomName = null,
            rentPaymentStatus = true,
            paidLate = null,
            tenantActive = null,
        )
    }

    fun resetFetchingStatus() {
        _uiState.update {
            it.copy(
                fetchingStatus = FetchingTenantsPaidStatus.INITIAL
            )
        }
    }

    // DOWNLOAD REPORT

    fun fetchReport(context: Context) {
        _uiState.update {
            it.copy(
                downloadingStatus = DownloadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.generateGeneralReport(
                    tenantId = null,
                    month = uiStatus.value.month,
                    year = uiStatus.value.year,
                    roomName = uiStatus.value.selectedUnitName,
                    rooms = uiStatus.value.selectedNumOfRooms,
                    tenantName = uiStatus.value.tenantName,
                    rentPaymentStatus = true,
                    paidLate = uiStatus.value.paidLate,
                )
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            downloadingStatus = DownloadingStatus.SUCCESS
                        )
                    }
                    Log.i("REPORT_GENERATION", "SUCCESS: ${response.body()}")
                    val pdfBytes = response.body()?.bytes()
                    Log.i("REPORT:", "PDF Bytes: $pdfBytes")

                    if (pdfBytes != null && pdfBytes.isNotEmpty()) {
                        savePdfToFile(context, pdfBytes, "rent_payments_report.pdf")
                    } else {
                        Log.e("REPORT_GENERATION", "PDF Bytes are null or empty")
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            downloadingStatus = DownloadingStatus.FAILURE
                        )
                    }
                    Log.e("REPORT_GENERATION_ERROR_RESPONSE", "Response not successful: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        downloadingStatus = DownloadingStatus.FAILURE
                    )
                }
                Log.e("REPORT_GENERATION_ERROR_EXCEPTION", "Exception: ${e.message}")
            }
        }
    }

    private fun savePdfToFile(context: Context, pdfBytes: ByteArray, fileName: String) {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                checkAndRequestPermissions(context)
            }

            val downloadsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            } else {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            }

            val file = File(downloadsDir, fileName)
            Log.i("SAVE_PDF_TO_FILE", "Saving PDF to: ${file.absolutePath}")

            FileOutputStream(file).use { output ->
                output.write(pdfBytes)
                Log.i("SAVE_PDF_TO_FILE", "PDF saved successfully")
            }

            if (file.exists()) {
                Log.i("SAVE_PDF_TO_FILE", "File exists after saving: ${file.absolutePath}")
                openPdf(context, file)
            } else {
                Log.e("SAVE_PDF_TO_FILE", "File does not exist after saving: ${file.absolutePath}")
            }
        } catch (e: Exception) {
            Log.e("SAVE_PDF_TO_FILE_ERROR", "Exception: ${e.message}")
        }
    }

    private fun openPdf(context: Context, file: File) {
        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            Log.i("OPEN_PDF", "File URI: $uri")

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            val chooser = Intent.createChooser(intent, "Open with")
            context.startActivity(chooser)

        } catch (e: Exception) {
            Log.e("OPEN_PDF_ERROR", "Exception: ${e.message}")
        }
    }

    private fun checkAndRequestPermissions(context: Context) {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, permissions.toTypedArray(),
                TenantsPaidScreenViewModel.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun resetDownloadingStatus() {
        _uiState.update {
            it.copy(
                downloadingStatus = DownloadingStatus.INITIAL
            )
        }
    }

    fun unfilterUnits() {
        _uiState.update {
            it.copy(
                tenantName = null,
                selectedNumOfRooms = null,
                selectedUnitName = null,
                tenantActive = null,
                activeTenantsSelected = false,
                inactiveTenantsSelected = false,
                latePaymentsSelected = false,
                earlyPaymentsSelected = false
            )
        }
        fetchRentPaymentsData(
            month = uiStatus.value.month,
            year = uiStatus.value.year,
            tenantName = _uiState.value.tenantName,
            tenantId = null,
            rooms = null,
            roomName = null,
            rentPaymentStatus = true,
            paidLate = _uiState.value.paidLate,
            tenantActive = null
        )
    }

    init {
        loadUserDetails()
    }
}