package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AboutPrivacyDialog
import com.example.ui.components.BitBoardSection
import com.example.ui.components.DisplaySection
import com.example.ui.components.HistoryBottomSheet
import com.example.ui.components.KeypadSection
import com.example.ui.components.RadixRowSection
import com.example.ui.components.WordSizeBar
import com.example.viewmodel.ProgrammerCalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: ProgrammerCalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var isAboutOpen by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 560.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Üst bölüm: Sonuç / Expression
            DisplaySection(
                expression = uiState.expression,
                displayValue = uiState.displayValue,
                activeRadix = uiState.activeRadix,
                wordSize = uiState.wordSize,
                signMode = uiState.signMode,
                errorMessage = uiState.errorMessage,
                canUndo = uiState.canUndo,
                onUndoClick = { viewModel.onUndo() },
                onHistoryClick = { viewModel.onToggleHistory(true) },
                onAboutClick = { isAboutOpen = true }
            )

            // 2. Altında: "HEX", "DEC", "OCT", "BIN"
            RadixRowSection(
                displayValue = uiState.displayValue,
                activeRadix = uiState.activeRadix,
                wordSize = uiState.wordSize,
                signMode = uiState.signMode,
                onRadixSelect = { viewModel.onRadixSelected(it) }
            )

            // 3. Ardından: 64 BIT | SIGNED
            WordSizeBar(
                wordSize = uiState.wordSize,
                signMode = uiState.signMode,
                onSelectWordSize = { viewModel.onSelectWordSize(it) },
                onToggleSignMode = { viewModel.onToggleSignMode() }
            )

            // 4. Sonrasında dokunulabilir bit alanı: "63 62 61 ... 3 2 1 0"
            BitBoardSection(
                displayValue = uiState.displayValue,
                wordSize = uiState.wordSize,
                onBitToggle = { bitIndex -> viewModel.onBitToggled(bitIndex) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 5. Alt bölümde kompakt hesap makinesi tuş takımı
            KeypadSection(
                activeRadix = uiState.activeRadix,
                canUndo = uiState.canUndo,
                onDigitClick = { viewModel.onDigitInput(it) },
                onOperatorClick = { viewModel.onOperatorInput(it) },
                onParenthesisClick = { viewModel.onParenthesisInput(it) },
                onBitwiseNotClick = { viewModel.onBitwiseNot() },
                onNegateClick = { viewModel.onNegate() },
                onClearClick = { viewModel.onClear() },
                onBackspaceClick = { viewModel.onBackspace() },
                onEvaluateClick = { viewModel.onEvaluate() },
                onUndoClick = { viewModel.onUndo() }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // History Bottom Sheet
        if (uiState.isHistoryOpen) {
            HistoryBottomSheet(
                historyItems = uiState.historyItems,
                onItemClick = { item -> viewModel.onHistoryItemClicked(item) },
                onClearHistory = { viewModel.onClearHistory() },
                onDismiss = { viewModel.onToggleHistory(false) }
            )
        }

        // About & Privacy Dialog
        if (isAboutOpen) {
            AboutPrivacyDialog(
                onDismiss = { isAboutOpen = false }
            )
        }
    }
}
