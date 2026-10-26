package com.enonymous1.stylepreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Owns the preview state: the typed name, background color, text color,
 * and bold flag. Values are mirrored into [SavedStateHandle] so a
 * recreated process restores them instead of resetting the preview.
 *
 * This fixes v1's rotation bug: v1 saved the preview text in
 * `onSaveInstanceState` but never read it back, and never saved the
 * colors or the bold flag at all.
 */
class PreviewViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PreviewState(
            text = savedStateHandle[KEY_TEXT] ?: "",
            backgroundColor = savedColor(KEY_BACKGROUND) ?: PreviewColor.RED,
            textColor = savedColor(KEY_TEXT_COLOR) ?: PreviewColor.GREEN,
            isBold = savedStateHandle[KEY_BOLD] ?: false,
        ),
    )
    val uiState: StateFlow<PreviewState> = _uiState.asStateFlow()

    fun onTextChange(text: String) {
        updateState { updateText(it, text) }
    }

    fun onBackgroundColorChange(color: PreviewColor) {
        updateState { updateBackgroundColor(it, color) }
    }

    fun onTextColorChange(color: PreviewColor) {
        updateState { updateTextColor(it, color) }
    }

    fun onBoldToggle() {
        updateState { toggleBold(it) }
    }

    private fun savedColor(key: String): PreviewColor? {
        val name: String? = savedStateHandle[key]
        return name?.let { runCatching { PreviewColor.valueOf(it) }.getOrNull() }
    }

    private fun updateState(transform: (PreviewState) -> PreviewState) {
        _uiState.update { current ->
            val next = transform(current)
            savedStateHandle[KEY_TEXT] = next.text
            savedStateHandle[KEY_BACKGROUND] = next.backgroundColor.name
            savedStateHandle[KEY_TEXT_COLOR] = next.textColor.name
            savedStateHandle[KEY_BOLD] = next.isBold
            next
        }
    }

    companion object {
        private const val KEY_TEXT = "preview_text"
        private const val KEY_BACKGROUND = "background_color"
        private const val KEY_TEXT_COLOR = "text_color"
        private const val KEY_BOLD = "is_bold"
    }
}
