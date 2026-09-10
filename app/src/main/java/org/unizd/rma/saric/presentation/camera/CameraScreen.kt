package org.unizd.rma.saric.presentation.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.unizd.rma.saric.pet.detail.PetDetailViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    context: Context,
    petId: Int,
    petDetailViewModel: PetDetailViewModel = hiltViewModel(),
    onPhotoTaken: (photoPath: String) -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(petId) {
        petDetailViewModel.loadPet(petId)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val photoPath = saveBitmapToFile(context, it)
            petDetailViewModel.updatePetImage(photoPath)
            onPhotoTaken(photoPath)
        }
    }

    // Launcher za traženje dopuštenja - ako korisnik dopusti, odmah otvara kameru
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        }
    }

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fotografiraj ljubimca") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Nazad")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        cameraLauncher.launch(null)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(Icons.Filled.PhotoCamera, "Fotografiraj")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pritisnite gumb za fotografiranje ljubimca")
        }
    }
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
    val directory = File(context.getExternalFilesDir(null), "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val filename = "pet_${System.currentTimeMillis()}.jpg"
    val file = File(directory, filename)
    file.outputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    }
    return file.absolutePath
}