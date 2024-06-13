package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.EditAlertDialog

object PManagerAddUnitScreenDestination: AppNavigation {
    override val title: String = "Add Unit Screen"
    override val route: String = "add-unit-screen"
    val unitId: String = "unitId"
    val routeWithArgs: String = "$route/{$unitId}"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PManagerAddUnitComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToPmanagerHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: PManagerAddUnitScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    viewModel.checkIfAllFieldsAreFilled()

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    if(uiState.uploadingStatus == UploadingStatus.SUCCESS) {
        Toast.makeText(context, uiState.uploadingResponseMessage, Toast.LENGTH_SHORT).show()
        navigateToPmanagerHomeScreenWithArgs("units-management-screen")
        viewModel.resetUploadingStatus()
    }

    if(showEditDialog) {
        if(uiState.propertyId == null) {
            EditAlertDialog(
                title = "Add unit",
                onConfirm = {
                    showEditDialog = !showEditDialog
                    viewModel.uploadNewUnit() },
                onDismissRequest = {
                    showEditDialog = !showEditDialog
                }
            )
        } else {
            EditAlertDialog(
                title = "Save edit",
                onConfirm = {
                    showEditDialog = !showEditDialog
                    viewModel.updateUnit()
                },
                onDismissRequest = { showEditDialog = !showEditDialog }
            )
        }
    }


    
    Box(modifier = modifier) {
        PManagerAddUnitScreen(
            unitID = uiState.propertyId,
            numOfRooms = uiState.numOfRooms,
            unitNameOrNumber = uiState.unitNameOrNumber,
            unitDescription = uiState.unitDescription,
            monthlyRent = uiState.monthlyRent,
            showSaveButton = uiState.showSaveButton,
            updateNumOfRooms = {
                viewModel.updateNumOfRooms(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            updateUnitNameOrNumber = {
                viewModel.updateUnitNameOrNumber(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            updateUnitDescription = {
                viewModel.updateUnitDescription(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            updateUnitRent = {
                viewModel.updateUnitRent(it)
                viewModel.checkIfAllFieldsAreFilled()
            },
            uploadingStatus = uiState.uploadingStatus,
            navigateToPreviousScreen = navigateToPreviousScreen,
            saveUnit = {
                showEditDialog = !showEditDialog
            }
        )
    }
}

@Composable
fun PManagerAddUnitScreen(
    unitID: String?,
    numOfRooms: String,
    unitNameOrNumber: String,
    unitDescription: String,
    monthlyRent: String,
    showSaveButton: Boolean,
    updateNumOfRooms: (rooms: String) -> Unit,
    updateUnitNameOrNumber: (name: String) -> Unit,
    updateUnitDescription: (description: String) -> Unit,
    updateUnitRent: (rent: String) -> Unit,
    uploadingStatus: UploadingStatus,
    navigateToPreviousScreen: () -> Unit,
    saveUnit: () -> Unit,
    modifier: Modifier = Modifier
) {


    Scaffold(
        topBar = {
            PManagerAddUnitScreenTopBar(
                unitID = unitID
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            if(unitID != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = navigateToPreviousScreen) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate to previous screen"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                    .fillMaxSize()
            ) {
                PManagerAddUnitForm(
                    unitId = unitID,
                    numOfRooms = numOfRooms,
                    unitNameOrNumber = unitNameOrNumber,
                    unitDescription = unitDescription,
                    monthlyRent = monthlyRent,
                    showSaveButton = showSaveButton,
                    updateNumOfRooms = updateNumOfRooms,
                    updateUnitNameOrNumber = updateUnitNameOrNumber,
                    updateUnitDescription = updateUnitDescription,
                    updateUnitRent = updateUnitRent,
                    uploadingStatus = uploadingStatus,
                    saveUnit = saveUnit
                )
            }
        }
    }
}

@Composable
fun PManagerAddUnitForm(
    unitId: String?,
    numOfRooms: String,
    unitNameOrNumber: String,
    unitDescription: String,
    monthlyRent: String,
    showSaveButton: Boolean,
    updateNumOfRooms: (rooms: String) -> Unit,
    updateUnitNameOrNumber: (unitName: String) -> Unit,
    updateUnitDescription: (unitDescription: String) -> Unit,
    updateUnitRent: (rent: String) -> Unit,
    uploadingStatus: UploadingStatus,
    saveUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var items = mutableListOf("Bedsitter", "One bedroom", "Two bedroom")

    Column {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Column {
                    TextButton(onClick = { expanded = true }) {
                        if (numOfRooms.isEmpty()) {
                            Text(text = "Type")
                        } else {
                            Text(text = numOfRooms)
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        for (item in items) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = item)
                                },
                                onClick = {
                                    updateNumOfRooms(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                //            Spacer(modifier = Modifier.height(20.dp))
                PManagerAddUnitInputField(
                    label = "Unit name / number",
                    value = unitNameOrNumber,
                    onValueChange = {
                        updateUnitNameOrNumber(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                PManagerAddUnitInputField(
                    label = "Unit description",
                    value = unitDescription,
                    onValueChange = {
                        updateUnitDescription(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                PManagerAddUnitInputField(
                    label = "Monthly rent",
                    value = monthlyRent,
                    onValueChange = {
                        updateUnitRent(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AddUnitButton(
            unitID = unitId,
            enabled = showSaveButton,
            onSaveUnitButtonClicked = { saveUnit() },
            uploadingStatus = uploadingStatus,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
    }
}

@Composable
fun AddUnitButton(
    unitID: String?,
    enabled: Boolean,
    onSaveUnitButtonClicked: () -> Unit,
    uploadingStatus: UploadingStatus,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = enabled,
        onClick = onSaveUnitButtonClicked,
        modifier = modifier
    ) {
        Text(text = "Save Unit")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerAddUnitInputField(
    label: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerAddUnitScreenTopBar(
    unitID: String?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = "PropEase",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                if(unitID == null) {
                    Text(
                        text = "Add Unit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                } else {
                    Text(
                        text = "Edit Unit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }

            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PManagerAddUnitFormPreview() {
    val viewModel: PManagerAddUnitScreenViewModel = viewModel()
    Tenant_careTheme {
        PManagerAddUnitForm(
            unitId = null,
            numOfRooms = "Bedsitter",
            unitNameOrNumber = "Col A2",
            unitDescription = "New unit",
            monthlyRent = "",
            showSaveButton = false,
            updateNumOfRooms = {},
            updateUnitNameOrNumber = {},
            updateUnitDescription = {},
            updateUnitRent = {},
            saveUnit = {},
            uploadingStatus = UploadingStatus.INITIAL
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PManagerAddUnitScreenPreview() {
    Tenant_careTheme {
        PManagerAddUnitScreen(
            unitID = null,
            numOfRooms = "Bedsitter",
            unitNameOrNumber = "Col A2",
            unitDescription = "New unit",
            monthlyRent = "",
            showSaveButton = false,
            updateNumOfRooms = {},
            updateUnitNameOrNumber = {},
            updateUnitDescription = {},
            updateUnitRent = {},
            uploadingStatus = UploadingStatus.INITIAL,
            navigateToPreviousScreen = { /*TODO*/ },
            saveUnit = {}
        )
    }
}