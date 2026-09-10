package org.unizd.rma.saric.domain.repositories

import org.unizd.rma.saric.domain.models.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    // Dohvati sve ljubimce
    fun getAllPets(): Flow<List<Pet>>

    // Dohvati jednog ljubimca po ID-u
    suspend fun getSinglePet(id: Int): Pet?

    // Dodaj novog ljubimca
    suspend fun addNewPet(pet: Pet): Long

    // Ažuriraj postojećeg ljubimca
    suspend fun updatePet(pet: Pet)

    // Obriši ljubimca po ID-u
    suspend fun deletePet(id: Int)

    // Pretraži ljubimce
    fun searchPets(query: String): Flow<List<Pet>>
}