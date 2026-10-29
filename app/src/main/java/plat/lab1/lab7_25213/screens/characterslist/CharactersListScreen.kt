package plat.lab1.lab7_25213.screens.characterslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import plat.lab1.lab7_25213.data.Character
import plat.lab1.lab7_25213.ui.components.EmptyLayout
import plat.lab1.lab7_25213.ui.components.ErrorLayout
import plat.lab1.lab7_25213.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val characters = if (state.showOnlyFavorites) {
        state.data.filter { it.isFavorite }
    } else {
        state.data
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(CharactersListEvent.OnFilterClick) }) {
                        Icon(
                            imageVector = if (state.showOnlyFavorites) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            },
                            contentDescription = "Filtrar favoritos"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        when {
            state.hasError -> ErrorLayout(
                message = "Error al obtener listado de personajes.\nIntenta de nuevo",
                onRetryClick = { viewModel.onEvent(CharactersListEvent.OnRetryClick) },
                modifier = Modifier.padding(innerPadding)
            )
            state.isLoading -> LoadingLayout(
                onClick = { viewModel.onEvent(CharactersListEvent.OnLoadingClick) },
                modifier = Modifier.padding(innerPadding)
            )
            characters.isEmpty() -> EmptyLayout(
                message = "No hay elementos para mostrar",
                modifier = Modifier.padding(innerPadding)
            )
            else -> LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(characters) { character ->
                    CharacterRow(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onFavoriteClick = {
                            viewModel.onEvent(
                                CharactersListEvent.OnFavoriteClick(
                                    id = character.id,
                                    isFavorite = character.isFavorite
                                )
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${character.species} - ${character.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = character.gender,
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (character.isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = "Favorito",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
