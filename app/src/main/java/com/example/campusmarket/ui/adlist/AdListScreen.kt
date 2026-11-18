package com.example.campusmarket.ui.adlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campusmarket.R
import com.example.campusmarket.data.Ad
import com.example.campusmarket.data.sampleAds
import com.example.campusmarket.ui.formatPrice
import com.example.campusmarket.ui.theme.CampusMarketTheme

// Route: holt den ViewModel und reicht State und Callbacks an den Screen weiter.
@Composable
fun AdListRoute(onAdClick: (Long) -> Unit) {
    val viewModel: AdListViewModel = viewModel(factory = AdListViewModel.Factory)
    // Sammelt nur, solange der Screen sichtbar ist.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdListScreen(
        uiState = uiState,
        onAdClick = onAdClick,
        onRetry = viewModel::loadAds
    )
}

// Stateless: bekommt nur Werte und Lambdas, kennt keinen ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(
    uiState: AdListUiState,
    onAdClick: (Long) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.hasError -> ErrorContent(onRetry, contentModifier)
            uiState.ads.isEmpty() -> EmptyContent(contentModifier)
            else -> AdListContent(uiState.ads, onAdClick, contentModifier)
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.ad_list_error))
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier) {
    Box(modifier = modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.ad_list_empty))
    }
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
            onRetry = {}
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
            onRetry = {}
        )
    }
}
