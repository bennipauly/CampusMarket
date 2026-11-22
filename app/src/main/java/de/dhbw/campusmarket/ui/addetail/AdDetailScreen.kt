package de.dhbw.campusmarket.ui.addetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: holt den ViewModel für genau diese id und reicht State und Callbacks weiter.
@Composable
fun AdDetailRoute(adId: Long, onBack: () -> Unit) {
    val viewModel: AdDetailViewModel = viewModel(factory = AdDetailViewModel.factory(adId))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Sobald die Anzeige gelöscht ist, einmal zurück zur Liste.
    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBack()
        }
    }

    AdDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::loadAd,
        onDeleteClick = viewModel::onDeleteClick,
        onDeleteConfirm = viewModel::onDeleteConfirm,
        onDeleteDismiss = viewModel::onDeleteDismiss
    )
}

// Stateless: bekommt nur Werte und Lambdas, kennt keinen ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdDetailScreen(
    uiState: AdDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteDismiss: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ad_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    // Löschen gibt es nur, wenn wirklich eine Anzeige angezeigt wird.
                    if (uiState.ad != null) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.hasError -> ErrorContent(stringResource(R.string.ad_detail_error), onRetry, contentModifier)
            uiState.ad == null -> MessageContent(stringResource(R.string.ad_detail_not_found), contentModifier)
            else -> AdDetailContent(uiState.ad, contentModifier)
        }
    }

    if (uiState.showDeleteDialog && uiState.ad != null) {
        DeleteDialog(title = uiState.ad.title, onConfirm = onDeleteConfirm, onDismiss = onDeleteDismiss)
    }
}

@Composable
private fun AdDetailContent(ad: Ad, modifier: Modifier) {
    // verticalScroll: Bei langer Beschreibung oder im Querformat kann man scrollen.
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(ad.title, style = MaterialTheme.typography.headlineSmall)
        Text(
            formatPrice(ad.price),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        DetailRow(stringResource(R.string.label_category), stringResource(ad.category.labelRes()))
        DetailRow(stringResource(R.string.label_condition), stringResource(ad.condition.labelRes()))
        DetailRow(stringResource(R.string.label_location), ad.location)
        val status = if (ad.isSold) R.string.status_sold else R.string.status_available
        DetailRow(stringResource(R.string.label_status), stringResource(status))
        Text(
            stringResource(R.string.label_description),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )
        Text(ad.description, style = MaterialTheme.typography.bodyLarge)
    }
}

// Eine Zeile "Bezeichnung: Wert", die Bezeichnung links, der Wert rechts daneben.
@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(0.6f))
    }
}

// Bestätigung vor dem Löschen, damit niemand aus Versehen eine Anzeige verliert.
@Composable
private fun DeleteDialog(title: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_dialog_title)) },
        text = { Text(stringResource(R.string.delete_dialog_text, title)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.delete)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AdDetailScreenReadyPreview() {
    CampusMarketTheme {
        AdDetailScreen(
            uiState = AdDetailUiState(isLoading = false, ad = sampleAds(now = 0L).first().copy(id = 1L)),
            onBack = {},
            onRetry = {},
            onDeleteClick = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdDetailScreenDeleteDialogPreview() {
    CampusMarketTheme {
        AdDetailScreen(
            uiState = AdDetailUiState(
                isLoading = false,
                ad = sampleAds(now = 0L).first().copy(id = 1L),
                showDeleteDialog = true
            ),
            onBack = {},
            onRetry = {},
            onDeleteClick = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}
