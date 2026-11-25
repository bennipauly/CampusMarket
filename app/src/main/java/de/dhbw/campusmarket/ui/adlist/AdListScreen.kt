package de.dhbw.campusmarket.ui.adlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import de.dhbw.campusmarket.logic.SortOrder
import de.dhbw.campusmarket.ui.AdImage
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.FavoriteButton
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.formatPrice
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.previewAds
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: gets the ViewModel and passes state and callbacks on to the screen.
@Composable
fun AdListRoute(onAdClick: (Long) -> Unit, onAddClick: () -> Unit) {
    val viewModel: AdListViewModel = viewModel(factory = AdListViewModel.Factory)
    // Collects only while the screen is visible.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdListScreen(
        uiState = uiState,
        onAdClick = onAdClick,
        onAddClick = onAddClick,
        onRefreshClick = viewModel::refreshAds,
        onQueryChange = viewModel::onQueryChange,
        onFavoriteClick = viewModel::onFavoriteClick,
        onCategoryFilterClick = viewModel::onCategoryFilterClick,
        onOnlyFavoritesClick = viewModel::onOnlyFavoritesClick,
        onOnlyAvailableClick = viewModel::onOnlyAvailableClick,
        onSortOrderChange = viewModel::onSortOrderChange
    )
}

// Stateless: only receives values and lambdas and knows no ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (Ad) -> Unit,
    onCategoryFilterClick: (Category) -> Unit,
    onOnlyFavoritesClick: () -> Unit,
    onOnlyAvailableClick: () -> Unit,
    onSortOrderChange: (SortOrder) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    // Loads the test data again. Disabled while loading, so it cannot start twice.
                    IconButton(onClick = onRefreshClick, enabled = !uiState.isLoading) {
                        Icon(painterResource(R.drawable.ic_refresh), contentDescription = stringResource(R.string.refresh))
                    }
                    // Sorting only makes sense when ads are shown.
                    if (uiState.loadError == null && uiState.ads.isNotEmpty()) {
                        SortMenu(sortOrder = uiState.sortOrder, onSortOrderChange = onSortOrderChange)
                    }
                }
            )
        },
        // FAB at the bottom right: Material convention for "create something new".
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(painterResource(R.drawable.ic_add), contentDescription = stringResource(R.string.add_ad))
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        // On an error only the message and "try again" are shown, no ads (even if Room has some).
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.loadError != null -> ErrorContent(loadErrorText(uiState.loadError), onRefreshClick, contentModifier)
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

// The message for each kind of error. "when" without else: a new LoadError case would not compile
// until it has a text here.
@Composable
private fun loadErrorText(error: LoadError): String = when (error) {
    LoadError.NoInternet -> stringResource(R.string.error_no_internet)
    is LoadError.Server -> stringResource(R.string.error_server, error.code)
    LoadError.InvalidData -> stringResource(R.string.error_invalid_data)
    LoadError.Database -> stringResource(R.string.ad_list_error)
}

// Sort button in the top app bar. Opens a menu with one entry per sort order;
// the active one has a check mark.
@Composable
private fun SortMenu(sortOrder: SortOrder, onSortOrderChange: (SortOrder) -> Unit) {
    // Whether the menu is open is pure UI state, so it may live in the composable.
    // rememberSaveable keeps it open across a rotation.
    var isMenuOpen by rememberSaveable { mutableStateOf(false) }
    // The Box anchors the menu below the button.
    Box {
        IconButton(onClick = { isMenuOpen = true }) {
            Icon(painterResource(R.drawable.ic_sort), contentDescription = stringResource(R.string.sort))
        }
        DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
            for (order in SortOrder.entries) {
                DropdownMenuItem(
                    text = { Text(stringResource(order.labelRes())) },
                    onClick = {
                        isMenuOpen = false
                        onSortOrderChange(order)
                    },
                    trailingIcon = {
                        if (order == sortOrder) {
                            Icon(
                                painterResource(R.drawable.ic_check),
                                contentDescription = stringResource(R.string.sort_selected)
                            )
                        }
                    }
                )
            }
        }
    }
}

// Ready: search field and filters at the top, below them the matching ads or a hint that nothing matches.
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
            // With a search text, name it; otherwise only the filters are the reason.
            val message = if (uiState.query.isBlank()) {
                stringResource(R.string.ad_list_no_filter_results)
            } else {
                stringResource(R.string.ad_list_no_results, uiState.query.trim())
            }
            MessageContent(message, Modifier.fillMaxSize())
        } else {
            // key(sortOrder): a new sort order creates a new scroll state, so the list starts at
            // the top again. Without it, Compose would keep the previously first ad in view.
            // A rotation keeps the sort order and therefore also the scroll position.
            key(uiState.sortOrder) {
                AdListContent(visibleAds, onAdClick, onFavoriteClick, Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    // Only used to close the keyboard, not business state.
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
            // The X only appears when there is text in the field.
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
        // The enter key shows a magnifier and closes the keyboard. Searching already happens while typing.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
    )
}

// A row of filter chips that can be swiped sideways. Several filters apply at the same time.
@Composable
private fun FilterRow(
    uiState: AdListUiState,
    onCategoryFilterClick: (Category) -> Unit,
    onOnlyFavoritesClick: () -> Unit,
    onOnlyAvailableClick: () -> Unit
) {
    // horizontalScroll: not all chips fit next to each other on the screen.
    // The scroll position is pure UI state and may therefore live in the composable.
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
    // Leave space at the bottom so the FAB does not cover the last ad.
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = 88.dp)) {
        // key = id: Compose recognizes the same ad again when the list changes.
        items(ads, key = { it.id }) { ad ->
            AdListItem(ad = ad, onClick = { onAdClick(ad.id) }, onFavoriteClick = { onFavoriteClick(ad) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun AdListItem(ad: Ad, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    ListItem(
        // On the left a small thumbnail, or the placeholder if there is no photo.
        leadingContent = {
            AdImage(imageUri = ad.imageUri, contentDescription = null, modifier = Modifier.size(56.dp))
        },
        headlineContent = { Text(ad.title) },
        // "Sold" below the location, so sold ads can already be recognized in the list.
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
        // Price and heart on the right. The heart has its own click and does not open the details.
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formatPrice(ad.price))
                FavoriteButton(isFavorite = ad.isFavorite, onClick = onFavoriteClick)
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

// Shared helper for all previews: the screen with the given state and empty callbacks.
@Composable
private fun AdListScreenPreview(uiState: AdListUiState) {
    CampusMarketTheme {
        AdListScreen(
            uiState = uiState,
            onAdClick = {},
            onAddClick = {},
            onRefreshClick = {},
            onQueryChange = {},
            onFavoriteClick = {},
            onCategoryFilterClick = {},
            onOnlyFavoritesClick = {},
            onOnlyAvailableClick = {},
            onSortOrderChange = {}
        )
    }
}

// The first ad is a favorite and the second one is sold, so both can be seen.
@Preview(showBackground = true)
@Composable
private fun AdListScreenReadyPreview() {
    val ads = previewAds().mapIndexed { index, ad -> ad.copy(isFavorite = index == 0, isSold = index == 1) }
    AdListScreenPreview(AdListUiState(isLoading = false, ads = ads))
}

// Sorted by price, cheapest first.
@Preview(showBackground = true)
@Composable
private fun AdListScreenSortedPreview() {
    AdListScreenPreview(AdListUiState(isLoading = false, ads = previewAds(), sortOrder = SortOrder.PRICE_ASCENDING))
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenNoResultsPreview() {
    AdListScreenPreview(AdListUiState(isLoading = false, ads = previewAds(), query = "Fahrrad"))
}

// Active filters: category "books" and "only available". One ad is left.
@Preview(showBackground = true)
@Composable
private fun AdListScreenFilterPreview() {
    AdListScreenPreview(
        AdListUiState(
            isLoading = false,
            ads = previewAds(),
            categoryFilter = Category.BOOKS,
            onlyAvailable = true
        )
    )
}

// Airplane mode: only the message and "try again", even though Room has ads.
@Preview(showBackground = true)
@Composable
private fun AdListScreenNoInternetPreview() {
    AdListScreenPreview(AdListUiState(isLoading = false, loadError = LoadError.NoInternet, ads = previewAds()))
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenServerErrorPreview() {
    AdListScreenPreview(AdListUiState(isLoading = false, loadError = LoadError.Server(404)))
}
