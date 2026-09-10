package org.unizd.rma.saric.presentation.util

import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionRequest(
    onPermissionGranted: () -> Unit = {}
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Dozvola za kameru") },
            text = { Text("Trebam dozvolu za pristup kameri radi slikanja ljubimca") },
            confirmButton = {
                Button(onClick = {
                    cameraPermissionState.launchPermissionRequest()
                    onPermissionGranted()
                }) {
                    Text("Dozvoli")
                }
            }
        )
    } else {
        onPermissionGranted()
    }
}