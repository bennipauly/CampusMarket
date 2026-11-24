package de.dhbw.campusmarket.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import de.dhbw.campusmarket.R

// Heart to remember an ad. Filled and colored = favorite, only outline = no favorite.
// Used in the list and in the detail screen.
@Composable
fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        if (isFavorite) {
            Icon(
                painter = painterResource(R.drawable.ic_favorite),
                contentDescription = stringResource(R.string.favorite_remove),
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_favorite_border),
                contentDescription = stringResource(R.string.favorite_add)
            )
        }
    }
}
