package org.unizd.rma.saric.data.repository

import org.unizd.rma.saric.data.database.dao.PetDao
import org.unizd.rma.saric.data.mappers.toDomain
import org.unizd.rma.saric.data.mappers.toEntity
import org.unizd.rma.saric.domain.models.Pet
import org.unizd.rma.saric.domain.repositories.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PetRepositoryImpl(
    private val petDao: PetDao
) : PetRepository {

    override fun getAllPets(): Flow<List<Pet>> =
        petDao.getAllPets().map { petEntities ->
            petEntities.map { it.toDomain() }
        }

    override suspend fun getSinglePet(id: Int): Pet? =
        petDao.getPetById(id)?.toDomain()

    override suspend fun addNewPet(pet: Pet): Long =
        petDao.insertPet(pet.toEntity())

    override suspend fun updatePet(pet: Pet) =
        petDao.updatePet(pet.toEntity())

    override suspend fun deletePet(id: Int) =
        petDao.deletePetById(id)

    override fun searchPets(query: String): Flow<List<Pet>> =
        petDao.searchPets(query).map { petEntities ->
            petEntities.map { it.toDomain() }
        }
}