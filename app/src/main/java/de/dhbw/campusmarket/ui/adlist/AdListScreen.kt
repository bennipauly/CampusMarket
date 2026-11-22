package de.dhbw.campusmarket.ui.adlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import de.dhbw.campusmarket.data.sampleAds
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.formatPrice
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: holt den ViewModel und reicht State und Callbacks an den Screen weiter.
@Composable
fun AdListRoute(onAdClick: (Long) -> Unit) {
    val viewModel: AdListViewModel = viewModel(factory = AdListViewModel.Factory)
    // Sammelt nur, solange der Screen sichtbar ist.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdListScreen(
        uiState = uiState,
        onAdClick = onAdClick,
        onRetry = viewModel::loadAds,
        onQueryChange = viewModel::onQueryChange
    )
}

// Stateless: bekommt nur Werte und Lambdas, kennt keinen ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onQueryChange: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.hasError -> ErrorContent(stringResource(R.string.ad_list_error), onRetry, contentModifier)
            uiState.ads.isEmpty() -> MessageContent(stringResource(R.string.ad_list_empty), contentModifier)
            else -> ReadyContent(uiState, onAdClick, onQueryChange, contentModifier)
        }
    }
}

// Ready: Suchfeld oben, darunter die Treffer oder ein Hinweis, dass nichts passt.
@Composable
private fun ReadyContent(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        SearchField(query = uiState.query, onQueryChange = onQueryChange)
        val visibleAds = uiState.visibleAds
        if (visibleAds.isEmpty()) {
            MessageContent(
                stringResource(R.string.ad_list_no_results, uiState.query.trim()),
                Modifier.fillMaxSize()
            )
        } else {
            AdListContent(visibleAds, onAdClick, Modifier.fillMaxSize())
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

@Composable
private fun AdListContent(ads: List<Ad>, onAdClick: (Long) -> Unit, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        // key = id: Compose erkennt dieselbe Anzeige wieder, wenn sich die Liste ändert.
        items(ads, key = { it.id }) { ad ->
            AdListItem(ad = ad, onClick = { onAdClick(ad.id) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun AdListItem(ad: Ad, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(ad.title) },
        supportingContent = { Text(ad.location) },
        trailingContent = { Text(formatPrice(ad.price)) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenReadyPreview() {
    CampusMarketTheme {
        // Die Beispieldaten bekommen hier ids, weil Room sie sonst erst beim Einfügen vergibt.
        val ads = sampleAds(now = 0L).mapIndexed { index, ad -> ad.copy(id = index + 1L) }
        AdListScreen(
            uiState = AdListUiState(isLoading = false, ads = ads),
            onAdClick = {},
            onRetry = {},
            onQueryChange = {}
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
            onRetry = {},
            onQueryChange = {}
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
            onRetry = {},
            onQueryChange = {}
        )
    }
}
