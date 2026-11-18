package com.example.campusmarket.ui.adlist

import com.example.campusmarket.data.Ad

// Alles, was der Listen-Screen anzeigen muss. Es gibt drei Zustände:
// Loading (isLoading = true), Error (hasError = true) und Ready (die Liste in ads).
data class AdListUiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val ads: List<Ad> = emptyList()
)
