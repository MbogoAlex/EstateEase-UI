package com.example.tenant_care.pManagerViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun PManagerAddUnitScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: PManagerAddUnitScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            PManagerAddUnitScreenTopBar()
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                PManagerAddUnitForm(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun PManagerAddUnitForm(
    viewModel: PManagerAddUnitScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var items = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

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
                        if (uiState.unitDetails.numOfRooms == 0) {
                            Text(text = "No. of rooms")
                        } else {
                            Text(text = "${uiState.unitDetails.numOfRooms} rooms")
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
                                    Text(text = "$item rooms")
                                },
                                onClick = {
                                    viewModel.updateNumOfRooms(item)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                //            Spacer(modifier = Modifier.height(20.dp))
                PManagerAddUnitInputField(
                    label = "Unit name / number",
                    value = uiState.unitDetails.unitNameOrNumber,
                    maxLines = 1,
                    onValueChange = {
                                    viewModel.updateUnitNameOrNumber(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                PManagerAddUnitInputField(
                    label = "Unit description",
                    value = uiState.unitDetails.unitDescription,
                    maxLines = 3,
                    onValueChange = {
                                    viewModel.updateUnitDescription(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                PManagerAddUnitInputField(
                    label = "Monthly rent",
                    value = uiState.unitDetails.monthlyRent.toString(),
                    maxLines = 1,
                    onValueChange = {
                        viewModel.updateUnitRent(it.toDouble())
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
            enabled = uiState.showSaveButton,
            onSaveUnitButtonClicked = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
    }
}

@Composable
fun AddUnitButton(
    enabled: Boolean,
    onSaveUnitButtonClicked: () -> Unit,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PManagerAddUnitScreenTopBar(
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
                Text(
                    text = "Add Unit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
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
            viewModel = viewModel
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PManagerAddUnitScreenPreview() {
    Tenant_careTheme {
        PManagerAddUnitScreen()
    }
}