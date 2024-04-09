package com.example.tenant_care.pManagerViews.unitsManagementViews

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.nav.AppNavigation
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.ReusableFunctions

object UnOccupiedUnitDetailsComposableDestination: AppNavigation {
    override val title: String = "Unoccupied unit details screen"
    override val route: String = "unoccupied-unit-details-screen"
    val propertyId: String = "propertyId"
    val routeWithArgs = "$route/{$propertyId}"

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnoccupiedUnitDetailsComposable(
    navigateToPreviousScreen: () -> Unit,
    navigateToOccupiedUnitsScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
//    BackHandler(onBack = navigateToPreviousScreen)
    val context = LocalContext.current
    val viewModel: UnoccupiedUnitDetailsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    if(uiState.assignmentStatus == AssignmentStatus.SUCCESS) {
        Toast.makeText(context, "Unit assigned to tenant successfully", Toast.LENGTH_SHORT).show()
        navigateToOccupiedUnitsScreen()
        viewModel.resetAssignmentStatus()
    }
    Box {
        if(uiState.showUnitAssignmentScreen) {
            UnitAssignmentScreen(
                assignmentStatus = uiState.assignmentStatus,
                unitName = uiState.propertyUnit.propertyNumberOrName,
                monthlyRent = ReusableFunctions.formatMoneyValue(uiState.propertyUnit.monthlyRent),
                fullName = uiState.tenantName,
                onFullNameChanged = {
                    viewModel.updateTenantName(it)
                },
                identificationNumber = uiState.nationalIdOrPassportNumber,
                onIdentificationNumberChanged = {
                    viewModel.updateTenantIdOrPassport(it)
                },
                phoneNumber = uiState.phoneNumber,
                onPhoneNumberChanged = {
                    viewModel.updateTenantPhoneNumber(it)
                },
                email = uiState.email,
                onEmailChanged = {
                    viewModel.updateTenantEmail(it)
                },
                password = uiState.password,
                onPasswordChanged = {
                    viewModel.updateTenantPassword(it)
                },
                assignmentButtonEnabled = uiState.assignmentButtonEnabled,
                numOfRooms = uiState.propertyUnit.numberOfRooms.toString(),
                cancelUnitAssignment = {
                    viewModel.toggleScreen()
                },
                confirmUnitAssignment = {
                    viewModel.assignUnitToTenant()
                }
            )
        } else {
            UnOccupiedUnitDetailsScreen(
                propertyUnit = uiState.propertyUnit,
                propertyAddedAt = uiState.propertyAddedAt,
                navigateToPreviousScreen = navigateToPreviousScreen,
                navigateToAssignmentScreen = {
                    viewModel.toggleScreen()
                }
            )
        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UnOccupiedUnitDetailsScreen(
    propertyUnit: PropertyUnit,
    propertyAddedAt: String,
    navigateToPreviousScreen: () -> Unit,
    navigateToAssignmentScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Text(
                    text = propertyUnit.propertyNumberOrName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Uploaded on: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyAddedAt)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "No. Rooms: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyUnit.numberOfRooms.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Monthly rent: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = ReusableFunctions.formatMoneyValue(propertyUnit.monthlyRent))
                }


            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navigateToAssignmentScreen() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Assign Tenant")
        }

    }
}
@Composable
fun UnitAssignmentScreen(
    assignmentStatus: AssignmentStatus,
    unitName: String,
    numOfRooms: String,
    monthlyRent: String,
    fullName: String,
    onFullNameChanged: (name: String) -> Unit,
    identificationNumber: String,
    onIdentificationNumberChanged: (number: String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChanged: (phoneNumber: String) -> Unit,
    email: String,
    onEmailChanged: (email: String) -> Unit,
    password: String,
    onPasswordChanged: (password: String) -> Unit,
    assignmentButtonEnabled: Boolean,
    cancelUnitAssignment: () -> Unit,
    confirmUnitAssignment: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = cancelUnitAssignment)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = { cancelUnitAssignment() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "Assign unit",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "EstateEase",
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "$unitName, $numOfRooms rooms",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Monthly rent: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = monthlyRent)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Tenant details: ",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        AssignUnitInputForm(
            fullName = fullName,
            onFullNameChanged = onFullNameChanged,
            identificationNumber = identificationNumber,
            onIdentificationNumberChanged =onIdentificationNumberChanged ,
            phoneNumber = phoneNumber,
            onPhoneNumberChanged = onPhoneNumberChanged,
            email = email,
            onEmailChanged = onEmailChanged,
            password = password,
            onPasswordChanged = onPasswordChanged
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = assignmentButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            ),
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { confirmUnitAssignment() }
        ) {
            if(assignmentStatus == AssignmentStatus.LOADING) {
                CircularProgressIndicator()
            } else {
                Text(text = "Confirm assignment")
            }

        }
    }

}

@Composable
fun AssignUnitInputForm(
    fullName: String,
    onFullNameChanged: (name: String) -> Unit,
    identificationNumber: String,
    onIdentificationNumberChanged: (number: String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChanged: (phoneNumber: String) -> Unit,
    email: String,
    onEmailChanged: (email: String) -> Unit,
    password: String,
    onPasswordChanged: (password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            AssignUnitInputField(
                label = "Fullname",
                value = fullName,
                maxLines = 1,
                onValueChange = onFullNameChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            AssignUnitInputField(
                label = "Nat.id / Passport No.",
                value = identificationNumber,
                maxLines = 1,
                onValueChange = onIdentificationNumberChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            AssignUnitInputField(
                label = "Phone number",
                value = phoneNumber,
                maxLines = 1,
                onValueChange = onPhoneNumberChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            AssignUnitInputField(
                label = "Email",
                value = email,
                maxLines = 1,
                onValueChange = onEmailChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            AssignUnitInputField(
                label = "Password",
                value = password,
                maxLines = 1,
                onValueChange = onPasswordChanged,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignUnitInputField(
    label: String,
    value: String,
    maxLines: Int,
    onValueChange: (value: String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        label = {
            Text(text = label)
        },
        maxLines = maxLines,
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

@Preview(showBackground = true)
@Composable
fun UnitAssignmentScreenPreview() {
    Tenant_careTheme {
        UnitAssignmentScreen(
            assignmentStatus = AssignmentStatus.INITIAL,
            unitName = "Col C2",
            numOfRooms = "3",
            monthlyRent = ReusableFunctions.formatMoneyValue(7500.0),
            fullName = "",
            onFullNameChanged = {},
            identificationNumber = "",
            onIdentificationNumberChanged = {},
            phoneNumber = "",
            onPhoneNumberChanged = {},
            email = "",
            onEmailChanged = {},
            password = "",
            onPasswordChanged = {},
            assignmentButtonEnabled = false,
            cancelUnitAssignment = {},
            confirmUnitAssignment = {}
        )
    }
}