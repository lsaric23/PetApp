package org.unizd.rma.saric.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.unizd.rma.saric.domain.models.Pet

@Composable
fun PetsList(
    pets: List<Pet>,
    onPetClick: (Pet) -> Unit,
    onDeleteClick: (Pet) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(pets, key = { it.id }) { pet ->
            PetCard(
                pet = pet,
                onPetClick = { onPetClick(pet) },
                onDeleteClick = { onDeleteClick(pet) }
            )
        }
    }
}