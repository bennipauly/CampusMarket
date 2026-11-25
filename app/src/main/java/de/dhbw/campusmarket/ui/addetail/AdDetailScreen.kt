package de.dhbw.campusmarket.ui.addetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import de.dhbw.campusmarket.ui.AdImage
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.FavoriteButton
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.formatPrice
import de.dhbw.campusmarket.ui.previewAds
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: gets the ViewModel for exactly this id and passes state and callbacks on.
@Composable
fun AdDetailRoute(adId: Long, onBack: () -> Unit, onPhotoClick: () -> Unit, onEditClick: () -> Unit) {
    val viewModel: AdDetailViewModel = viewModel(factory = AdDetailViewModel.factory(adId))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // As soon as the ad is deleted, go back to the list once.
    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBack()
        }
    }

    AdDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onPhotoClick = onPhotoClick,
        onEditClick = onEditClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onSoldClick = viewModel::onSoldClick,
        onRetry = viewModel::loadAd,
        onDeleteClick = viewModel::onDeleteClick,
        onDeleteConfirm = viewModel::onDeleteConfirm,
        onDeleteDismiss = viewModel::onDeleteDismiss
    )
}

// Stateless: only receives values and lambdas and knows no ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdDetailScreen(
    uiState: AdDetailUiState,
    onBack: () -> Unit,
    onPhotoClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSoldClick: () -> Unit,
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
                    // Favorite, edit and delete only exist when an ad is really shown.
                    if (uiState.ad != null) {
                        FavoriteButton(isFavorite = uiState.ad.isFavorite, onClick = onFavoriteClick)
                        // Test ads from GitHub cannot be edited, so they get no pencil.
                        if (!uiState.ad.isTestData) {
                            IconButton(onClick = onEditClick) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = stringResource(R.string.edit)
                                )
                            }
                        }
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
            else -> AdDetailContent(uiState.ad, onPhotoClick, onSoldClick, contentModifier)
        }
    }

    if (uiState.showDeleteDialog && uiState.ad != null) {
        DeleteDialog(title = uiState.ad.title, onConfirm = onDeleteConfirm, onDismiss = onDeleteDismiss)
    }
}

@Composable
private fun AdDetailContent(ad: Ad, onPhotoClick: () -> Unit, onSoldClick: () -> Unit, modifier: Modifier) {
    // verticalScroll: with a long description or in landscape the content can be scrolled.
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (ad.imageUri != null) {
            AdImage(
                imageUri = ad.imageUri,
                contentDescription = stringResource(R.string.ad_photo),
                // Tapping opens the photo in full screen. TalkBack reads out onClickLabel.
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(240.dp)
                    .clickable(onClickLabel = stringResource(R.string.photo_enlarge), onClick = onPhotoClick)
            )
        }
        Text(ad.title, style = MaterialTheme.typography.headlineSmall)
        Text(
            formatPrice(ad.price),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (ad.isTestData) {
            Text(
                stringResource(R.string.test_data_hint),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        DetailRow(stringResource(R.string.label_category), stringResource(ad.category.labelRes()))
        DetailRow(stringResource(R.string.label_condition), stringResource(ad.condition.labelRes()))
        DetailRow(stringResource(R.string.label_location), ad.location)
        val status = if (ad.isSold) R.string.status_sold else R.string.status_available
        DetailRow(stringResource(R.string.label_status), stringResource(status))
        // The button describes what happens on tap, not the current state.
        OutlinedButton(
            onClick = onSoldClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            val text = if (ad.isSold) R.string.mark_available else R.string.mark_sold
            Text(stringResource(text))
        }
        // The description is optional in the form. Without text, do not show an empty heading.
        if (ad.description.isNotBlank()) {
            Text(
                stringResource(R.string.label_description),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            Text(ad.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// One row "label: value", the label on the left, the value next to it on the right.
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

// Confirmation before deleting, so nobody loses an ad by accident.
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
            uiState = AdDetailUiState(isLoading = false, ad = previewAds().first()),
            onBack = {},
            onPhotoClick = {},
            onEditClick = {},
            onFavoriteClick = {},
            onSoldClick = {},
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
                ad = previewAds().first(),
                showDeleteDialog = true
            ),
            onBack = {},
            onPhotoClick = {},
            onEditClick = {},
            onFavoriteClick = {},
            onSoldClick = {},
            onRetry = {},
            onDeleteClick = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}
