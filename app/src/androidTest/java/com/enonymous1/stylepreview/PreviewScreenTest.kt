package com.enonymous1.stylepreview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Deterministic UI checks only. State rules are covered by the JVM
 * [PreviewTest]; these tests do not depend on timing.
 */
@RunWith(AndroidJUnit4::class)
class PreviewScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun placeholderIsDisplayedAtStart() {
        composeRule.onNodeWithText(PLACEHOLDER_TEXT).assertIsDisplayed()
    }

    @Test
    fun typingUpdatesPreviewText() {
        composeRule.onNodeWithTag("sword_name_field").performTextInput("Excalibur")
        composeRule.onNodeWithTag("preview_text").assertIsDisplayed()
        composeRule.onNodeWithText("Excalibur").assertIsDisplayed()
    }

    @Test
    fun colorAndBoldChoicesKeepTypedText() {
        // v1 regression at the UI level: color and bold used to
        // replace each other's styles; the typed text must survive both.
        composeRule.onNodeWithTag("sword_name_field").performTextInput("Excalibur")
        composeRule.onNodeWithTag("text_color_BLUE").performClick()
        composeRule.onNodeWithTag("bold_switch").performClick()
        composeRule.onNodeWithText("Excalibur").assertIsDisplayed()
    }

    @Test
    fun backgroundChoiceKeepsTypedText() {
        composeRule.onNodeWithTag("sword_name_field").performTextInput("Excalibur")
        composeRule.onNodeWithTag("bg_BLUE").performClick()
        composeRule.onNodeWithText("Excalibur").assertIsDisplayed()
    }
}
