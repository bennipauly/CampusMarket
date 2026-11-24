package de.dhbw.campusmarket.ui.photoviewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import de.dhbw.campusmarket.R
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: gets the ViewModel for exactly this id and passes state and callbacks on.
@Composable
fun PhotoViewerRoute(adId: Long, onBack: () -> Unit) {
    val viewModel: PhotoViewerViewModel = viewModel(factory = PhotoViewerViewModel.factory(adId))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PhotoViewerScreen(uiState = uiState, onBack = onBack, onRetry = viewModel::loadPhoto)
}

// Stateless: shows the photo as large as possible and complete (nothing is cut off).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoViewerScreen(uiState: PhotoViewerUiState, onBack: () -> Unit, onRetry: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.photo_viewer_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
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
            uiState.hasError -> ErrorContent(stringResource(R.string.photo_viewer_error), onRetry, contentModifier)
            uiState.imageUri == null -> MessageContent(stringResource(R.string.photo_viewer_no_photo), contentModifier)
            // ContentScale.Fit: the whole photo fits on the screen, in portrait and landscape.
            else -> AsyncImage(
                model = uiState.imageUri,
                contentDescription = stringResource(R.string.ad_photo),
                contentScale = ContentScale.Fit,
                modifier = contentModifier
            )
        }
    }
}

// The preview has no real photo file, so it shows the "no photo" case.
@Preview(showBackground = true)
@Composable
private fun PhotoViewerScreenNoPhotoPreview() {
    CampusMarketTheme {
        PhotoViewerScreen(uiState = PhotoViewerUiState(isLoading = false), onBack = {}, onRetry = {})
    }
}
