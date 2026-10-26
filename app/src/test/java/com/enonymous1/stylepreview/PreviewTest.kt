package com.enonymous1.stylepreview

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PreviewTest {

    @Test
    fun defaults_matchV1InitialScreen() {
        val state = PreviewState()
        assertEquals("", state.text)
        assertEquals(PreviewColor.RED, state.backgroundColor)
        assertEquals(PreviewColor.GREEN, state.textColor)
        assertFalse(state.isBold)
    }

    @Test
    fun displayText_showsPlaceholderWhenBlank() {
        assertEquals(PLACEHOLDER_TEXT, displayText(PreviewState()))
        assertEquals(PLACEHOLDER_TEXT, displayText(PreviewState(text = "   ")))
        assertEquals("Excalibur", displayText(PreviewState(text = "Excalibur")))
    }

    @Test
    fun updateText_replacesOnlyText() {
        val next = updateText(PreviewState(), "Excalibur")
        assertEquals("Excalibur", next.text)
        assertEquals(PreviewColor.RED, next.backgroundColor)
        assertEquals(PreviewColor.GREEN, next.textColor)
        assertFalse(next.isBold)
    }

    @Test
    fun textColorChange_preservesBold() {
        // v1 regression: picking a text color used to wipe out bold.
        val bold = PreviewState(isBold = true)
        val next = updateTextColor(bold, PreviewColor.BLUE)
        assertTrue(next.isBold)
        assertEquals(PreviewColor.BLUE, next.textColor)
    }

    @Test
    fun boldToggle_preservesTextColor() {
        // v1 regression, the other direction: toggling bold wiped the color.
        val colored = PreviewState(textColor = PreviewColor.BLUE)
        val next = toggleBold(colored)
        assertTrue(next.isBold)
        assertEquals(PreviewColor.BLUE, next.textColor)
        assertFalse(toggleBold(next).isBold)
    }

    @Test
    fun backgroundChange_preservesTextAndColor() {
        val state = PreviewState(text = "Excalibur", textColor = PreviewColor.BLUE, isBold = true)
        val next = updateBackgroundColor(state, PreviewColor.GREEN)
        assertEquals(PreviewColor.GREEN, next.backgroundColor)
        assertEquals("Excalibur", next.text)
        assertEquals(PreviewColor.BLUE, next.textColor)
        assertTrue(next.isBold)
    }

    @Test
    fun colorLabels_matchV1Choices() {
        assertEquals(listOf("Red", "Green", "Blue"), PreviewColor.entries.map { it.label })
    }

    @Test
    fun viewModel_defaultsAndUpdates() {
        val viewModel = PreviewViewModel(SavedStateHandle())
        assertEquals(PreviewState(), viewModel.uiState.value)
        viewModel.onTextChange("Excalibur")
        viewModel.onBackgroundColorChange(PreviewColor.BLUE)
        viewModel.onTextColorChange(PreviewColor.RED)
        viewModel.onBoldToggle()
        val state = viewModel.uiState.value
        assertEquals("Excalibur", state.text)
        assertEquals(PreviewColor.BLUE, state.backgroundColor)
        assertEquals(PreviewColor.RED, state.textColor)
        assertTrue(state.isBold)
    }

    @Test
    fun viewModel_restoresFromSavedStateHandle() {
        val handle = SavedStateHandle(
            initialState = mapOf(
                "preview_text" to "Excalibur",
                "background_color" to "BLUE",
                "text_color" to "RED",
                "is_bold" to true,
            ),
        )
        val viewModel = PreviewViewModel(handle)
        val state = viewModel.uiState.value
        assertEquals("Excalibur", state.text)
        assertEquals(PreviewColor.BLUE, state.backgroundColor)
        assertEquals(PreviewColor.RED, state.textColor)
        assertTrue(state.isBold)
    }

    @Test
    fun viewModel_ignoresUnknownSavedColor() {
        val handle = SavedStateHandle(initialState = mapOf("background_color" to "PURPLE"))
        val viewModel = PreviewViewModel(handle)
        assertEquals(PreviewColor.RED, viewModel.uiState.value.backgroundColor)
        assertEquals(PreviewColor.GREEN, viewModel.uiState.value.textColor)
    }
}
