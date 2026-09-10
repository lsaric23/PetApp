package org.unizd.rma.saric.pet.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.unizd.rma.saric.domain.models.Pet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: Int,
    viewModel: PetDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditClick: (Pet) -> Unit = {},
    onPhotoClick: () -> Unit = {}
) {
    val pet by viewModel.pet.collectAsStateWithLifecycle()

    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji ljubimca") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Nazad")
                    }
                },
                actions = {
                    pet?.let {
                        IconButton(onClick = { onEditClick(it) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Uredi")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        pet?.let { petItem ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!petItem.slikaLjubimca.isNullOrEmpty()) {
                    AsyncImage(
                        model = petItem.slikaLjubimca,
                        contentDescription = petItem.ime,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(MaterialTheme.shapes.large),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(200.dp),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = petItem.ime.firstOrNull()?.uppercase() ?: "L",
                                fontSize = 80.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PetInfoCard(label = "Ime ljubimca", value = petItem.ime)

                PetInfoCard(label = "Pasmina / Vrsta", value = petItem.pasmina)

                PetInfoCard(label = "Vrsta životinje", value = petItem.vrstaZivotinje)

                val formattedDate = SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault())
                    .format(Date(petItem.datumPosvojenja))
                PetInfoCard(label = "Datum posvojenja", value = formattedDate)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onPhotoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        Icons.Filled.PhotoCamera,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Uslikaj ljubimca kamerom")
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PetInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}