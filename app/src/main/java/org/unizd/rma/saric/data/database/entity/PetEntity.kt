package org.unizd.rma.saric.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ime: String,
    val pasmina: String,
    val slikaLjubimca: String? = null,
    val vrstaZivotinje: String,
    val datumPosvojenja: Long
)