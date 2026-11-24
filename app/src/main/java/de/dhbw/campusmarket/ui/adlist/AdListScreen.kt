package de.dhbw.campusmarket.ui.adlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dhbw.campusmarket.R
import de.dhbw.campusmarket.data.Ad
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.sampleAds
import de.dhbw.campusmarket.ui.AdImage
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.FavoriteButton
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.formatPrice
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: holt den ViewModel und reicht State und Callbacks an den Screen weiter.
@Composable
fun AdListRoute(onAdClick: (Long) -> Unit, onAddClick: () -> Unit) {
    val viewModel: AdListViewModel = viewModel(factory = AdListViewModel.Factory)
    // Sammelt nur, solange der Screen sichtbar ist.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdListScreen(
        uiState = uiState,
        onAdClick = onAdClick,
        onAddClick = onAddClick,
        onRetry = viewModel::loadAds,
        onQueryChange = viewModel::onQueryChange,
        onFavoriteClick = viewModel::onFavoriteClick,
        onCategoryFilterClick = viewModel::onCategoryFilterClick,
        onOnlyFavoritesClick = viewModel::onOnlyFavoritesClick,
        onOnlyAvailableClick = viewModel::onOnlyAvailableClick
    )
}

// Stateless: bekommt nur Werte und Lambdas, kennt keinen ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onRetry: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (Ad) -> Unit,
    onCategoryFilterClick: (Category) -> Unit,
    onOnlyFavoritesClick: () -> Unit,
    onOnlyAvailableClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        // FAB unten rechts: Material-Konvention für "Neues anlegen".
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(painterResource(R.drawable.ic_add), contentDescription = stringResource(R.string.add_ad))
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.hasError -> ErrorContent(stringResource(R.string.ad_list_error), onRetry, contentModifier)
            uiState.ads.isEmpty() -> MessageContent(stringResource(R.string.ad_list_empty), contentModifier)
            else -> ReadyContent(
                uiState = uiState,
                onAdClick = onAdClick,
                onQueryChange = onQueryChange,
                onFavoriteClick = onFavoriteClick,
                onCategoryFilterClick = onCategoryFilterClick,
                onOnlyFavoritesClick = onOnlyFavoritesClick,
                onOnlyAvailableClick = onOnlyAvailableClick,
                modifier = contentModifier
            )
        }
    }
}

// Ready: Suchfeld und Filter oben, darunter die Treffer oder ein Hinweis, dass nichts passt.
@Composable
private fun ReadyContent(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (Ad) -> Unit,
    onCategoryFilterClick: (Category) -> Unit,
    onOnlyFavoritesClick: () -> Unit,
    onOnlyAvailableClick: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        SearchField(query = uiState.query, onQueryChange = onQueryChange)
        FilterRow(
            uiState = uiState,
            onCategoryFilterClick = onCategoryFilterClick,
            onOnlyFavoritesClick = onOnlyFavoritesClick,
            onOnlyAvailableClick = onOnlyAvailableClick
        )
        val visibleAds = uiState.visibleAds
        if (visibleAds.isEmpty()) {
            // Mit Suchtext den Suchtext nennen, sonst liegt es nur an den Filtern.
            val message = if (uiState.query.isBlank()) {
                stringResource(R.string.ad_list_no_filter_results)
            } else {
                stringResource(R.string.ad_list_no_results, uiState.query.trim())
            }
            MessageContent(message, Modifier.fillMaxSize())
        } else {
            AdListContent(visibleAds, onAdClick, onFavoriteClick, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    // Nur zum Schließen der Tastatur, kein fachlicher State.
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text(stringResource(R.string.search_hint)) },
        leadingIcon = { Icon(painterResource(R.drawable.ic_search), contentDescription = null) },
        trailingIcon = {
            // Das Kreuz erscheint nur, wenn etwas im Feld steht.
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.search_clear)
                    )
                }
            }
        },
        singleLine = true,
        // Die Enter-Taste zeigt eine Lupe und schließt die Tastatur. Gesucht wird schon beim Tippen.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
    )
}

// Eine Zeile Filter-Chips, die man seitlich wischen kann. Mehrere Filter gelten gleichzeitig.
@Composable
private fun FilterRow(
    uiState: AdListUiState,
    onCategoryFilterClick: (Category) -> Unit,
    onOnlyFavoritesClick: () -> Unit,
    onOnlyAvailableClick: () -> Unit
) {
    // horizontalScroll: Nicht alle Chips passen nebeneinander auf den Bildschirm.
    // Die Scroll-Position ist reiner UI-State und darf darum im Composable liegen.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = uiState.onlyFavorites,
            onClick = onOnlyFavoritesClick,
            label = { Text(stringResource(R.string.filter_favorites)) }
        )
        FilterChip(
            selected = uiState.onlyAvailable,
            onClick = onOnlyAvailableClick,
            label = { Text(stringResource(R.string.filter_available)) }
        )
        for (category in Category.entries) {
            FilterChip(
                selected = uiState.categoryFilter == category,
                onClick = { onCategoryFilterClick(category) },
                label = { Text(stringResource(category.labelRes())) }
            )
        }
    }
}

@Composable
private fun AdListContent(
    ads: List<Ad>,
    onAdClick: (Long) -> Unit,
    onFavoriteClick: (Ad) -> Unit,
    modifier: Modifier
) {
    // Unten Platz lassen, damit der FAB die letzte Anzeige nicht verdeckt.
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 88.dp)) {
        // key = id: Compose erkennt dieselbe Anzeige wieder, wenn sich die Liste ändert.
        items(ads, key = { it.id }) { ad ->
            AdListItem(ad = ad, onClick = { onAdClick(ad.id) }, onFavoriteClick = { onFavoriteClick(ad) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun AdListItem(ad: Ad, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    ListItem(
        // Links ein kleines Vorschaubild oder der Platzhalter, wenn es kein Foto gibt.
        leadingContent = {
            AdImage(imageUri = ad.imageUri, contentDescription = null, modifier = Modifier.size(56.dp))
        },
        headlineContent = { Text(ad.title) },
        // Unter dem Ort steht "Verkauft", damit man verkaufte Anzeigen schon in der Liste erkennt.
        supportingContent = {
            Column {
                Text(ad.location)
                if (ad.isSold) {
                    Text(
                        stringResource(R.string.status_sold),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        // Rechts der Preis und das Herz. Das Herz hat einen eigenen Klick und öffnet nicht die Details.
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formatPrice(ad.price))
                FavoriteButton(isFavorite = ad.isFavorite, onClick = onFavoriteClick)
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenReadyPreview() {
    CampusMarketTheme {
        // Die Beispieldaten bekommen hier ids, weil Room sie sonst erst beim Einfügen vergibt.
        // Die erste Anzeige ist Favorit und die zweite verkauft, damit man beides sieht.
        val ads = sampleAds(now = 0L).mapIndexed { index, ad ->
            ad.copy(id = index + 1L, isFavorite = index == 0, isSold = index == 1)
        }
        AdListScreen(
            uiState = AdListUiState(isLoading = false, ads = ads),
            onAdClick = {},
            onAddClick = {},
            onRetry = {},
            onQueryChange = {},
            onFavoriteClick = {},
            onCategoryFilterClick = {},
            onOnlyFavoritesClick = {},
            onOnlyAvailableClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenNoResultsPreview() {
    CampusMarketTheme {
        val ads = sampleAds(now = 0L).mapIndexed { index, ad -> ad.copy(id = index + 1L) }
        AdListScreen(
            uiState = AdListUiState(isLoading = false, ads = ads, query = "Fahrrad"),
            onAdClick = {},
            onAddClick = {},
            onRetry = {},
            onQueryChange = {},
            onFavoriteClick = {},
            onCategoryFilterClick = {},
            onOnlyFavoritesClick = {},
            onOnlyAvailableClick = {}
        )
    }
}

// Filter aktiv: Kategorie "Bücher" und "Nur verfügbare". Es bleibt eine Anzeige übrig.
@Preview(showBackground = true)
@Composable
private fun AdListScreenFilterPreview() {
    CampusMarketTheme {
        val ads = sampleAds(now = 0L).mapIndexed { index, ad -> ad.copy(id = index + 1L) }
        AdListScreen(
            uiState = AdListUiState(
                isLoading = false,
                ads = ads,
                categoryFilter = Category.BOOKS,
                onlyAvailable = true
            ),
            onAdClick = {},
            onAddClick = {},
            onRetry = {},
            onQueryChange = {},
            onFavoriteClick = {},
            onCategoryFilterClick = {},
            onOnlyFavoritesClick = {},
            onOnlyAvailableClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenErrorPreview() {
    CampusMarketTheme {
        AdListScreen(
            uiState = AdListUiState(isLoading = false, hasError = true),
            onAdClick = {},
            onAddClick = {},
            onRetry = {},
            onQueryChange = {},
            onFavoriteClick = {},
            onCategoryFilterClick = {},
            onOnlyFavoritesClick = {},
            onOnlyAvailableClick = {}
        )
    }
}
