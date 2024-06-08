package com.example.tenant_care.ui.screens.generalViews.amenity

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.amenity.AmenityImage
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ExecutionStatus

object EditAmenityScreenDestination: AppNavigation {
    override val title: String = "Edit amenity screen"
    override val route: String = "edit-amenity-screen"
    val amenityId: String = "amenityId"
    val routeWithArgs: String = "$route/{$amenityId}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditAmenityComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToPManagerHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: EditAmenityScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    viewModel.checkIfRequiredFieldsAreFilled()

    BackHandler(onBack = navigateToPreviousScreen)

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    if(uiState.executionStatus == ExecutionStatus.SUCCESS) {
        if(uiState.amenityId != null) {
            Toast.makeText(context, "Amenity updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Amenity added", Toast.LENGTH_SHORT).show()
        }
        navigateToPManagerHomeScreenWithArgs("amenities-screen")
        viewModel.resetExecutionStatus()
    } else if(uiState.executionStatus == ExecutionStatus.FAILURE) {
        if(uiState.amenityId != null) {
            Toast.makeText(context, "Failed to update amenity. Check your connection", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add amenity. Check your connection", Toast.LENGTH_SHORT).show()
        }
        viewModel.resetExecutionStatus()
    }

    if(showEditDialog) {
        if(uiState.amenityId != null) {
            EditAlertDialog(
                title = "Save amenity update",
                onConfirm = {
                    showEditDialog = !showEditDialog
                    if(uiState.uploadedImages.isNotEmpty()) {
                        viewModel.updateAmenityWithImages(context)
                    } else {
                        viewModel.updateAmenityWithoutImages()
                    }
                },
                onDismissRequest = { showEditDialog = !showEditDialog }
            )
        } else {
            EditAlertDialog(
                title = "Add amenity",
                onConfirm = {
                    showEditDialog = !showEditDialog
                    viewModel.uploadAmenity(context)
                },
                onDismissRequest = { showEditDialog = !showEditDialog }
            )
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {uriList ->
            if(uriList.isNotEmpty()) {
                for(uri in uriList) {
                    viewModel.uploadImage(uri)
                }
            }

        }
    )

    Box {
        EditAmenityScreen(
            amenityId = uiState.amenityId,
            amenityName = uiState.amenityName,
            amenityDescription = uiState.amenityDescription,
            onLaunchGallery = {
                galleryLauncher.launch("image/*")
            },
            onRemovePhoto = {
                viewModel.removeImage(it)
            },
            images = uiState.uploadedImages,
            serverImages = uiState.serverImages,
            onRemoveServerImage = {imageId, index ->
                viewModel.deleteAmenityImage(imageId, index)
            },
            onChangeAmenityName = {
                viewModel.updateAmenityName(it)
            },
            onChangeAmenityDescription = {
                viewModel.updateAmenityDescription(it)
            },
            ownerName = uiState.providerName,
            ownerPhoneNumber = uiState.providerPhoneNumber,
            ownerEmail = uiState.providerEmail!!,
            onChangeOwnerName = {
                viewModel.updateProviderName(it)
            },
            onChangeOwnerPhoneNumber = {
                viewModel.updateProviderPhoneNumber(it)
            },
            onChangeOwnerEmail = {
                viewModel.updateProviderEmail(it)
            },
            onSaveAmenity = {
                showEditDialog = !showEditDialog
            },
            saveButtonEnabled = uiState.saveButtonEnabled,
            executionStatus = uiState.executionStatus,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}

@Composable
fun EditAmenityScreen(
    amenityId: String?,
    amenityName: String,
    amenityDescription: String,
    onLaunchGallery: () -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    images: List<Uri>,
    serverImages: List<AmenityImage>,
    onRemoveServerImage: (imageId: Int, index: Int) -> Unit,
    onChangeAmenityName: (newValue: String) -> Unit,
    onChangeAmenityDescription: (newValue: String) -> Unit,
    ownerName: String,
    ownerPhoneNumber: String,
    ownerEmail: String,
    onChangeOwnerName: (newVale: String) -> Unit,
    onChangeOwnerPhoneNumber: (newVale: String) -> Unit,
    onChangeOwnerEmail: (newVale: String) -> Unit,
    onSaveAmenity: () -> Unit,
    saveButtonEnabled: Boolean,
    executionStatus: ExecutionStatus,
    navigateToPreviousScreen: () -> Unit,
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
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate to previous screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                enabled = saveButtonEnabled && executionStatus != ExecutionStatus.LOADING,
                onClick = onSaveAmenity
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(amenityId != null) {
                        Text(text = "Save update")
                        Spacer(modifier = Modifier.width(3.dp))
                        if(executionStatus == ExecutionStatus.LOADING) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        } else {
                            Icon(painter = painterResource(id = R.drawable.save), contentDescription = "Edit amenity")
                        }
                    } else {
                        Text(text = "Save")
                        Spacer(modifier = Modifier.width(3.dp))
                        if(executionStatus == ExecutionStatus.LOADING) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        } else {
                            Icon(painter = painterResource(id = R.drawable.save), contentDescription = "Edit amenity")
                        }
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "* Required fields",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(10.dp))
        AmenityDetails(
            amenityName = amenityName,
            amenityDescription = amenityDescription,
            onLaunchGallery = onLaunchGallery,
            onRemovePhoto = onRemovePhoto,
            images = images,
            serverImages = serverImages,
            onRemoveServerImage = onRemoveServerImage,
            onChangeAmenityName = onChangeAmenityName,
            onChangeAmenityDescription = onChangeAmenityDescription,
            ownerName = ownerName,
            ownerPhoneNumber = ownerPhoneNumber,
            ownerEmail = ownerEmail,
            onChangeOwnerName = onChangeOwnerName,
            onChangeOwnerPhoneNumber = onChangeOwnerPhoneNumber,
            onChangeOwnerEmail = onChangeOwnerEmail
        )
    }
}

@Composable
fun AmenityDetails(
    amenityName: String,
    amenityDescription: String,
    onLaunchGallery: () -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    onRemoveServerImage: (imageId: Int, index: Int) -> Unit,
    images: List<Uri>,
    serverImages: List<AmenityImage>,
    onChangeAmenityName: (newValue: String) -> Unit,
    onChangeAmenityDescription: (newValue: String) -> Unit,
    ownerName: String,
    ownerPhoneNumber: String,
    ownerEmail: String,
    onChangeOwnerName: (newVale: String) -> Unit,
    onChangeOwnerPhoneNumber: (newVale: String) -> Unit,
    onChangeOwnerEmail: (newVale: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Amenity details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        AmenityTextDetails(
            amenityName = amenityName,
            amenityDescription = amenityDescription,
            onChangeAmenityName = onChangeAmenityName,
            onChangeAmenityDescription = onChangeAmenityDescription
        )
        if(serverImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            ServerImages(
                images = serverImages,
                onRemoveServerImage = onRemoveServerImage
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        PhotoSelection(
            onLaunchGallery = onLaunchGallery,
            onRemovePhoto = onRemovePhoto,
            images = images
        )
        Spacer(modifier = Modifier.height(50.dp))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Owner details",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        AmenityOwnerDetails(
            ownerName = ownerName,
            ownerPhoneNumber = ownerPhoneNumber,
            ownerEmail = ownerEmail,
            onChangeOwnerName = onChangeOwnerName,
            onChangeOwnerPhoneNumber = onChangeOwnerPhoneNumber,
            onChangeOwnerEmail = onChangeOwnerEmail
        )
    }
}

@Composable
fun InputField(
    value: String,
    label: String,
    required: Boolean,
    keyboardOptions: KeyboardOptions,
    onValueChange: (newValue: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        label = {
            if(required) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = label)
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            } else {
                Text(text = label)
            }
        },
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun PhotoSelection(
    onLaunchGallery: () -> Unit,
    onRemovePhoto: (index: Int) -> Unit,
    images: List<Uri>,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            images.forEachIndexed { index, uri ->
                Row {
                    Image(
                        rememberImagePainter(data = uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(
                                top = 5.dp,
                                end = 3.dp,
                                bottom = 5.dp
                            )
                            .size(100.dp)
                    )
                    IconButton(onClick = {
                        onRemovePhoto(index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    onLaunchGallery()
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Click to upload images"
                    )
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun ServerImages(
    images: List<AmenityImage>,
    onRemoveServerImage: (imageId: Int, index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Images in the server: ",
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            images.forEachIndexed { index, image ->
                Row {
                    Image(
                        rememberImagePainter(data = image.name),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(
                                top = 5.dp,
                                end = 3.dp,
                                bottom = 5.dp
                            )
                            .size(100.dp)
                    )
                    IconButton(onClick = {
                        onRemoveServerImage(image.id, index)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityTextDetails(
    amenityName: String,
    amenityDescription: String,
    onChangeAmenityName: (newVale: String) -> Unit,
    onChangeAmenityDescription: (newVale: String) -> Unit,
    modifier: Modifier = Modifier
) {
    InputField(
        value = amenityName,
        label = "Name",
        required = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        onValueChange = onChangeAmenityName,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        value = amenityDescription,
        label = "Description",
        required = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        onValueChange = onChangeAmenityDescription,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun AmenityOwnerDetails(
    ownerName: String,
    ownerPhoneNumber: String,
    ownerEmail: String,
    onChangeOwnerName: (newVale: String) -> Unit,
    onChangeOwnerPhoneNumber: (newVale: String) -> Unit,
    onChangeOwnerEmail: (newVale: String) -> Unit,
    modifier: Modifier = Modifier
) {
    InputField(
        value = ownerName,
        label = "Name",
        required = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        onValueChange = onChangeOwnerName,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        value = ownerPhoneNumber,
        label = "Phone",
        required = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        onValueChange = onChangeOwnerPhoneNumber,
        modifier = Modifier
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))
    InputField(
        value = ownerEmail,
        label = "Email",
        required = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        onValueChange = onChangeOwnerEmail,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun EditAlertDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview(showBackground = true)
@Composable
fun EditAmenityScreenPreview() {
    Tenant_careTheme {
        EditAmenityScreen(
            amenityId = null,
            amenityName = "",
            amenityDescription = "",
            onLaunchGallery = { /*TODO*/ },
            onRemovePhoto = {},
            images = emptyList(),
            serverImages = emptyList(),
            onRemoveServerImage = {imageId, index ->  },
            onChangeAmenityName = {},
            onChangeAmenityDescription = {},
            ownerName = "",
            ownerEmail = "",
            ownerPhoneNumber = "",
            onChangeOwnerPhoneNumber = {},
            onChangeOwnerEmail = {},
            onChangeOwnerName = {},
            onSaveAmenity = {},
            saveButtonEnabled = false,
            executionStatus = ExecutionStatus.INITIAL,
            navigateToPreviousScreen = {}
        )
    }
}