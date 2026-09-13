package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CalculatorEngine
import com.example.model.Radix
import com.example.model.SignMode
import com.example.model.WordSize

@Composable
fun DisplaySection(
    expression: String,
    displayValue: ULong,
    activeRadix: Radix,
    wordSize: WordSize,
    signMode: SignMode,
    errorMessage: String?,
    canUndo: Boolean,
    onUndoClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAboutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Toolbar with History, Undo & About
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PROGRAMMER",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onUndoClick,
                    enabled = canUndo,
                    modifier = Modifier.testTag("btn_undo_top")
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Undo",
                        tint = if (canUndo) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }

                IconButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.testTag("btn_history_top")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onAboutClick,
                    modifier = Modifier.testTag("btn_about_top")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About & Privacy",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Expression line (secondary)
        val displayText = when {
            errorMessage != null -> errorMessage
            expression.isNotBlank() -> expression
            else -> "0"
        }

        Text(
            text = displayText,
            style = MaterialTheme.typography.bodyMedium,
            color = if (errorMessage != null) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("text_expression")
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Large Primary Result Display
        val mainResultFormatted = CalculatorEngine.formatForRadix(
            value = displayValue,
            radix = activeRadix,
            wordSize = wordSize,
            signMode = signMode
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState, reverseScrolling = true),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mainResultFormatted,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = if (mainResultFormatted.length > 12) 28.sp
                    else if (mainResultFormatted.length > 8) 34.sp
                    else 40.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.testTag("text_primary_result")
            )
        }
    }
}
