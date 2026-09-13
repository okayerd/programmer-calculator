package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CalculatorEngine
import com.example.model.Radix
import com.example.model.SignMode
import com.example.model.WordSize

@Composable
fun RadixRowSection(
    displayValue: ULong,
    activeRadix: Radix,
    wordSize: WordSize,
    signMode: SignMode,
    onRadixSelect: (Radix) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val hexStr = CalculatorEngine.formatHex(displayValue, wordSize)
    val decStr = CalculatorEngine.formatDec(displayValue, wordSize, signMode)
    val octStr = CalculatorEngine.formatOct(displayValue, wordSize)
    val binStr = CalculatorEngine.formatBin(displayValue, wordSize, padded = true)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        RadixRowItem(
            radix = Radix.HEX,
            valueString = hexStr,
            isActive = activeRadix == Radix.HEX,
            onClick = { onRadixSelect(Radix.HEX) },
            onLongClick = {
                clipboardManager.setText(AnnotatedString(hexStr))
                Toast.makeText(context, "HEX copied: $hexStr", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("radix_row_hex")
        )

        RadixRowItem(
            radix = Radix.DEC,
            valueString = decStr,
            isActive = activeRadix == Radix.DEC,
            onClick = { onRadixSelect(Radix.DEC) },
            onLongClick = {
                clipboardManager.setText(AnnotatedString(decStr))
                Toast.makeText(context, "DEC copied: $decStr", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("radix_row_dec")
        )

        RadixRowItem(
            radix = Radix.OCT,
            valueString = octStr,
            isActive = activeRadix == Radix.OCT,
            onClick = { onRadixSelect(Radix.OCT) },
            onLongClick = {
                clipboardManager.setText(AnnotatedString(octStr))
                Toast.makeText(context, "OCT copied: $octStr", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("radix_row_oct")
        )

        RadixRowItem(
            radix = Radix.BIN,
            valueString = binStr,
            isActive = activeRadix == Radix.BIN,
            onClick = { onRadixSelect(Radix.BIN) },
            onLongClick = {
                val rawBin = CalculatorEngine.formatBin(displayValue, wordSize, padded = false)
                clipboardManager.setText(AnnotatedString(rawBin))
                Toast.makeText(context, "BIN copied: $rawBin", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("radix_row_bin")
        )
    }
}

@Composable
private fun RadixRowItem(
    radix: Radix,
    valueString: String,
    isActive: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val shape = RoundedCornerShape(8.dp)
    val backgroundColor = if (isActive) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    }

    val borderColor = if (isActive) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label
        Text(
            text = radix.label,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(36.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Value
        Box(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState, reverseScrolling = true),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = valueString,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}
