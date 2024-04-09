package com.example.tenant_care.pManagerViews.unitsManagementViews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tenant_care.R
import com.example.tenant_care.ui.theme.Tenant_careTheme

@Composable
fun UnoccupiedUnitsComposable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        UnoccupiedUnitsScreen()
    }
}

@Composable
fun UnoccupiedUnitsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchFieldForUnoccupiedUnits(
            labelText = "Search room name",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            FilterUnoccupiedUnitsByNumOfRoomsBox()
            Spacer(modifier = Modifier.weight(1f))
            FilterUnoccupiedUnitsByNameBox()
            Spacer(modifier = Modifier.weight(1f))
            UndoFilteringForUnoccupiedUnitsBox()
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(10) {
                UnoccupiedUnitItem(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                )
            }
        }
    }
}


@Composable
fun UnoccupiedUnitItem(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column(

            ) {
                Row {
                    Text(text = "Room No: ")
                    Text(
                        text = "Col C2",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text(text = "Uploaded on: ")
                    Text(
                        text = "2023/04/05 10:44AM",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Unit actions"
                )
            }
        }
    }
}

@Composable
fun FilterUnoccupiedUnitsByNumOfRoomsBox(
    modifier: Modifier = Modifier
) {
    var rooms = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedNumOfRooms by remember {
        mutableIntStateOf(0)
    }
    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
    ) {
        Column {
            Text(
                text = "No. Rooms".takeIf { selectedNumOfRooms == 0 } ?: "$selectedNumOfRooms room".takeIf { selectedNumOfRooms == 1 } ?: "$selectedNumOfRooms rooms",
                modifier = Modifier
                    .padding(10.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                rooms.forEachIndexed { index, i ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$i room".takeIf { i == 1 } ?: "$i rooms"
                            )
                        },
                        onClick = {
                            selectedNumOfRooms = i
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterUnoccupiedUnitsByNameBox(
    modifier: Modifier = Modifier
) {
    var rooms = listOf<String>("A1", "A2", "A3", "A4", "A5")
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedUnit by remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
    ) {
        Column {
            Text(
                text = "Room name".takeIf { selectedUnit.isEmpty() } ?: selectedUnit,
                modifier = Modifier
                    .padding(10.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                rooms.forEachIndexed { index, i ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = i
                            )
                        },
                        onClick = {
                            selectedUnit = i
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UndoFilteringForUnoccupiedUnitsBox(
    modifier: Modifier = Modifier
) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Unfilter")
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search"
            )
        }
    }
}

@Composable
fun SearchFieldForUnoccupiedUnits(
    labelText: String,
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        label = {
            Text(text = labelText)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.house),
                contentDescription = null
            )
        },
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun UnOccupiedUnitDetails(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = { /*TODO*/ }) {
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
                    text = "Col C2",
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
                    Text(text = "2024/05/08 12:08")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "No. Rooms: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "4")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Monthly rent: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Ksh25,000")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Current tenant: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Agnes Njoki")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Tenant since: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "2024/05/08 12:08")
                }

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Assign Tenant")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun UnOccupiedUnitDetailsPreview() {
    Tenant_careTheme {
        UnOccupiedUnitDetails()
    }
}