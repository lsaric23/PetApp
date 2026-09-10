package org.unizd.rma.saric.domain.usecases.pet

import org.unizd.rma.saric.domain.repositories.PetRepository
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val repository: PetRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return try {
            repository.deletePet(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}