package de.dhbw.campusmarket.ui.adlist

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdListUiStateTest {

    @Test
    fun addButton_hiddenWhileLoading() {
        assertFalse(AdListUiState(isLoading = true).showAddButton)
    }

    @Test
    fun addButton_hiddenWithoutInternet() {
        assertFalse(AdListUiState(isLoading = false, loadError = LoadError.NoInternet).showAddButton)
    }

    @Test
    fun addButton_hiddenOnServerError() {
        assertFalse(AdListUiState(isLoading = false, loadError = LoadError.Server(500)).showAddButton)
    }

    @Test
    fun addButton_shownWhenLoaded() {
        // Also with an empty list: the first own ad must be possible.
        assertTrue(AdListUiState(isLoading = false).showAddButton)
    }
}
