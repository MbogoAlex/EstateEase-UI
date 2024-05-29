package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.waterMeterData

object EditMeterReadingScreenDestination: AppNavigation {
    override val title: String = "Meter reading edit screen"
    override val route: String = "meter-reading-edit-screen"
    val meterTableId: String = "meterTableId"
    val childScreen: String = "childScreen"
    val routeWithArgs: String = "$route/{$meterTableId}/{$childScreen}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditMeterReadingScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToCaretakerHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = navigateToPreviousScreen)
    val context = LocalContext.current

    val viewModel: EditMeterReadingScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loadingStatus == LoadingStatus.SUCCESS) {
        Toast.makeText(context, "Meter reading edited", Toast.LENGTH_SHORT).show()
        navigateToCaretakerHomeScreenWithArgs("meter-reading")
        viewModel.resetLoadingStatus()
    } else if(uiState.loadingStatus == LoadingStatus.FAILURE) {
        Toast.makeText(context, "Failed to edit meter reading", Toast.LENGTH_SHORT).show()
        viewModel.resetLoadingStatus()
    }


    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showEditUnitsDialog by remember {
        mutableStateOf(false)
    }
    if(showEditUnitsDialog) {
        WaterUnitsEditDialog(
            value = uiState.capturedMeterReading.toString(),
            onValueChange = {
                viewModel.updateMeterReadingText(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            onDismissRequest = { showEditUnitsDialog = !showEditUnitsDialog },
            onConfirm = { showEditUnitsDialog = !showEditUnitsDialog}
        )
    }

    fun createImageUri(): Uri {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "new_image.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IllegalStateException("Failed to create new MediaStore record.")
    }


    val photoCapture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {success ->
            if(success) {
                viewModel.uploadCapturedImage(imageUri)
                viewModel.checkIfAllFieldsAreFilled()
            }
        }
    )


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if(it) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                imageUri = createImageUri()
                photoCapture.launch(imageUri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box {
       EditMeterReadingScreen(
           waterMeterDt = uiState.waterMeterDt,
           onEditUnits = {
               showEditUnitsDialog = !showEditUnitsDialog
           },
           onImageUpload = {
               val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
               if(permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                   try {
                       imageUri = createImageUri()
                       photoCapture.launch(imageUri)
                   } catch (e: Exception) {
                       Log.e("CAMERA_LAUNCH_FAIL", e.toString())
                   }
               } else {
                   permissionLauncher.launch(Manifest.permission.CAMERA)
               }

           },
           uploadText = "Meter reading",
           capturedImageUri = uiState.capturedImageUri,
           capturedMeterReading = uiState.capturedMeterReading,
           previousMeterReading = uiState.previousMeterReading,
           uploadedImage = uiState.uploadedImage,
           previousImage = uiState.previousImage,
           onRemoveUploadedImage = {
               viewModel.removeUploadedImage()
               viewModel.checkIfAllFieldsAreFilled()
           },
           onUploadMeterReading = {
               viewModel.uploadMeterReading(context)
           },
           onUpdateMeterReading = {
               viewModel.updateMeterReading(context)
           },
           navigateToPreviousScreen = navigateToPreviousScreen,
           loadingStatus = uiState.loadingStatus,
           buttonEnabled = uiState.uploadButtonEnabled
       )
    }
}

@Composable
fun EditMeterReadingScreen(
    waterMeterDt: WaterMeterDt,
    onEditUnits: () -> Unit,
    onImageUpload: () -> Unit,
    uploadText: String,
    capturedImageUri: Uri?,
    capturedMeterReading: Double?,
    previousMeterReading: Double?,
    uploadedImage: String?,
    previousImage: String?,
    onRemoveUploadedImage: () -> Unit,
    onUploadMeterReading: () -> Unit,
    onUpdateMeterReading: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    loadingStatus: LoadingStatus,
    buttonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "METER READING",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ROOM: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = waterMeterDt.propertyName)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tenant: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = waterMeterDt.tenantName)
        }
        Column(
            modifier = Modifier
                .weight(10f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current reading (units):",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$capturedMeterReading units",
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onEditUnits) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit units"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(capturedImageUri != null) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(capturedImageUri)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Front ID",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(0.5f)
                            .background(Color.Black)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                            )
                            .align(Alignment.TopEnd),
                        onClick = onRemoveUploadedImage
                    ) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove front id"
                        )
                    }
                }
            } else if(uploadedImage != null && !uploadedImage.contains("null")) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(waterMeterDt.imageName)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Front ID",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(0.5f)
                            .background(Color.Black)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                            )
                            .align(Alignment.TopEnd),
                        onClick = onRemoveUploadedImage
                    ) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Remove front id"
                        )
                    }
                }
            } else {
                ImageUpload(
                    onImageUpload = onImageUpload,
                    uploadText = uploadText,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Previous reading (units):",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                if(previousMeterReading != null) {
                    Text(
                        text = "$previousMeterReading units",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(previousImage != null && !previousImage.contains("null")) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(previousImage)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Current reading",
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .alpha(0.5f)
                        .height(250.dp)
                        .fillMaxWidth()
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
        Spacer(modifier = Modifier.weight(1f))
        if(waterMeterDt.waterUnits != null) {
            Button(
                enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onUpdateMeterReading
            ) {
                if(loadingStatus == LoadingStatus.LOADING) {
                    CircularProgressIndicator()
                } else {
                    Text("Update")
                }
            }
        } else {
            Button(
                enabled = buttonEnabled && loadingStatus != LoadingStatus.LOADING,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onUploadMeterReading
            ) {
                if(loadingStatus == LoadingStatus.LOADING) {
                    CircularProgressIndicator()
                } else {
                    Text("Upload")
                }
            }
        }
    }
}

@Composable
fun ImageUpload(
    onImageUpload: () -> Unit,
    uploadText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = onImageUpload) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = uploadText
                    )
                }
                Text(text = "Capture water meter box")
            }
        }
    }
}

@Composable
fun WaterUnitsEditDialog(
    value: String,
    onValueChange: (newValue: String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(text = "Water meter reading")
        },
        text = {
             Column {
                 Text(
                     text = "Enter the water units value",
                     fontWeight = FontWeight.Bold
                 )
                 Spacer(modifier = Modifier.height(10.dp))
                 OutlinedTextField(
                     value = value,
                     label = {
                         Text(text = "Water units")
                     },
                     keyboardOptions = KeyboardOptions.Default.copy(
                         imeAction = ImeAction.Done,
                         keyboardType = KeyboardType.Decimal
                     ),
                     onValueChange = onValueChange
                 )
             }
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EditMeterReadingScreenPreview() {
    Tenant_careTheme {
        EditMeterReadingScreen(
            waterMeterDt = waterMeterData,
            onEditUnits = {},
            onImageUpload = {},
            uploadText = "Meter reading",
            capturedImageUri = null,
            capturedMeterReading = 3.0,
            previousMeterReading = null,
            uploadedImage = null,
            previousImage = null,
            onRemoveUploadedImage = {},
            onUploadMeterReading = {},
            onUpdateMeterReading = {},
            navigateToPreviousScreen = {},
            loadingStatus = LoadingStatus.INITIAL,
            buttonEnabled = false
        )
    }
}