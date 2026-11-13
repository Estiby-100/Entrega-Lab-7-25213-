package plat.lab1.lab7_25213.screens.characterform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterFormViewModel = viewModel(factory = CharacterFormViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBackClick()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Edit character" else "New character") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            FormField(
                value = state.name,
                label = "Nombre",
                onValueChange = { viewModel.onEvent(CharacterFormEvent.OnNameChange(it)) }
            )
            FormField(
                value = state.status,
                label = "Estado",
                onValueChange = { viewModel.onEvent(CharacterFormEvent.OnStatusChange(it)) }
            )
            FormField(
                value = state.species,
                label = "Especie",
                onValueChange = { viewModel.onEvent(CharacterFormEvent.OnSpeciesChange(it)) }
            )
            FormField(
                value = state.gender,
                label = "Género",
                onValueChange = { viewModel.onEvent(CharacterFormEvent.OnGenderChange(it)) }
            )
            FormField(
                value = state.image,
                label = "URL de imagen (opcional)",
                onValueChange = { viewModel.onEvent(CharacterFormEvent.OnImageChange(it)) }
            )
            Button(
                onClick = { viewModel.onEvent(CharacterFormEvent.OnSaveClick) },
                enabled = state.name.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Guardar")
            }
        }
    }
}

@Composable
private fun FormField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}
