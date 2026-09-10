package org.unizd.rma.saric.domain.usecases.pet

import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPetsUseCase @Inject constructor(
    private val repository: PetRepository
) {
    operator fun invoke(query: String): Flow<List<Pet>> {
        return repository.searchPets(query)
    }
}