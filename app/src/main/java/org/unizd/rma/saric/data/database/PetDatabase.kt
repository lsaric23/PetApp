package org.unizd.rma.saric.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.unizd.rma.saric.data.database.dao.PetDao
import org.unizd.rma.saric.data.database.entity.PetEntity

@Database(
    entities = [PetEntity::class],
    version = 1
)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        const val DATABASE_NAME = "pets_db"
    }
}