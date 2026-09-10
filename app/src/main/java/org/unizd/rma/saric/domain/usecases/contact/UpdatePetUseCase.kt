package org.unizd.rma.saric.domain.usecases.contact

import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import javax.inject.Inject

class UpdatePetUseCase @Inject constructor(
    private val repository: PetRepository
) {
    suspend operator fun invoke(pet: Pet): Result<Unit> {
        return try {
            repository.updatePet(pet)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}