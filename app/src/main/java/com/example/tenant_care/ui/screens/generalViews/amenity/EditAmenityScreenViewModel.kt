package com.example.tenant_care.ui.screens.generalViews.amenity

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.model.amenity.AmenityImage
import com.example.tenant_care.model.amenity.AmenityRequestBody
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.sampleAmenity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.FileInputStream

data class EditAmenityScreenUiState(
    val amenity: Amenity = sampleAmenity,
    val amenityName: String = "",
    val amenityId: String? = null,
    val amenityDescription: String = "",
    val providerName: String = "",
    val providerPhoneNumber: String = "",
    val providerEmail: String? = "",
    val uploadedImages: List<Uri> = emptyList(),
    val serverImages: List<AmenityImage> = emptyList(),
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val saveButtonEnabled: Boolean = false,
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL,
    val executionStatus: ExecutionStatus = ExecutionStatus.INITIAL,
)
class EditAmenityScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(value = EditAmenityScreenUiState())
    val uiState: StateFlow<EditAmenityScreenUiState> = _uiState.asStateFlow()

    private val amenityId: String? = savedStateHandle[EditAmenityScreenDestination.amenityId]

    val images = mutableStateListOf<Uri>()
    val serverImages = mutableStateListOf<AmenityImage>()
    fun loadStartupData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails(),
                        amenityId = amenityId
                    )
                }
            }
        }
        if(uiState.value.amenityId != null) {
            fetchAmenity()
        }
    }
    fun updateAmenityName(amenityName: String) {
        _uiState.update {
            it.copy(
                amenityName = amenityName
            )
        }
    }

    fun updateAmenityDescription(amenityDescription: String) {
        _uiState.update {
            it.copy(
                amenityDescription = amenityDescription
            )
        }
    }

    fun updateProviderName(providerName: String) {
        _uiState.update {
            it.copy(
                providerName = providerName
            )
        }
    }

    fun updateProviderPhoneNumber(providerPhoneNumber: String) {
        _uiState.update {
            it.copy(
                providerPhoneNumber = providerPhoneNumber
            )
        }
    }

    fun updateProviderEmail(providerEmail: String) {
        _uiState.update {
            it.copy(
                providerEmail = providerEmail
            )
        }
    }

    fun uploadImage(uri: Uri) {
        images.add(uri)
        _uiState.update {
            it.copy(
                uploadedImages = images
            )
        }
    }

    fun removeImage(index: Int) {
        images.removeAt(index)
        _uiState.update {
            it.copy(
                uploadedImages = images
            )
        }
    }

    fun fetchAmenity() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchAmenity(amenityId!!.toInt())
                if(response.isSuccessful) {
                    serverImages.addAll(response.body()?.data?.amenity?.images!!)
                    _uiState.update {
                        it.copy(
                            amenityName = response.body()?.data?.amenity?.amenityName!!,
                            amenityDescription = response.body()?.data?.amenity?.amenityDescription!!,
                            providerName = response.body()?.data?.amenity?.providerName!!,
                            providerEmail = if(response.body()?.data?.amenity?.providerEmail == null) "" else response.body()?.data?.amenity?.providerEmail,
                            providerPhoneNumber = response.body()?.data?.amenity?.providerPhoneNumber!!,
                            serverImages = serverImages,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                    Log.e("FETCH_AMENITY_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
                Log.e("FETCH_AMENITY_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun uploadAmenity(context: Context) {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }
        val amenityRequestBody = AmenityRequestBody(
            amenityName = uiState.value.amenityName,
            amenityDescription = uiState.value.amenityDescription,
            providerName = uiState.value.providerName,
            providerPhoneNumber = uiState.value.providerPhoneNumber,
            providerEmail = uiState.value.providerEmail,
            addedBy = uiState.value.userDetails.fullName,
            propertyManagerId = uiState.value.userDetails.userId!!
        )

        var imageParts = ArrayList<MultipartBody.Part>()
        uiState.value.uploadedImages.forEach { uri ->
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
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

                val mimeType = context.contentResolver.getType(uri)
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                val imagePart = MultipartBody.Part.createFormData("images", "upload.$extension", requestFile)
                imageParts.add(imagePart)
            }
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.addAmenity(
                    amenityRequestBody = amenityRequestBody,
                    images = imageParts
                )
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
                    Log.e("AMENITY_UPLOAD_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("AMENITY_UPLOAD_ERROR_EXCEPTION", e.toString())
            }
        }
    }

    fun updateAmenityWithImages(context: Context) {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }

        val amenityRequestBody = AmenityRequestBody(
            amenityName = uiState.value.amenityName,
            amenityDescription = uiState.value.amenityDescription,
            providerName = uiState.value.providerName,
            providerPhoneNumber = uiState.value.providerPhoneNumber,
            providerEmail = uiState.value.providerEmail,
            addedBy = uiState.value.userDetails.fullName,
            propertyManagerId = uiState.value.userDetails.userId!!
        )

        var imageParts = ArrayList<MultipartBody.Part>()

        uiState.value.uploadedImages.forEach { uri ->
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
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

                val mimeType = context.contentResolver.getType(uri)
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), byteArray)
                val imagePart = MultipartBody.Part.createFormData("images", "upload.$extension", requestFile)
                imageParts.add(imagePart)
            }
        }

        viewModelScope.launch {
            try {
                val response = apiRepository.updateAmenityWithImages(
                    amenityRequestBody = amenityRequestBody,
                    amenityId = amenityId!!.toInt(),
                    newImages = imageParts,
                )
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
                    Log.e("AMENITY_UPLOAD_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("AMENITY_UPLOAD_ERROR_EXCEPTION", e.toString())
            }
        }

    }

    fun updateAmenityWithoutImages() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.LOADING
            )
        }

        val amenityRequestBody = AmenityRequestBody(
            amenityName = uiState.value.amenityName,
            amenityDescription = uiState.value.amenityDescription,
            providerName = uiState.value.providerName,
            providerPhoneNumber = uiState.value.providerPhoneNumber,
            providerEmail = uiState.value.providerEmail,
            addedBy = uiState.value.userDetails.fullName,
            propertyManagerId = uiState.value.userDetails.userId!!
        )

        viewModelScope.launch {
            try {
                val response = apiRepository.updateAmenityWithoutImages(
                    amenityRequestBody = amenityRequestBody,
                    amenityId = amenityId!!.toInt(),
                )
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
                    Log.e("AMENITY_UPLOAD_ERROR_RESPONSE", response.toString())
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        executionStatus = ExecutionStatus.FAILURE
                    )
                }
                Log.e("AMENITY_UPLOAD_ERROR_EXCEPTION", e.toString())
            }
        }

    }

    fun deleteAmenityImage(id: Int, index: Int) {
        viewModelScope.launch {
            try {
               val response = apiRepository.deleteAmenityImage(id)
               if(response.isSuccessful) {
                   serverImages.removeAt(index)
                   _uiState.update {
                       it.copy(
                           serverImages = serverImages
                       )
                   }
               }
            } catch (e: Exception) {

            }
        }
    }

    fun resetExecutionStatus() {
        _uiState.update {
            it.copy(
                executionStatus = ExecutionStatus.INITIAL
            )
        }
    }

    fun checkIfRequiredFieldsAreFilled() {
        _uiState.update {
            it.copy(
                saveButtonEnabled = uiState.value.amenityName.isNotEmpty() &&
                        uiState.value.amenityDescription.isNotEmpty() &&
                        (uiState.value.uploadedImages.isNotEmpty() || uiState.value.serverImages.isNotEmpty()) &&
                        uiState.value.providerName.isNotEmpty() &&
                        uiState.value.providerPhoneNumber.isNotEmpty()
            )
        }
    }

    init {
        loadStartupData()
    }
}