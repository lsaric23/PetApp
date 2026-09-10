package org.unizd.rma.saric.pet.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import org.unizd.rma.saric.domain.usecases.pet.AddPetUseCase
import org.unizd.rma.saric.domain.usecases.pet.UpdatePetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val repository: PetRepository
) : ViewModel() {

    companion object {
        val vrsteZivotinjaPopis = listOf(
            "Pas",
            "Mačka",
            "Ptica",
            "Glodavac",
            "Gmaz",
            "Akvarijska životinja"
        )
    }

    private val _uiState = MutableStateFlow<AddPetUiState>(AddPetUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _ime = MutableStateFlow("")
    val ime = _ime.asStateFlow()

    private val _pasmina = MutableStateFlow("")
    val pasmina = _pasmina.asStateFlow()

    private val _vrstaZivotinje = MutableStateFlow(vrsteZivotinjaPopis.first())
    val vrstaZivotinje = _vrstaZivotinje.asStateFlow()

    private val _slikaLjubimca = MutableStateFlow<String?>(null)
    val slikaLjubimca = _slikaLjubimca.asStateFlow()

    private val _datumPosvojenja = MutableStateFlow(System.currentTimeMillis())
    val datumPosvojenja = _datumPosvojenja.asStateFlow()

    private var petId: Int? = null

    fun setIme(ime: String) {
        _ime.value = ime
    }

    fun setPasmina(pasmina: String) {
        _pasmina.value = pasmina
    }

    fun setVrstaZivotinje(vrsta: String) {
        _vrstaZivotinje.value = vrsta
    }

    fun setSlikaLjubimca(uri: String?) {
        _slikaLjubimca.value = uri
    }

    fun setDatumPosvojenja(timestamp: Long) {
        _datumPosvojenja.value = timestamp
    }

    fun loadPetForEdit(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pet = repository.getSinglePet(id)
                if (pet != null) {
                    _ime.value = pet.ime
                    _pasmina.value = pet.pasmina
                    _vrstaZivotinje.value = pet.vrstaZivotinje
                    _slikaLjubimca.value = pet.slikaLjubimca
                    _datumPosvojenja.value = pet.datumPosvojenja
                    petId = pet.id
                }
            } catch (e: Exception) {
                _uiState.value = AddPetUiState.Error("Greška pri učitavanju ljubimca: ${e.message}")
            }
        }
    }

    fun loadPetForEditDirect(pet: Pet) {
        _ime.value = pet.ime
        _pasmina.value = pet.pasmina
        _vrstaZivotinje.value = pet.vrstaZivotinje
        _slikaLjubimca.value = pet.slikaLjubimca
        _datumPosvojenja.value = pet.datumPosvojenja
        petId = pet.id
    }

    fun savePet() {
        if (!validateForm()) {
            _uiState.value = AddPetUiState.Error("Popunite sva obavezna polja")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddPetUiState.Loading

            val pet = Pet(
                id = petId ?: 0,
                ime = _ime.value,
                pasmina = _pasmina.value,
                vrstaZivotinje = _vrstaZivotinje.value,
                slikaLjubimca = _slikaLjubimca.value,
                datumPosvojenja = _datumPosvojenja.value
            )

            val result = if (petId != null) {
                updatePetUseCase(pet)
            } else {
                addPetUseCase(pet).map { Unit }
            }

            result
                .onSuccess {
                    _uiState.value = AddPetUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = AddPetUiState.Error(error.message ?: "Greška pri spremanju")
                }
        }
    }

    private fun validateForm(): Boolean {
        return _ime.value.isNotBlank() &&
                _pasmina.value.isNotBlank() &&
                _vrstaZivotinje.toString().isNotBlank() &&
                _datumPosvojenja.value > 0
    }

    fun resetState() {
        _uiState.value = AddPetUiState.Idle
    }
}