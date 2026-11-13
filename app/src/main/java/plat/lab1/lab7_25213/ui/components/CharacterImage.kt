package plat.lab1.lab7_25213.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage

@Composable
fun CharacterImage(
    image: String,
    name: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    if (image.isBlank()) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = name,
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(size)
        )
    } else {
        AsyncImage(
            model = image,
            contentDescription = name,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    }
}
