package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CalculatorEngine
import com.example.model.WordSize

@Composable
fun BitBoardSection(
    displayValue: ULong,
    wordSize: WordSize,
    onBitToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Row 1: Bits 63 .. 48
        BitRow(
            startBit = 63,
            endBit = 48,
            displayValue = displayValue,
            wordSize = wordSize,
            onBitClick = { bitIndex ->
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onBitToggle(bitIndex)
            }
        )

        // Row 2: Bits 47 .. 32
        BitRow(
            startBit = 47,
            endBit = 32,
            displayValue = displayValue,
            wordSize = wordSize,
            onBitClick = { bitIndex ->
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onBitToggle(bitIndex)
            }
        )

        // Row 3: Bits 31 .. 16
        BitRow(
            startBit = 31,
            endBit = 16,
            displayValue = displayValue,
            wordSize = wordSize,
            onBitClick = { bitIndex ->
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onBitToggle(bitIndex)
            }
        )

        // Row 4: Bits 15 .. 0
        BitRow(
            startBit = 15,
            endBit = 0,
            displayValue = displayValue,
            wordSize = wordSize,
            onBitClick = { bitIndex ->
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onBitToggle(bitIndex)
            }
        )
    }
}

@Composable
private fun BitRow(
    startBit: Int,
    endBit: Int,
    displayValue: ULong,
    wordSize: WordSize,
    onBitClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left index label
        Text(
            text = startBit.toString(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.width(18.dp),
            textAlign = TextAlign.Start
        )

        // 16 bits split into 4 nibbles of 4 bits
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nibble 1 (e.g. 63..60)
            NibbleGroup(
                start = startBit,
                displayValue = displayValue,
                wordSize = wordSize,
                onBitClick = onBitClick
            )

            // Nibble 2 (e.g. 59..56)
            NibbleGroup(
                start = startBit - 4,
                displayValue = displayValue,
                wordSize = wordSize,
                onBitClick = onBitClick
            )

            Spacer(modifier = Modifier.width(4.dp)) // byte separation

            // Nibble 3 (e.g. 55..52)
            NibbleGroup(
                start = startBit - 8,
                displayValue = displayValue,
                wordSize = wordSize,
                onBitClick = onBitClick
            )

            // Nibble 4 (e.g. 51..48)
            NibbleGroup(
                start = startBit - 12,
                displayValue = displayValue,
                wordSize = wordSize,
                onBitClick = onBitClick
            )
        }

        // Right index label
        Text(
            text = endBit.toString(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.width(18.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun NibbleGroup(
    start: Int,
    displayValue: ULong,
    wordSize: WordSize,
    onBitClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..3) {
            val bitIndex = start - i
            BitCell(
                bitIndex = bitIndex,
                displayValue = displayValue,
                wordSize = wordSize,
                onClick = { onBitClick(bitIndex) }
            )
        }
    }
}

@Composable
private fun BitCell(
    bitIndex: Int,
    displayValue: ULong,
    wordSize: WordSize,
    onClick: () -> Unit
) {
    val isEnabled = bitIndex < wordSize.bits
    val isSet = isEnabled && CalculatorEngine.isBitSet(displayValue, bitIndex)

    val shape = RoundedCornerShape(3.dp)
    val bgColor = when {
        !isEnabled -> Color.Transparent
        isSet -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

    val borderColor = when {
        !isEnabled -> MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
        isSet -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    val textColor = when {
        !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        isSet -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    }

    Box(
        modifier = Modifier
            .size(width = 15.dp, height = 20.dp)
            .clip(shape)
            .background(bgColor)
            .border(0.75.dp, borderColor, shape)
            .clickable(enabled = isEnabled, onClick = onClick)
            .testTag("bit_$bitIndex"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (!isEnabled) "·" else if (isSet) "1" else "0",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isSet) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
