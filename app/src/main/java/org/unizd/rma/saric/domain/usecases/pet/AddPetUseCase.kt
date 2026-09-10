package org.unizd.rma.saric.domain.usecases.pet

import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import javax.inject.Inject

class AddPetUseCase @Inject constructor(
    private val repository: PetRepository
) {
    suspend operator fun invoke(pet: Pet): Result<Long> {
        return try {
            val id = repository.addNewPet(pet)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}