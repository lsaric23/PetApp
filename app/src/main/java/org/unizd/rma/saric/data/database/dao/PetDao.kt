package org.unizd.rma.saric.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prezime.petapp.data.database.entity.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets ORDER BY id DESC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getPetById(id: Int): PetEntity?

    @Insert
    suspend fun insertPet(petEntity: PetEntity): Long

    @Update
    suspend fun updatePet(petEntity: PetEntity)

    @Delete
    suspend fun deletePet(petEntity: PetEntity)

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun deletePetById(id: Int)

    @Query("SELECT * FROM pets WHERE ime LIKE '%' || :query || '%' OR pasmina LIKE '%' || :query || '%'")
    fun searchPets(query: String): Flow<List<PetEntity>>
}