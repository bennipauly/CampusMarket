package com.example.campusmarket.ui.adlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campusmarket.R
import com.example.campusmarket.ui.theme.CampusMarketTheme

// Platzhalter für die Anzeigenliste. In Schritt 4 kommt hier die echte Liste
// aus dem ViewModel hin. Der Button testet bis dahin nur die Navigation.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(onAdClick: (Long) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.ad_list_placeholder))
            Button(onClick = { onAdClick(1L) }, modifier = Modifier.padding(top = 16.dp)) {
                Text(stringResource(R.string.ad_list_open_sample))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdListScreenPreview() {
    CampusMarketTheme {
        AdListScreen(onAdClick = {})
    }
}
