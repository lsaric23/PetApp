package org.unizd.rma.saric.presentation

import org.unizd.rma.saric.domain.models.Pet

sealed class PetsUiState {
    object Loading : PetsUiState()
    data class Success(val pets: List<Pet>) : PetsUiState()
    data class Error(val message: String) : PetsUiState()
    object Empty : PetsUiState()
}