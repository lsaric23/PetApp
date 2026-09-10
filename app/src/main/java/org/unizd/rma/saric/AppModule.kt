package org.unizd.rma.saric

import android.content.Context
import androidx.room.Room
import org.unizd.rma.saric.data.database.PetDatabase
import org.unizd.rma.saric.data.database.dao.PetDao
import org.unizd.rma.saric.data.repository.PetRepositoryImpl
import org.unizd.rma.saric.domain.repositories.PetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePetDatabase(
        @ApplicationContext context: Context
    ): PetDatabase {
        return Room.databaseBuilder(
            context,
            PetDatabase::class.java,
            PetDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providePetDao(
        database: PetDatabase
    ): PetDao {
        return database.petDao()
    }

    @Singleton
    @Provides
    fun providePetRepository(
        dao: PetDao
    ): PetRepository {
        return PetRepositoryImpl(dao)
    }
}