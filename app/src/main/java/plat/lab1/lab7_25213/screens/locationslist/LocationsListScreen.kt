package plat.lab1.lab7_25213.screens.locationslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.lab7_25213.data.Location
import plat.lab1.lab7_25213.ui.components.ErrorLayout
import plat.lab1.lab7_25213.ui.components.LoadingLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationsListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

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
            else -> LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(state.data) { location ->
                    LocationRow(
                        location = location,
                        onClick = { onLocationClick(location.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun LocationRow(
    location: Location,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = location.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
