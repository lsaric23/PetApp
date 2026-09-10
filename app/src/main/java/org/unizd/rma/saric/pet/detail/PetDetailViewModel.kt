package org.unizd.rma.saric.pet.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val repository: PetRepository
) : ViewModel() {

    private val _pet = MutableStateFlow<Pet?>(null)
    val pet = _pet.asStateFlow()

    fun loadPet(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pet.value = repository.getSinglePet(id)
        }
    }

    fun updatePetImage(imageUri: String) {
        _pet.value?.let { pet ->
            val updated = pet.copy(slikaLjubimca = imageUri)
            viewModelScope.launch(Dispatchers.IO) {
                repository.updatePet(updated)
                _pet.value = updated
            }
        }
    }
}