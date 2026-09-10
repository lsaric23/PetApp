package org.unizd.rma.saric.pet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.presentation.PetsViewModel
import org.unizd.rma.saric.presentation.components.EmptyScreen
import org.unizd.rma.saric.presentation.components.ErrorScreen
import org.unizd.rma.saric.presentation.components.PetsList
import org.unizd.rma.saric.presentation.components.SearchBar
import org.unizd.rma.saric.presentation.PetsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(
    viewModel: PetsViewModel = hiltViewModel(),
    onAddClick: () -> Unit = {},
    onPetClick: (Pet) -> Unit = {},
    onNavigateToDetail: (Pet) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moji ljubimci") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj ljubimca")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tražilica
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Prikaz sadržaja ovisno o stanju (UiState)
            when (uiState) {
                is PetsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PetsUiState.Success -> {
                    PetsList(
                        pets = (uiState as PetsUiState.Success).pets,
                        onPetClick = { pet ->
                            viewModel.selectPet(pet)
                            onNavigateToDetail(pet)
                        },
                        onDeleteClick = { pet ->
                            viewModel.deletePet(pet.id)
                        }
                    )
                }

                is PetsUiState.Error -> {
                    ErrorScreen(
                        message = (uiState as PetsUiState.Error).message,
                        onRetry = viewModel::clearError
                    )
                }

                is PetsUiState.Empty -> {
                    EmptyScreen(onAddClick = onAddClick)
                }
            }
        }
    }
}