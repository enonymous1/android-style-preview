package com.enonymous1.stylepreview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/** v1's color resources, kept verbatim (its "blue" is a deep indigo). */
fun PreviewColor.toColor(): Color = when (this) {
    PreviewColor.RED -> Color(0xFFB71C1C)
    PreviewColor.GREEN -> Color(0xFF1B5E20)
    PreviewColor.BLUE -> Color(0xFF311B92)
}

@Composable
fun PreviewScreen(viewModel: PreviewViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PreviewContent(
        state = state,
        onTextChange = viewModel::onTextChange,
        onBackgroundChange = viewModel::onBackgroundColorChange,
        onTextColorChange = viewModel::onTextColorChange,
        onBoldToggle = viewModel::onBoldToggle,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewContent(
    state: PreviewState,
    onTextChange: (String) -> Unit,
    onBackgroundChange: (PreviewColor) -> Unit,
    onTextColorChange: (PreviewColor) -> Unit,
    onBoldToggle: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Styles & Themes") }) },
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            val wide = maxWidth > maxHeight
            val previewSize = if (wide) 275.dp else 200.dp
            if (wide) {
                Row(modifier = Modifier.fillMaxSize()) {
                    PreviewBox(state, previewSize)
                    Spacer(modifier = Modifier.width(16.dp))
                    FormColumn(
                        state = state,
                        onTextChange = onTextChange,
                        onBackgroundChange = onBackgroundChange,
                        onTextColorChange = onTextColorChange,
                        onBoldToggle = onBoldToggle,
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PreviewBox(state, previewSize)
                    Spacer(modifier = Modifier.height(16.dp))
                    FormColumn(
                        state = state,
                        onTextChange = onTextChange,
                        onBackgroundChange = onBackgroundChange,
                        onTextColorChange = onTextColorChange,
                        onBoldToggle = onBoldToggle,
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewBox(state: PreviewState, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(state.backgroundColor.toColor()),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = displayText(state),
                color = state.textColor.toColor(),
                fontSize = 20.sp,
                fontWeight = if (state.isBold) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("preview_text"),
            )
            Spacer(modifier = Modifier.height(8.dp))
            SwordDrawing()
        }
    }
}

/**
 * An original, generic sword drawn with Canvas shapes. v1 shipped a
 * `buster_sword.png` asset; that file is not carried over, so the
 * preview keeps a sword without reusing the old image.
 */
@Composable
private fun SwordDrawing() {
    Canvas(
        modifier = Modifier
            .size(width = 72.dp, height = 110.dp)
            .semantics { contentDescription = "Generic sword illustration" },
    ) {
        val centerX = size.width / 2f
        val bladeWidth = size.width * 0.30f
        val bladeHeight = size.height * 0.58f
        val tipHeight = bladeHeight * 0.22f
        val bladeTop = tipHeight
        drawRect(
            color = Color.LightGray,
            topLeft = Offset(centerX - bladeWidth / 2f, bladeTop),
            size = Size(bladeWidth, bladeHeight - tipHeight),
        )
        val tip = Path().apply {
            moveTo(centerX, 0f)
            lineTo(centerX + bladeWidth / 2f, bladeTop)
            lineTo(centerX - bladeWidth / 2f, bladeTop)
            close()
        }
        drawPath(tip, Color.LightGray)
        val guardY = bladeHeight
        drawRect(
            color = Color(0xFF8D6E63),
            topLeft = Offset(size.width * 0.10f, guardY),
            size = Size(size.width * 0.80f, size.height * 0.05f),
        )
        val handleTop = guardY + size.height * 0.05f
        drawRect(
            color = Color.DarkGray,
            topLeft = Offset(centerX - bladeWidth * 0.30f, handleTop),
            size = Size(bladeWidth * 0.60f, size.height * 0.24f),
        )
        drawCircle(
            color = Color(0xFF8D6E63),
            radius = bladeWidth * 0.30f,
            center = Offset(centerX, handleTop + size.height * 0.24f + bladeWidth * 0.30f),
        )
    }
}

@Composable
private fun FormColumn(
    state: PreviewState,
    onTextChange: (String) -> Unit,
    onBackgroundChange: (PreviewColor) -> Unit,
    onTextColorChange: (PreviewColor) -> Unit,
    onBoldToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        ColorChoiceRow(
            title = "Background Color",
            selected = state.backgroundColor,
            onSelect = onBackgroundChange,
            tagPrefix = "bg",
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.text,
            onValueChange = onTextChange,
            label = { Text("Sword name") },
            placeholder = { Text("Insert Text Here") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("sword_name_field"),
        )
        Spacer(modifier = Modifier.height(16.dp))
        ColorChoiceRow(
            title = "Text Color",
            selected = state.textColor,
            onSelect = onTextColorChange,
            tagPrefix = "text_color",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Text Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            Switch(
                checked = state.isBold,
                onCheckedChange = { onBoldToggle() },
                modifier = Modifier.testTag("bold_switch"),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Bold")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ColorChoiceRow(
    title: String,
    selected: PreviewColor,
    onSelect: (PreviewColor) -> Unit,
    tagPrefix: String,
) {
    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Row(modifier = Modifier.fillMaxWidth()) {
        PreviewColor.entries.forEach { color ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .testTag("${tagPrefix}_${color.name}")
                    .clickable { onSelect(color) }
                    .padding(end = 8.dp),
            ) {
                RadioButton(selected = selected == color, onClick = null)
                Text(text = color.label)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreenPreview() {
    MaterialTheme {
        PreviewContent(
            state = PreviewState(text = "Excalibur", textColor = PreviewColor.BLUE, isBold = true),
            onTextChange = {},
            onBackgroundChange = {},
            onTextColorChange = {},
            onBoldToggle = {},
        )
    }
}
