package com.example.tenant_care.ui.screens.generalViews.amenity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.model.amenity.AmenityImage
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.util.ExecutionStatus
import com.example.tenant_care.util.sampleAmenity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

object AmenityDetailsScreenDestination: AppNavigation {
    override val title: String = "Amenity details screen"
    override val route: String = "amenity-details-screen"
    val amenityId: String = "amenityId"
    val routeWithArgs: String = "$route/{$amenityId}"
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AmenityDetailsScreenComposable(
    navigateToEditAmenityScreenWithArgs: (amenityId: String) -> Unit,
    navigateToPManagerHomeScreenWithArgs: (childScreen: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: AmenityDetailsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(onBack = navigateToPreviousScreen)

    var showDeletionDialog by remember {
        mutableStateOf(false)
    }
    
    if(uiState.executionStatus == ExecutionStatus.SUCCESS) {
        Toast.makeText(context, "Amenity deleted", Toast.LENGTH_SHORT).show()
        navigateToPManagerHomeScreenWithArgs("amenities-screen")
        viewModel.resetExecutionStatus()
    } else if(uiState.executionStatus == ExecutionStatus.FAILURE) {
        Toast.makeText(context, "Failed to delete amenity. Check connection", Toast.LENGTH_SHORT).show()
        viewModel.resetExecutionStatus()
    }

    if(showDeletionDialog) {
        DeleteAlertDialog(
            onConfirm = {
                showDeletionDialog = !showDeletionDialog
                viewModel.deleteAmenity(uiState.amenity.amenityId)
            },
            onDismissRequest = { showDeletionDialog = !showDeletionDialog }
        )
    }

    Box {
        AmenityDetailsScreen(
            roleId = uiState.userDetails.roleId!!,
            amenity = uiState.amenity,
            executionStatus = uiState.executionStatus,
            onDeleteAmenity = {
                showDeletionDialog = !showDeletionDialog
            },
            navigateToEditAmenityScreenWithArgs = navigateToEditAmenityScreenWithArgs,
            navigateToPreviousScreen = navigateToPreviousScreen
        )
    }
}
@Composable
fun AmenityDetailsScreen(
    roleId: Int,
    amenity: Amenity,
    executionStatus: ExecutionStatus,
    navigateToEditAmenityScreenWithArgs: (amenityId: String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
    onDeleteAmenity: () -> Unit,
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
            if(roleId == 1) {
                IconButton(
                    enabled = executionStatus != ExecutionStatus.LOADING,
                    onClick = onDeleteAmenity
                ) {
                    if(executionStatus == ExecutionStatus.LOADING) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            tint = Color.Red,
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete amenity"
                        )
                    }
                }
            }

        }
        AmenityImagesDisplay(
            imageName = amenity.amenityName,
            images = amenity.images
        )
        Spacer(modifier = Modifier.height(10.dp))
        AmenityDetailsBody(amenity = amenity)
        if(roleId == 1) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { navigateToEditAmenityScreenWithArgs(amenity.amenityId.toString()) }
            ) {
                Text(text = "Edit amenity")
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AmenityImagesDisplay(
    imageName: String,
    images: List<AmenityImage>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0)
    Column {
        Card {
            Box {
                if(images.isEmpty()) {
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
                } else {
                    HorizontalPager(count = images.size, state = pagerState) { page ->
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(images[page].name)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.ic_broken_image),
                            contentScale = ContentScale.Crop,
                            contentDescription = imageName,
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        )

                    }
                }

                if(images.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                            color = Color.White,
                            modifier = Modifier
                                .alpha(0.5f)
                                .background(Color.Black)
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                )
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityDetailsBody(
    amenity: Amenity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:${amenity.providerPhoneNumber}")
    }
    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:${amenity.providerPhoneNumber}")
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = amenity.amenityName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = amenity.amenityDescription,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
            Text(
                text = "Contact",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Name: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = amenity.providerName,
                fontSize = 18.sp
            )
        }
        if(!amenity.providerEmail.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = amenity.providerEmail,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Phone: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = amenity.providerPhoneNumber,
                fontSize = 18.sp
            )
            IconButton(onClick = {
                context.startActivity(phoneIntent)
            }) {
                Icon(
                    tint = Color.Blue,
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "Call"
                )
            }
            IconButton(onClick = { 
                context.startActivity(smsIntent)
            }) {
                Icon(
                    tint = Color.Blue,
                    painter = painterResource(id = R.drawable.message),
                    contentDescription = "Message"
                )
            }
        }
    }
}

@Composable
fun DeleteAlertDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
                Text(text = "Confirm deletion")
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
fun AmenityDetailsScreenPreview() {
    AmenityDetailsScreen(
        roleId = 1,
        amenity = sampleAmenity,
        executionStatus = ExecutionStatus.INITIAL,
        onDeleteAmenity = {},
        navigateToEditAmenityScreenWithArgs = {},
        navigateToPreviousScreen = {}
    )
}