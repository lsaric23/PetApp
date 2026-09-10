package org.unizd.rma.saric.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.usecases.pet.DeletePetUseCase
import org.unizd.rma.saric.domain.usecases.pet.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val deletePetUseCase: DeletePetUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet = _selectedPet.asStateFlow()

    val uiState: StateFlow<PetsUiState> = combine(
        getPetsUseCase(),
        _searchQuery
    ) { petsList, query ->
        val filtered = if (query.isBlank()) {
            petsList
        } else {
            petsList.filter { pet ->
                pet.ime.contains(query, ignoreCase = true) ||
                        pet.pasmina.contains(query, ignoreCase = true) ||
                        pet.vrstaZivotinje.contains(query, ignoreCase = true)
            }
        }

        if (filtered.isEmpty()) {
            PetsUiState.Empty
        } else {
            PetsUiState.Success(filtered)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PetsUiState.Loading
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPet(pet: Pet) {
        _selectedPet.value = pet
    }

    fun deletePet(id: Int) {
        viewModelScope.launch {
            deletePetUseCase(id)
        }
    }


    fun clearError() {
        // pomoćna metoda
    }
}