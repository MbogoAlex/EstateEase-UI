package com.example.tenant_care.ui.screens.caretakerViews.meterReading

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tenant_care.R
import com.example.tenant_care.model.caretaker.WaterMeterDt
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.waterMeterData
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditMeterReadingScreenComposable(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var capturedImageUri by remember {
        mutableStateOf<Uri?>(null)
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
                capturedImageUri = imageUri
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
           waterMeterDt = waterMeterData,
           onEditUnits = {},
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
           uploadedImageUri = capturedImageUri,
           navigateToPreviousScreen = { /*TODO*/ }
       )
    }
}

@Composable
fun EditMeterReadingScreen(
    waterMeterDt: WaterMeterDt,
    onEditUnits: () -> Unit,
    onImageUpload: () -> Unit,
    uploadText: String,
    uploadedImageUri: Uri?,
    navigateToPreviousScreen: () -> Unit,
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
            Text(text = "Col A2")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tenant: ",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Alex Mbogo")
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
                if(waterMeterDt.waterUnits != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${waterMeterDt.waterUnits} units",
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = onEditUnits) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit units"
                            )
                        }
                    }
                } else {
                    TextButton(onClick = onEditUnits) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit units"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(uploadedImageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(uploadedImageUri)
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
                ImageUpload(
                    onImageUpload = onImageUpload,
                    uploadText = uploadText,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Previous reading (units):",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("")
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

@Preview(showBackground = true)
@Composable
fun EditMeterReadingScreenPreview() {
    Tenant_careTheme {
        EditMeterReadingScreen(
            waterMeterDt = waterMeterData,
            onEditUnits = {},
            onImageUpload = {},
            uploadText = "Meter reading",
            uploadedImageUri = null,
            navigateToPreviousScreen = {}
        )
    }
}