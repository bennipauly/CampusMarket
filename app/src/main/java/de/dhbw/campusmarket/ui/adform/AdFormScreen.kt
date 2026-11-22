package de.dhbw.campusmarket.ui.adform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import de.dhbw.campusmarket.ui.labelRes
import de.dhbw.campusmarket.ui.theme.CampusMarketTheme

// Route: holt den ViewModel, reicht State und Callbacks weiter und meldet "gespeichert" nach oben.
@Composable
fun AdFormRoute(onBack: () -> Unit, onSaved: () -> Unit) {
    val viewModel: AdFormViewModel = viewModel(factory = AdFormViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Sobald die Anzeige gespeichert ist, einmal zurück.
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaved()
        }
    }

    AdFormScreen(
        uiState = uiState,
        onBack = onBack,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onPriceChange = viewModel::onPriceChange,
        onCategoryChange = viewModel::onCategoryChange,
        onConditionChange = viewModel::onConditionChange,
        onLocationChange = viewModel::onLocationChange,
        onSaveClick = viewModel::onSaveClick
    )
}

// Stateless: bekommt nur Werte und Lambdas, kennt keinen ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdFormScreen(
    uiState: AdFormUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onConditionChange: (Condition) -> Unit,
    onLocationChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ad_form_title_create)) },
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
        // verticalScroll + imePadding: Bei offener Tastatur kann man bis zum Button scrollen.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
}

// Einzeiliges Textfeld mit Fehlertext darunter, wenn errorText nicht null ist.
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
        // ImeAction.Next: Die Enter-Taste springt ins nächste Feld.
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth()
    )
}

// Überschrift und darunter ein Chip pro Möglichkeit. Genau ein Chip ist ausgewählt.
// <T> erlaubt, dieselbe Funktion für Category und Condition zu benutzen.
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

@Preview(showBackground = true)
@Composable
private fun AdFormScreenEmptyPreview() {
    CampusMarketTheme {
        AdFormScreen(
            uiState = AdFormUiState(),
            onBack = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onCategoryChange = {},
            onConditionChange = {},
            onLocationChange = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdFormScreenErrorsPreview() {
    CampusMarketTheme {
        AdFormScreen(
            uiState = AdFormUiState(price = "12,505", showErrors = true),
            onBack = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPriceChange = {},
            onCategoryChange = {},
            onConditionChange = {},
            onLocationChange = {},
            onSaveClick = {}
        )
    }
}
