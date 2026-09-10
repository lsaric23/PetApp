package org.unizd.rma.saric.domain.models

data class Pet(
    val id: Int = 0,
    val ime: String,
    val pasmina: String,
    val slikaLjubimca: String? = null,
    val vrstaZivotinje: String,
    val datumPosvojenja: Long
)