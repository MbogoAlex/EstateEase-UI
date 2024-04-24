package com.example.tenant_care.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tenant_care.R

// filter by name
@Composable
fun FilterByNumOfRoomsBox(
    selectedNumOfRooms: String?,
    onSelectNumOfRooms: (rooms: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var rooms = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var expanded by remember {
        mutableStateOf(false)
    }

    var icon: ImageVector
    if(expanded) {
        icon = Icons.Default.KeyboardArrowUp
    } else {
        icon = Icons.Default.KeyboardArrowDown
    }

    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .widthIn(min = 100.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "No. Rooms".takeIf { selectedNumOfRooms == null } ?: "$selectedNumOfRooms room".takeIf { selectedNumOfRooms?.toInt() == 1 } ?: "$selectedNumOfRooms rooms",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
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
                            onSelectNumOfRooms(i)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterByRoomNameBox(
    rooms: List<String>,
    selectedUnit: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var icon: ImageVector
    if(expanded) {
        icon = Icons.Default.KeyboardArrowUp
    } else {
        icon = Icons.Default.KeyboardArrowDown
    }
    Card(
        modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .widthIn(min = 100.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room name".takeIf { selectedUnit == null } ?: "$selectedUnit",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
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
                            onChangeSelectedUnitName(i)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UndoFilteringBox(
    unfilterUnits: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .clickable {
                unfilterUnits()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search"
            )
        }
    }
}

@Composable
fun SearchFieldForTenants(
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
                painter = painterResource(id = R.drawable.person),
                contentDescription = null
            )
        },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}