package org.unizd.rma.saric.domain.usecases.pet

import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val repository: PetRepository
) {
    operator fun invoke(): Flow<List<Pet>> {
        return repository.getAllPets()
    }
}