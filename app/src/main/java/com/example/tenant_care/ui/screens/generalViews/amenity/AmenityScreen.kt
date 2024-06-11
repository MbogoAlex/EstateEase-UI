package com.example.tenant_care.ui.screens.generalViews.amenity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.amenity.Amenity
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.sampleAmenity

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AmenityScreenComposable(
    navigateToEditAmenityScreen: () -> Unit,
    navigateToAmenityDetailsScreen: (amenityId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AmenityScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        AmenityScreen(
            roleId = uiState.userDetails.roleId!!,
            amenities = uiState.amenities,
            searchText = if(uiState.searchText == null) "" else uiState.searchText!!,
            onStartSearch = {
                viewModel.filterAmenities(it)
            },
            onStopSearch = {
                viewModel.unfilter()
            },
            navigateToAmenityDetailsScreen = navigateToAmenityDetailsScreen,
            navigateToEditAmenityScreen = navigateToEditAmenityScreen
        )
    }

}

@Composable
fun AmenityScreen(
    roleId: Int,
    amenities: List<Amenity>,
    navigateToEditAmenityScreen: () -> Unit,
    navigateToAmenityDetailsScreen: (amenityId: String) -> Unit,
    onStartSearch: (value: String) -> Unit,
    searchText: String,
    onStopSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            if(roleId == 1) {
                FloatingActionButton(onClick = navigateToEditAmenityScreen) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new amenity"
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                TextField(
                    value = searchText,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = onStopSearch) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                                       
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                            fontSize = 12.sp
                        )
                    },
                    onValueChange = onStartSearch,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(amenities) {amenity ->
                        AmenityCell(
                            amenity = amenity,
                            modifier = Modifier
                                .clickable {
                                    navigateToAmenityDetailsScreen(amenity.amenityId.toString())
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityCell(
    amenity: Amenity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            if(amenity.images[0].name.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .alpha(0.5f)
                        .height(180.dp)
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
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(amenity.images[0].name)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Front ID",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = amenity.amenityName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
            )
            Text(
                text = "${amenity.providerName} - ${amenity.providerPhoneNumber}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AmenityScreenPreview() {
    var i = 10
    val amenities = mutableListOf<Amenity>()
    do {
        amenities.add(sampleAmenity)
        i--
    } while (
        i > 0
    )
    Tenant_careTheme {
        AmenityScreen(
            roleId = 1,
            amenities = amenities,
            searchText = "",
            onStartSearch = {},
            onStopSearch = {},
            navigateToAmenityDetailsScreen = {},
            navigateToEditAmenityScreen = {}
        )
    }
}