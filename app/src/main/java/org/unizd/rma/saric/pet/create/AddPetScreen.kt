package org.unizd.rma.saric.pet.create

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.unizd.rma.saric.domain.models.Pet
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: AddPetViewModel = hiltViewModel(),
    pet: Pet? = null,
    petIdForEdit: Int? = null,
    onBackClick: () -> Unit = {},
    onSaveSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ime by viewModel.ime.collectAsStateWithLifecycle()
    val pasmina by viewModel.pasmina.collectAsStateWithLifecycle()
    val vrstaZivotinje by viewModel.vrstaZivotinje.collectAsStateWithLifecycle()
    val datumPosvojenja by viewModel.datumPosvojenja.collectAsStateWithLifecycle()

    var expandedDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(petIdForEdit) {
        if (petIdForEdit != null) {
            viewModel.loadPetForEdit(petIdForEdit)
        }
    }

    LaunchedEffect(pet) {
        pet?.let {
            viewModel.loadPetForEditDirect(it)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AddPetUiState.Success) {
            onSaveSuccess()
        }
    }

    // DatePickerDialog za odabir datuma posvojenja (bez sati)
    val calendar = Calendar.getInstance()
    if (datumPosvojenja != 0L) {
        calendar.timeInMillis = datumPosvojenja
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            viewModel.setDatumPosvojenja(selectedCalendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (petIdForEdit != null) "Uredi ljubimca" else "Dodaj ljubimca"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Nazad")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (uiState) {
                is AddPetUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AddPetUiState.Error -> {
                    Text(
                        text = (uiState as AddPetUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                else -> {
                    // Ime ljubimca (Znakovni tip 1)
                    OutlinedTextField(
                        value = ime,
                        onValueChange = { viewModel.setIme(it) },
                        label = { Text("Ime ljubimca*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    // Pasmina (Znakovni tip 2)
                    OutlinedTextField(
                        value = pasmina,
                        onValueChange = { viewModel.setPasmina(it) },
                        label = { Text("Pasmina / Vrsta*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    // Vrsta životinje (Fiksni popis - Exposed Dropdown Menu)
                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = { expandedDropdown = !expandedDropdown },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = vrstaZivotinje,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vrsta životinje*") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false }
                        ) {
                            AddPetViewModel.vrsteZivotinjaPopis.forEach { opcija ->
                                DropdownMenuItem(
                                    text = { Text(opcija) },
                                    onClick = {
                                        viewModel.setVrstaZivotinje(opcija)
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Datum posvojenja (Datum bez sati)
                    val formattedDate = if (datumPosvojenja != 0L) {
                        SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(Date(datumPosvojenja))
                    } else ""

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = formattedDate,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Datum posvojenja*") },
                            trailingIcon = {
                                Icon(Icons.Filled.DateRange, contentDescription = "Odaberi datum")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { datePickerDialog.show() }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.savePet() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Spremi")
                    }
                }
            }
        }
    }
}