package org.unizd.rma.saric.data.mappers

import org.unizd.rma.saric.data.database.entity.PetEntity
import org.unizd.rma.saric.domain.models.Pet

fun PetEntity.toDomain(): Pet = Pet(
    id = id,
    ime = ime,
    pasmina = pasmina,
    slikaLjubimca = slikaLjubimca,
    vrstaZivotinje = vrstaZivotinje,
    datumPosvojenja = datumPosvojenja
)

fun Pet.toEntity(): PetEntity = PetEntity(
    id = id,
    ime = ime,
    pasmina = pasmina,
    slikaLjubimca = slikaLjubimca,
    vrstaZivotinje = vrstaZivotinje,
    datumPosvojenja = datumPosvojenja
)