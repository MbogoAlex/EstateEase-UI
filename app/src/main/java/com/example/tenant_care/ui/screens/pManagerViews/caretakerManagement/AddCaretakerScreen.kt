package com.example.tenant_care.ui.screens.pManagerViews.caretakerManagement

import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.EditAlertDialog
import com.example.tenant_care.util.ExecutionStatus

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCaretakerScreenComposable(
    navigateToHomeScreenWithArgs: (childScreen: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: AddCaretakerScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showAddCaretakerDialog by remember {
        mutableStateOf(false)
    }

    if(uiState.executionStatus == ExecutionStatus.SUCCESS) {
        Toast.makeText(context, "Caretaker added", Toast.LENGTH_LONG).show()
        navigateToHomeScreenWithArgs("caretakers-screen")
        viewModel.resetExecutionStatus()
    } else if(uiState.executionStatus == ExecutionStatus.FAILURE) {
        Toast.makeText(context, "Failed. Try again later", Toast.LENGTH_LONG).show()
        viewModel.resetExecutionStatus()
    }

    if(showAddCaretakerDialog) {
        EditAlertDialog(
            title = "Add caretaker",
            onConfirm = {
                showAddCaretakerDialog = !showAddCaretakerDialog
                viewModel.addCaretaker()
            },
            onDismissRequest = { showAddCaretakerDialog = !showAddCaretakerDialog }
        )
    }

    Box(
        modifier = modifier
    ) {
        AddCaretakerScreen(
            name = uiState.name,
            idNo = uiState.natId,
            phoneNumber = uiState.phoneNumber,
            email = uiState.email,
            password = uiState.password,
            salary = uiState.salary,
            onChangeName = {
                viewModel.updateName(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onChangeIdNo = {
                viewModel.updateNatId(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onChangeEmail = {
                viewModel.updateEmail(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onChangePhoneNumber = {
                viewModel.updatePhone(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onChangePassword = {
                viewModel.updatePassword(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onChangeSalary = {
                viewModel.updateSalary(it)
                viewModel.checkIfRequiredFieldsAreFilled()
            },
            onAddCaretaker = {
                showAddCaretakerDialog = !showAddCaretakerDialog
            },
            addCaretakerButtonEnabled = uiState.addButtonEnabled,
            executionStatus = uiState.executionStatus
        )
    }
}

@Composable
fun AddCaretakerScreen(
    name: String,
    idNo: String,
    phoneNumber: String,
    email: String,
    password: String,
    salary: String,
    onChangeName: (value: String) -> Unit,
    onChangeIdNo: (value: String) -> Unit,
    onChangeEmail: (value: String) -> Unit,
    onChangePhoneNumber: (value: String) -> Unit,
    onChangePassword: (value: String) -> Unit,
    onChangeSalary: (value: String) -> Unit,
    onAddCaretaker: () -> Unit,
    executionStatus: ExecutionStatus,
    addCaretakerButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Add caretaker",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        AddCaretakerInputField(
            name = name,
            idNo = idNo,
            phoneNumber = phoneNumber,
            email = email,
            password = password,
            salary = salary,
            onChangeName = onChangeName,
            onChangeIdNo = onChangeIdNo,
            onChangeEmail = onChangeEmail,
            onChangePhoneNumber = onChangePhoneNumber,
            onChangePassword = onChangePassword,
            onChangeSalary = onChangeSalary
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = addCaretakerButtonEnabled && executionStatus != ExecutionStatus.LOADING,
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onAddCaretaker
        ) {
            if(executionStatus == ExecutionStatus.LOADING) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Add caretaker"
                )
            }
        }
    }
}

@Composable
fun AddCaretakerInputField(
    name: String,
    idNo: String,
    phoneNumber: String,
    email: String,
    password: String,
    salary: String,
    onChangeName: (value: String) -> Unit,
    onChangeIdNo: (value: String) -> Unit,
    onChangeEmail: (value: String) -> Unit,
    onChangePhoneNumber: (value: String) -> Unit,
    onChangePassword: (value: String) -> Unit,
    onChangeSalary: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            InputField(
                label = "Name",
                value = name,
                onValueChange = onChangeName,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(
                label = "Id. No",
                value = idNo,
                onValueChange = onChangeIdNo,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(
                label = "Phone",
                value = phoneNumber,
                onValueChange = onChangePhoneNumber,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(
                label = "Email",
                value = email,
                onValueChange = onChangeEmail,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(
                label = "Password",
                value = password,
                onValueChange = onChangePassword,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(
                label = "Salary",
                value = salary,
                onValueChange = onChangeSalary,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
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

@Preview(showBackground = true)
@Composable
fun AddCaretakerScreenPreview() {
    Tenant_careTheme {
        AddCaretakerScreen(
            name = "",
            idNo = "",
            phoneNumber = "",
            email = "",
            password = "",
            salary = "",
            onChangeName = {},
            onChangeIdNo = {},
            onChangeEmail = {},
            onChangePhoneNumber = {},
            onChangePassword = {},
            addCaretakerButtonEnabled = false,
            executionStatus = ExecutionStatus.INITIAL,
            onAddCaretaker = {},
            onChangeSalary = {}
        )
    }
}