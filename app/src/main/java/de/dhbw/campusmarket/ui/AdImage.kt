package de.dhbw.campusmarket.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.dhbw.campusmarket.R

// Zeigt das Foto einer Anzeige mit Coil. Hinter dem Foto liegt immer ein grauer Platzhalter
// mit Bild-Symbol: Gibt es kein Foto oder lässt es sich nicht laden, sieht man den Platzhalter.
@Composable
fun AdImage(imageUri: String?, contentDescription: String?, modifier: Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_image),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(32.dp)
        )
        if (imageUri != null) {
            // AsyncImage lädt das Bild im Hintergrund, die Oberfläche friert also nicht ein.
            AsyncImage(
                model = imageUri,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
