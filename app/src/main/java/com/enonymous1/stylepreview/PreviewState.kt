package com.enonymous1.stylepreview

/**
 * Pure preview state for the styles-and-themes demo. Nothing here touches
 * Android APIs, so every rule below is covered by a plain JVM unit test.
 *
 * v1 stored the preview in Views and applied each choice with a separate
 * `setTextAppearance()` call, which replaced the whole style: picking a
 * text color wiped out bold, and toggling bold wiped out the color.
 * Here background color, text color, and bold are independent fields of
 * one state object, so changing one can never clobber another.
 *
 * v1 also switched on the radio buttons' displayed text ("Red", "Green",
 * "Blue"). These are enums now, so a label change or a translation
 * cannot break the logic.
 */

/** The three choices offered for both background and text color. */
enum class PreviewColor(val label: String) {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
}

data class PreviewState(
    val text: String = "",
    val backgroundColor: PreviewColor = PreviewColor.RED,
    val textColor: PreviewColor = PreviewColor.GREEN,
    val isBold: Boolean = false,
)

/** Placeholder shown while the name field is empty, matching v1. */
const val PLACEHOLDER_TEXT = "Insert Sword Name"

/** The text shown in the preview: the typed name, or the placeholder. */
fun displayText(state: PreviewState): String =
    state.text.ifBlank { PLACEHOLDER_TEXT }

fun updateText(state: PreviewState, text: String): PreviewState =
    state.copy(text = text)

fun updateBackgroundColor(state: PreviewState, color: PreviewColor): PreviewState =
    state.copy(backgroundColor = color)

fun updateTextColor(state: PreviewState, color: PreviewColor): PreviewState =
    state.copy(textColor = color)

fun toggleBold(state: PreviewState): PreviewState =
    state.copy(isBold = !state.isBold)
