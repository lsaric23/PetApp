package org.unizd.rma.saric.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.usecases.pet.DeletePetUseCase
import org.unizd.rma.saric.domain.usecases.pet.GetPetsUseCase
import org.unizd.rma.saric.domain.usecases.pet.SearchPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val searchPetsUseCase: SearchPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PetsUiState>(PetsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet = _selectedPet.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _uiState.value = PetsUiState.Loading
            try {
                getPetsUseCase().collect { pets ->
                    _uiState.value = if (pets.isEmpty()) {
                        PetsUiState.Empty
                    } else {
                        PetsUiState.Success(pets)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PetsUiState.Error(e.message ?: "Greška")
            }
        }
    }

    fun deletePet(id: Int) {
        viewModelScope.launch {
            deletePetUseCase(id)
                .onSuccess { }
                .onFailure { error ->
                    _uiState.value = PetsUiState.Error(
                        error.message ?: "Greška pri brisanju"
                    )
                }
        }
    }

    fun selectPet(pet: Pet) {
        _selectedPet.value = pet
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

        if (query.isEmpty()) {
            loadPets()
        } else {
            viewModelScope.launch {
                try {
                    searchPetsUseCase(query).collect { pets ->
                        _uiState.value = if (pets.isEmpty()) {
                            PetsUiState.Empty
                        } else {
                            PetsUiState.Success(pets)
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = PetsUiState.Error(
                        e.message ?: "Greška pri pretrazi"
                    )
                }
            }
        }
    }

    fun clearError() {
        loadPets()
    }
}