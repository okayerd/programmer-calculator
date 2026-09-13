package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SignMode
import com.example.model.WordSize

@Composable
fun WordSizeBar(
    wordSize: WordSize,
    signMode: SignMode,
    onSelectWordSize: (WordSize) -> Unit,
    onToggleSignMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Word Size Button & Dropdown
        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .clickable { menuExpanded = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("btn_word_size"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = wordSize.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Word Size",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(16.dp)
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                WordSize.values().forEach { size ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${size.label} (${when(size) {
                                    WordSize.QWORD -> "QWORD"
                                    WordSize.DWORD -> "DWORD"
                                    WordSize.WORD -> "WORD"
                                    WordSize.BYTE -> "BYTE"
                                }})",
                                fontFamily = FontFamily.Monospace,
                                color = if (size == wordSize) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onSelectWordSize(size)
                            menuExpanded = false
                        }
                    )
                }
            }
        }

        // Divider
        Text(
            text = "|",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )

        // Signed / Unsigned Toggle Button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (signMode == SignMode.SIGNED)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
                .border(
                    1.dp,
                    if (signMode == SignMode.SIGNED)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    RoundedCornerShape(6.dp)
                )
                .clickable(onClick = onToggleSignMode)
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .testTag("btn_sign_mode"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = signMode.label,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (signMode == SignMode.SIGNED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
