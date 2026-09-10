package org.unizd.rma.saric.pet.create

sealed class AddPetUiState {
    object Idle : AddPetUiState()
    object Loading : AddPetUiState()
    object Success : AddPetUiState()
    data class Error(val message: String) : AddPetUiState()
}