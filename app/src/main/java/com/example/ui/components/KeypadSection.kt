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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Radix

@Composable
fun KeypadSection(
    activeRadix: Radix,
    canUndo: Boolean,
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onParenthesisClick: (String) -> Unit,
    onBitwiseNotClick: () -> Unit,
    onNegateClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onEvaluateClick: () -> Unit,
    onUndoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val hapticTap = {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // --- Bitwise Logic Operators Bar (2 rows of 4 compact buttons) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LogicKey(label = "AND", onClick = { hapticTap(); onOperatorClick("AND") }, modifier = Modifier.weight(1f))
            LogicKey(label = "OR", onClick = { hapticTap(); onOperatorClick("OR") }, modifier = Modifier.weight(1f))
            LogicKey(label = "XOR", onClick = { hapticTap(); onOperatorClick("XOR") }, modifier = Modifier.weight(1f))
            LogicKey(label = "NOT", onClick = { hapticTap(); onBitwiseNotClick() }, modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LogicKey(label = "NAND", onClick = { hapticTap(); onOperatorClick("NAND") }, modifier = Modifier.weight(1f))
            LogicKey(label = "NOR", onClick = { hapticTap(); onOperatorClick("NOR") }, modifier = Modifier.weight(1f))
            LogicKey(label = "<<", onClick = { hapticTap(); onOperatorClick("<<") }, modifier = Modifier.weight(1f))
            LogicKey(label = ">>", onClick = { hapticTap(); onOperatorClick(">>") }, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(2.dp))

        // --- Main Keypad: 5 rows x 6 columns ---
        // Row 1: A | B | ( | ) | ⌫ | AC
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HexKey(label = "A", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("A") }, modifier = Modifier.weight(1f))
            HexKey(label = "B", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("B") }, modifier = Modifier.weight(1f))
            FnKey(label = "(", onClick = { hapticTap(); onParenthesisClick("(") }, modifier = Modifier.weight(1f))
            FnKey(label = ")", onClick = { hapticTap(); onParenthesisClick(")") }, modifier = Modifier.weight(1f))
            ActionKey(label = "⌫", onClick = { hapticTap(); onBackspaceClick() }, modifier = Modifier.weight(1f))
            ActionKey(label = "AC", isHighlighted = true, onClick = { hapticTap(); onClearClick() }, modifier = Modifier.weight(1f))
        }

        // Row 2: C | D | 7 | 8 | 9 | ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HexKey(label = "C", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("C") }, modifier = Modifier.weight(1f))
            HexKey(label = "D", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("D") }, modifier = Modifier.weight(1f))
            NumKey(label = "7", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("7") }, modifier = Modifier.weight(1f))
            NumKey(label = "8", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("8") }, modifier = Modifier.weight(1f))
            NumKey(label = "9", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("9") }, modifier = Modifier.weight(1f))
            OpKey(label = "÷", onClick = { hapticTap(); onOperatorClick("÷") }, modifier = Modifier.weight(1f))
        }

        // Row 3: E | F | 4 | 5 | 6 | ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HexKey(label = "E", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("E") }, modifier = Modifier.weight(1f))
            HexKey(label = "F", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("F") }, modifier = Modifier.weight(1f))
            NumKey(label = "4", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("4") }, modifier = Modifier.weight(1f))
            NumKey(label = "5", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("5") }, modifier = Modifier.weight(1f))
            NumKey(label = "6", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("6") }, modifier = Modifier.weight(1f))
            OpKey(label = "×", onClick = { hapticTap(); onOperatorClick("×") }, modifier = Modifier.weight(1f))
        }

        // Row 4: Undo | ± | 1 | 2 | 3 | -
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FnKey(
                label = "↩",
                enabled = canUndo,
                onClick = { hapticTap(); onUndoClick() },
                modifier = Modifier.weight(1f)
            )
            FnKey(
                label = "±",
                onClick = { hapticTap(); onNegateClick() },
                modifier = Modifier.weight(1f)
            )
            NumKey(label = "1", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("1") }, modifier = Modifier.weight(1f))
            NumKey(label = "2", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("2") }, modifier = Modifier.weight(1f))
            NumKey(label = "3", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("3") }, modifier = Modifier.weight(1f))
            OpKey(label = "-", onClick = { hapticTap(); onOperatorClick("-") }, modifier = Modifier.weight(1f))
        }

        // Row 5: 00 | 0 | % | = | +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HexKey(
                label = "00",
                activeRadix = activeRadix,
                onClick = { hapticTap(); onDigitClick("0"); onDigitClick("0") },
                modifier = Modifier.weight(1f)
            )
            NumKey(label = "0", activeRadix = activeRadix, onClick = { hapticTap(); onDigitClick("0") }, modifier = Modifier.weight(1f))
            OpKey(label = "%", onClick = { hapticTap(); onOperatorClick("%") }, modifier = Modifier.weight(1f))
            OpKey(label = "+", onClick = { hapticTap(); onOperatorClick("+") }, modifier = Modifier.weight(1f))
            EvalKey(label = "=", onClick = { hapticTap(); onEvaluateClick() }, modifier = Modifier.weight(2f))
        }
    }
}

@Composable
private fun LogicKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), shape)
            .clickable(onClick = onClick)
            .testTag("key_logic_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun HexKey(
    label: String,
    activeRadix: Radix,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = if (label == "00") activeRadix != Radix.BIN else activeRadix.isKeyEnabled(label)
    val shape = RoundedCornerShape(10.dp)

    val bgColor = if (isEnabled) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
    }

    val textColor = if (isEnabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(bgColor)
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = if (isEnabled) 0.3f else 0.08f), shape)
            .clickable(enabled = isEnabled, onClick = onClick)
            .testTag("key_hex_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun NumKey(
    label: String,
    activeRadix: Radix,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = activeRadix.isKeyEnabled(label)
    val shape = RoundedCornerShape(10.dp)

    val bgColor = if (isEnabled) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
    }

    val textColor = if (isEnabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(bgColor)
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = if (isEnabled) 0.35f else 0.08f), shape)
            .clickable(enabled = isEnabled, onClick = onClick)
            .testTag("key_num_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
private fun OpKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f))
            .border(0.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f), shape)
            .clickable(onClick = onClick)
            .testTag("key_op_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun FnKey(
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (enabled) 0.5f else 0.15f))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = if (enabled) 0.25f else 0.08f), shape)
            .clickable(enabled = enabled, onClick = onClick)
            .testTag("key_fn_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun ActionKey(
    label: String,
    isHighlighted: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val bgColor = if (isHighlighted) {
        MaterialTheme.colorScheme.error.copy(alpha = 0.18f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    }
    val textColor = if (isHighlighted) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(bgColor)
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), shape)
            .clickable(onClick = onClick)
            .testTag("key_action_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun EvalKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
            .testTag("key_eval"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
