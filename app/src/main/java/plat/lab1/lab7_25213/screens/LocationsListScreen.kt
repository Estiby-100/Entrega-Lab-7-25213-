package plat.lab1.lab7_25213.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import plat.lab1.lab7_25213.data.Location
import plat.lab1.lab7_25213.data.LocationDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val locations = remember { LocationDb().getAllLocations() }

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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(locations) { location ->
                LocationRow(
                    location = location,
                    onClick = { onLocationClick(location.id) }
                )
                HorizontalDivider()
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
