package plat.lab1.lab7_25213.screens.characterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.lab7_25213.data.model.Character
import plat.lab1.lab7_25213.ui.components.CharacterImage
import plat.lab1.lab7_25213.ui.components.ErrorLayout
import plat.lab1.lab7_25213.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = viewModel(factory = CharacterDetailViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onBackClick()
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CharacterDetailEvent.OnDeleteDismiss) },
            title = { Text("Eliminar personaje") },
            text = { Text("¿Seguro que quieres eliminar este personaje?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CharacterDetailEvent.OnDeleteConfirm) }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(CharacterDetailEvent.OnDeleteDismiss) }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    state.data?.let { character ->
                        IconButton(onClick = { viewModel.onEvent(CharacterDetailEvent.OnFavoriteClick) }) {
                            Icon(
                                imageVector = if (character.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Filled.FavoriteBorder
                                },
                                contentDescription = "Favorito"
                            )
                        }
                        IconButton(onClick = { onEditClick(character.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar"
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(CharacterDetailEvent.OnDeleteClick) }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        when {
            state.hasError -> ErrorLayout(
                message = "Error al obtener personaje.\nIntenta de nuevo",
                onRetryClick = { viewModel.onEvent(CharacterDetailEvent.OnRetryClick) },
                modifier = Modifier.padding(innerPadding)
            )
            state.isLoading -> LoadingLayout(
                onClick = { viewModel.onEvent(CharacterDetailEvent.OnLoadingClick) },
                modifier = Modifier.padding(innerPadding)
            )
            else -> state.data?.let { character ->
                CharacterDetailContent(
                    character = character,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(
    character: Character,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CharacterImage(
            image = character.image,
            name = character.name,
            size = 150.dp
        )
        Text(
            text = character.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        DetailRow(label = "Species:", value = character.species)
        HorizontalDivider()
        DetailRow(label = "Status:", value = character.status)
        HorizontalDivider()
        DetailRow(label = "Gender:", value = character.gender)
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
