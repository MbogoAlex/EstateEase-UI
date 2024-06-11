package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.caretaker.MeterReadingRequestBody
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.waterMeterData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.*
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import java.time.LocalDateTime

data class EditMeterReadingScreenUiState(
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val childScreen: String = "",
    val waterMeterDt: WaterMeterDt = waterMeterData,
    val capturedMeterReading: Double? = null,
    val previousMeterReading: Double? = null,
    val capturedPhoto: Uri? = null,
    val uploadedImage: String? = null,
    val uploadedImageId: Int? = null,
    val capturedImageUri: Uri? = null,
    val previousImage: String? = null,
    val previousImageId: Int? = null,
    val capturedUnits: String = "",
    val month: String = "",
    val year: String = "",
    val uploadButtonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
)
class EditMeterReadingScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = EditMeterReadingScreenUiState())
    val uiState: StateFlow<EditMeterReadingScreenUiState> = _uiState.asStateFlow()

    private val childScreen: String? = savedStateHandle[EditMeterReadingScreenDestination.childScreen]
    private val meterTableId: String? = savedStateHandle[EditMeterReadingScreenDestination.meterTableId]
    private val propertyName: String? = savedStateHandle[EditMeterReadingScreenDestination.propertyName]
    private val month: String? = savedStateHandle[EditMeterReadingScreenDestination.month]
    private val year: String? = savedStateHandle[EditMeterReadingScreenDestination.year]

    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserModel->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserModel.toUserDetails()
                    )
                }
            }
        }
        loadMeterReading()
        if(month != null && year != null) {
            _uiState.update {
                it.copy(
                    month = month,
                    year = year
                )
            }
        }
    }

    fun loadMeterReading() {
        Log.i("METER_DATA", "FETCHING METER DATA")
        viewModelScope.launch {
            try {
                val response = apiRepository.getMeterReadingDataById(meterTableId!!.toInt())
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            waterMeterDt = response.body()?.data?.waterMeter!!,
                            capturedMeterReading = if(response.body()?.data?.waterMeter?.waterUnitsReading == null) 0.0 else response.body()?.data?.waterMeter?.waterUnitsReading,
                            uploadedImage = response.body()?.data?.waterMeter?.imageName,
                            uploadedImageId = response.body()?.data?.waterMeter?.imageId,
                        )
                    }
                    Log.i("METER_DATA_FETCHED", response.body()?.data?.waterMeter!!.toString())
                } else {
                    Log.e("METER_READING_FETCH_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                Log.e("METER_READING_FETCH_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadMeterReading(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        val meterReadingRequest = MeterReadingRequestBody(
            meterDtTableId = meterTableId!!.toInt(),
            waterUnits = uiState.value.capturedMeterReading!!,
            month = uiState.value.month,
            year = uiState.value.year
        )
        var meterImage: MultipartBody.Part? = null
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uiState.value.capturedImageUri!!, "r", null)
        parcelFileDescriptor?.let { pfd ->
            val inputStream = FileInputStream(pfd.fileDescriptor)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while(inputStream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            val byteArray = byteArrayOutputStream.toByteArray()

            //Get the MIME type of the file

            val mimeType = context.contentResolver.getType(uiState.value.capturedImageUri!!)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
            val imagePart = MultipartBody.Part.createFormData("image", "upload.$extension", requestFile)
            meterImage = imagePart
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.uploadMeterReading(
                    meterReadingRequestBody = meterReadingRequest,
                    image = meterImage!!
                )
                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("UPLOAD_METER_READING_ERROR_RESPONSE", response.toString())
                }
            }catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("UPLOAD_METER_READING_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMeterReading(context: Context) {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        val meterReadingRequest = MeterReadingRequestBody(
            meterDtTableId = meterTableId!!.toInt(),
            waterUnits = uiState.value.capturedMeterReading!!,
            month = uiState.value.month,
            year = uiState.value.year
        )
        val uploadedImage = if (uiState.value.capturedImageUri != null) uiState.value.capturedImageUri else uiState.value.uploadedImage
        var meterImage: MultipartBody.Part? = null

        viewModelScope.launch {
            try {
                val byteArray = withContext(Dispatchers.IO) {
                    when (uploadedImage) {
                        is Uri -> {
                            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uploadedImage, "r", null)
                            parcelFileDescriptor?.let { pfd ->
                                val inputStream = FileInputStream(pfd.fileDescriptor)
                                val byteArrayOutputStream = ByteArrayOutputStream()
                                val buffer = ByteArray(1024)
                                var length: Int
                                while (inputStream.read(buffer).also { length = it } != -1) {
                                    byteArrayOutputStream.write(buffer, 0, length)
                                }
                                byteArrayOutputStream.toByteArray()
                            }
                        }
                        is String -> {
                            if (uploadedImage.startsWith("http://") || uploadedImage.startsWith("https://")) {
                                try {
                                    URL(uploadedImage).openStream().use { it.readBytes() }
                                } catch (e: IOException) {
                                    null
                                }
                            } else {
                                null
                            }
                        }
                        else -> null
                    }
                }

                byteArray?.let {
                    val mimeType = when (uploadedImage) {
                        is Uri -> context.contentResolver.getType(uploadedImage)
                        is String -> "image/jpeg"
                        else -> null
                    } ?: "image/jpeg"

                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                    val requestFile = RequestBody.create(mimeType.toMediaTypeOrNull(), it)
                    meterImage = MultipartBody.Part.createFormData("image", "upload.$extension", requestFile)
                }

                val response = apiRepository.updateMeterReading(
                    oldImageId = uiState.value.uploadedImageId!!,
                    meterReadingDataTableId = meterTableId.toInt(),
                    meterReadingRequestBody = meterReadingRequest,
                    image = meterImage!!
                )
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("UPDATE_METER_READING_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("UPDATE_METER_READING_ERROR_EXCEPTION", e.toString())
            }
        }
    }


    fun updateMeterReadingText(meterReading: String) {
        _uiState.update {
            it.copy(
                capturedMeterReading = meterReading.toDouble()
            )
        }
    }

    fun uploadCapturedImage(image: Uri?) {
        _uiState.update {
            it.copy(
                capturedImageUri = image
            )
        }
    }

    fun removeUploadedImage() {
        _uiState.update {
            it.copy(
                capturedImageUri = null,
                uploadedImage = null
            )
        }
    }

    fun resetLoadingStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    fun checkIfAllFieldsAreFilled() {
        _uiState.update {
            it.copy(
                uploadButtonEnabled = uiState.value.capturedMeterReading != null && (uiState.value.capturedImageUri != null || uiState.value.uploadedImage != null)
            )
        }
    }

    init {
        if(childScreen != null) {
            _uiState.update {
                it.copy(
                    childScreen = childScreen
                )
            }
        }
        loadStartupData()
    }
}