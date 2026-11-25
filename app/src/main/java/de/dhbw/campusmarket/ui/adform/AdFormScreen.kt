package de.dhbw.campusmarket.ui.adform

import android.Manifest
import android.content.ActivityNotFoundException
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dhbw.campusmarket.R
import de.dhbw.campusmarket.data.Category
import de.dhbw.campusmarket.data.Condition
import de.dhbw.campusmarket.logic.MAX_TITLE_LENGTH
import de.dhbw.campusmarket.ui.AdImage
import de.dhbw.campusmarket.ui.ErrorContent
import de.dhbw.campusmarket.ui.LoadingContent
import de.dhbw.campusmarket.ui.MessageContent
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "AdFormScreen"

// Route: gets the ViewModel, passes state and callbacks on and reports "saved" upwards.
// adId == null: new ad. Otherwise the ad with this id is edited.
@Composable
fun AdFormRoute(adId: Long?, onBack: () -> Unit, onSaved: () -> Unit) {
    val viewModel: AdFormViewModel = viewModel(factory = AdFormViewModel.factory(adId))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Starts the camera app. It writes the photo into the file behind the Uri.
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> viewModel.onPhotoResult(success) }

    fun launchCamera() {
        val uri = createPhotoUri(context)
        viewModel.onPhotoUriCreated(uri.toString())
        try {
            cameraLauncher.launch(uri)
        } catch (e: ActivityNotFoundException) {
            // No camera app: show a hint instead of crashing.
            Log.e(TAG, "No camera app found", e)
            viewModel.onCameraUnavailable()
        }
    }

    // Step 1: ask for the camera permission. If it is already granted,
    // "true" comes back immediately without a dialog.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            viewModel.onCameraPermissionDenied()
        }
    }

    // Fallback without camera: the system photo picker. It needs no permission, so it also
    // works when the camera permission was denied or there is no camera app.
    val scope = rememberCoroutineScope()
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { pickedUri ->
        // pickedUri == null: the picker was closed without choosing a photo, the old photo stays.
        if (pickedUri != null) {
            scope.launch {
                try {
                    val copyUri = copyPhotoToAppStorage(context, pickedUri)
                    viewModel.onGalleryPhotoCopied(copyUri.toString())
                } catch (e: IOException) {
                    Log.e(TAG, "Copying the gallery photo failed", e)
                    viewModel.onGalleryPhotoFailed()
                } catch (e: SecurityException) {
                    Log.e(TAG, "No access to the gallery photo", e)
                    viewModel.onGalleryPhotoFailed()
                }
            }
        }
    }

    // As soon as the ad is saved, go back once.
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaved()
        }
    }

    AdFormScreen(
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::loadAd,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPriceChange = viewModel::onPriceChange,
        onCategoryChange = viewModel::onCategoryChange,
        onConditionChange = viewModel::onConditionChange,
        onLocationChange = viewModel::onLocationChange,
        onTakePhotoClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
        onPickFromGalleryClick = {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onRemovePhotoClick = viewModel::onRemovePhotoClick,
        onSaveClick = viewModel::onSaveClick
    )
}

// Stateless: only receives values and lambdas and knows no ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdFormScreen(
    uiState: AdFormUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onConditionChange: (Condition) -> Unit,
    onLocationChange: (String) -> Unit,
    onTakePhotoClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    onRemovePhotoClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = if (uiState.isEditing) R.string.ad_form_title_edit else R.string.ad_form_title_create
                    Text(stringResource(title))
                },
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
        // Loading/Error/not found only exist when editing. When creating, isLoading is false.
        when {
            uiState.isLoading -> LoadingContent(contentModifier)
            uiState.hasError -> ErrorContent(stringResource(R.string.ad_detail_error), onRetry, contentModifier)
            uiState.notFound -> MessageContent(stringResource(R.string.ad_detail_not_found), contentModifier)
            uiState.isReadOnly -> MessageContent(stringResource(R.string.test_data_not_editable), contentModifier)
            else -> FormContent(
                uiState = uiState,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onPriceChange = onPriceChange,
                onCategoryChange = onCategoryChange,
                onConditionChange = onConditionChange,
                onLocationChange = onLocationChange,
                onTakePhotoClick = onTakePhotoClick,
                onPickFromGalleryClick = onPickFromGalleryClick,
                onRemovePhotoClick = onRemovePhotoClick,
                onSaveClick = onSaveClick,
                modifier = contentModifier
            )
        }
    }
}

// The actual input fields, the same for creating and editing.
@Composable
private fun FormContent(
    uiState: AdFormUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onConditionChange: (Condition) -> Unit,
    onLocationChange: (String) -> Unit,
    onTakePhotoClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    onRemovePhotoClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier
) {
    // verticalScroll + imePadding: with the keyboard open, you can still scroll down to the button.
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PhotoSection(
            uiState = uiState,
            onTakePhotoClick = onTakePhotoClick,
            onPickFromGalleryClick = onPickFromGalleryClick,
            onRemovePhotoClick = onRemovePhotoClick
        )
        FormTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            label = stringResource(R.string.label_title),
            errorText = if (uiState.titleError) {
                stringResource(R.string.error_title, MAX_TITLE_LENGTH)
            } else {
                null
            }
        )
        FormTextField(
            value = uiState.price,
            onValueChange = onPriceChange,
            label = stringResource(R.string.label_price),
            errorText = if (uiState.priceError) stringResource(R.string.error_price) else null,
            keyboardType = KeyboardType.Decimal
        )
        FormTextField(
            value = uiState.location,
            onValueChange = onLocationChange,
            label = stringResource(R.string.label_location),
            errorText = if (uiState.locationError) stringResource(R.string.error_location) else null
        )
        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.label_description)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        ChipGroup(
            title = stringResource(R.string.label_category),
            options = Category.entries,
            selected = uiState.category,
            labelRes = { it.labelRes() },
            onSelect = onCategoryChange
        )
        ChipGroup(
            title = stringResource(R.string.label_condition),
            options = Condition.entries,
            selected = uiState.condition,
            labelRes = { it.labelRes() },
            onSelect = onConditionChange
        )
        if (uiState.saveFailed) {
            Text(stringResource(R.string.error_save), color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = onSaveClick,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

// Photo preview, buttons to take, pick or remove a photo and the hints if that is not possible.
@Composable
private fun PhotoSection(
    uiState: AdFormUiState,
    onTakePhotoClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit,
    onRemovePhotoClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AdImage(
            imageUri = uiState.imageUri,
            contentDescription = stringResource(R.string.ad_photo),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        // FlowRow: if the three buttons do not fit next to each other, they wrap into a second row.
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onTakePhotoClick) {
                val text = if (uiState.imageUri == null) R.string.photo_take else R.string.photo_retake
                Text(stringResource(text))
            }
            OutlinedButton(onClick = onPickFromGalleryClick) {
                Text(stringResource(R.string.photo_pick))
            }
            if (uiState.imageUri != null) {
                OutlinedButton(onClick = onRemovePhotoClick) {
                    Text(stringResource(R.string.photo_remove))
                }
            }
        }
        if (uiState.showPermissionHint) {
            Text(stringResource(R.string.photo_permission_denied), color = MaterialTheme.colorScheme.error)
        }
        if (uiState.showNoCameraHint) {
            Text(stringResource(R.string.photo_no_camera), color = MaterialTheme.colorScheme.error)
        }
        if (uiState.showGalleryError) {
            Text(stringResource(R.string.photo_gallery_error), color = MaterialTheme.colorScheme.error)
        }
    }
}

// Single-line text field with an error text below it if errorText is not null.
@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String?,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorText != null,
        supportingText = errorText?.let { { Text(it) } },
        singleLine = true,
        // ImeAction.Next: the enter key jumps to the next field.
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth()
    )
}

// Heading and below it one chip per option. Exactly one chip is selected.
// <T> allows using the same function for Category and Condition.
@Composable
private fun <T> ChipGroup(
    title: String,
    options: List<T>,
    selected: T,
    labelRes: (T) -> Int,
    onSelect: (T) -> Unit
) {
    Column {
        Text(title, style = MaterialTheme.typography.titleSmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (option in options) {
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    label = { Text(stringResource(labelRes(option))) }
                )
            }
        }
    }
}

// Shared helper for all previews: the screen with the given state and empty callbacks.
@Composable
private fun AdFormScreenPreview(uiState: AdFormUiState) {
    CampusMarketTheme {
        AdFormScreen(
            uiState = uiState,
            onBack = {},
            onRetry = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onCategoryChange = {},
            onConditionChange = {},
            onLocationChange = {},
            onTakePhotoClick = {},
            onPickFromGalleryClick = {},
            onRemovePhotoClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdFormScreenEmptyPreview() {
    AdFormScreenPreview(AdFormUiState())
}

@Preview(showBackground = true)
@Composable
private fun AdFormScreenErrorsPreview() {
    AdFormScreenPreview(AdFormUiState(price = "12,505", showErrors = true, showPermissionHint = true))
}

@Preview(showBackground = true)
@Composable
private fun AdFormScreenEditPreview() {
    AdFormScreenPreview(
        AdFormUiState(
            isEditing = true,
            title = "Analysis 1 Skript",
            price = "8,00",
            location = "Bibliothek",
            category = Category.SCRIPTS
        )
    )
}
