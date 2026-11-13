package plat.lab1.lab7_25213.screens.locationslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.lab7_25213.data.model.Location
import plat.lab1.lab7_25213.ui.components.EmptyLayout
import plat.lab1.lab7_25213.ui.components.ErrorLayout
import plat.lab1.lab7_25213.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationsListViewModel = viewModel(factory = LocationsListViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()

    val locations = when (state.filter) {
        LocationFilter.ALL -> state.data
        LocationFilter.FAVORITES -> state.data.filter { it.isFavorite }
        LocationFilter.NOT_FAVORITES -> state.data.filter { !it.isFavorite }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        when {
            state.hasError -> ErrorLayout(
                message = "Error al obtener listado de ubicaciones.\nIntenta de nuevo",
                onRetryClick = { viewModel.onEvent(LocationsListEvent.OnRetryClick) },
                modifier = Modifier.padding(innerPadding)
            )
            state.isLoading -> LoadingLayout(
                onClick = { viewModel.onEvent(LocationsListEvent.OnLoadingClick) },
                modifier = Modifier.padding(innerPadding)
            )
            else -> Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LocationFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = state.filter == filter,
                            onClick = { viewModel.onEvent(LocationsListEvent.OnFilterChange(filter)) },
                            label = { Text(filter.label) }
                        )
                    }
                }

                if (locations.isEmpty()) {
                    EmptyLayout(message = "No hay elementos para mostrar")
                } else {
                    LazyColumn {
                        items(locations) { location ->
                            LocationRow(
                                location = location,
                                onClick = { onLocationClick(location.id) },
                                onFavoriteClick = {
                                    viewModel.onEvent(
                                        LocationsListEvent.OnFavoriteClick(
                                            id = location.id,
                                            isFavorite = location.isFavorite
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
    }
}

@Composable
private fun LocationRow(
    location: Location,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (location.isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Filled.FavoriteBorder
                },
                contentDescription = "Favorita",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
