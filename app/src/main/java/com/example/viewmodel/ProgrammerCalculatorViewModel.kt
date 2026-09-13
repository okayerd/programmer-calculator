package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.CalculationEntity
import com.example.data.repository.CalculationRepository
import com.example.engine.CalculatorEngine
import com.example.model.Radix
import com.example.model.SignMode
import com.example.model.WordSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorSnapshot(
    val expression: String,
    val activeInputToken: String,
    val displayValue: ULong,
    val activeRadix: Radix,
    val wordSize: WordSize,
    val signMode: SignMode,
    val isEvaluated: Boolean
)

data class CalculatorUiState(
    val expression: String = "",
    val activeInputToken: String = "0",
    val displayValue: ULong = 0uL,
    val activeRadix: Radix = Radix.HEX,
    val wordSize: WordSize = WordSize.QWORD,
    val signMode: SignMode = SignMode.SIGNED,
    val errorMessage: String? = null,
    val isEvaluated: Boolean = false,
    val historyItems: List<CalculationEntity> = emptyList(),
    val isHistoryOpen: Boolean = false,
    val canUndo: Boolean = false
)

class ProgrammerCalculatorViewModel(
    private val repository: CalculationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val undoStack = mutableListOf<CalculatorSnapshot>()

    init {
        viewModelScope.launch {
            repository.recentCalculations.collect { history ->
                _uiState.update { it.copy(historyItems = history) }
            }
        }
    }

    private fun pushUndoSnapshot() {
        val current = _uiState.value
        undoStack.add(
            CalculatorSnapshot(
                expression = current.expression,
                activeInputToken = current.activeInputToken,
                displayValue = current.displayValue,
                activeRadix = current.activeRadix,
                wordSize = current.wordSize,
                signMode = current.signMode,
                isEvaluated = current.isEvaluated
            )
        )
        if (undoStack.size > 50) {
            undoStack.removeAt(0)
        }
        _uiState.update { it.copy(canUndo = true) }
    }

    fun onUndo() {
        if (undoStack.isNotEmpty()) {
            val snapshot = undoStack.removeAt(undoStack.lastIndex)
            _uiState.update {
                it.copy(
                    expression = snapshot.expression,
                    activeInputToken = snapshot.activeInputToken,
                    displayValue = snapshot.displayValue,
                    activeRadix = snapshot.activeRadix,
                    wordSize = snapshot.wordSize,
                    signMode = snapshot.signMode,
                    isEvaluated = snapshot.isEvaluated,
                    errorMessage = null,
                    canUndo = undoStack.isNotEmpty()
                )
            }
        }
    }

    fun onDigitInput(char: String) {
        val current = _uiState.value
        if (!current.activeRadix.isKeyEnabled(char)) return

        pushUndoSnapshot()

        val newToken = if (current.isEvaluated || current.activeInputToken == "0") {
            char
        } else {
            current.activeInputToken + char
        }

        val parsedVal = CalculatorEngine.parseNumber(
            text = newToken,
            radix = current.activeRadix,
            wordSize = current.wordSize,
            signMode = current.signMode
        )

        val newExpression = if (current.isEvaluated) {
            newToken
        } else {
            updateExpressionWithToken(current.expression, current.activeInputToken, newToken)
        }

        _uiState.update {
            it.copy(
                activeInputToken = newToken,
                displayValue = parsedVal,
                expression = newExpression,
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onOperatorInput(op: String) {
        val current = _uiState.value
        pushUndoSnapshot()

        val expr = if (current.expression.isBlank()) {
            CalculatorEngine.formatForRadix(current.displayValue, current.activeRadix, current.wordSize, current.signMode)
        } else {
            current.expression.trimEnd()
        }

        // Avoid duplicate trailing operator
        val cleanExpr = if (expr.endsWith("+") || expr.endsWith("-") ||
            expr.endsWith("×") || expr.endsWith("÷") || expr.endsWith("%") ||
            expr.endsWith("AND") || expr.endsWith("OR") || expr.endsWith("XOR") ||
            expr.endsWith("NAND") || expr.endsWith("NOR") ||
            expr.endsWith("<<") || expr.endsWith(">>")
        ) {
            val lastSpace = expr.lastIndexOf(' ')
            if (lastSpace != -1) expr.substring(0, lastSpace) else expr
        } else {
            expr
        }

        val newExpr = "$cleanExpr $op "

        _uiState.update {
            it.copy(
                expression = newExpr,
                activeInputToken = "",
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onParenthesisInput(paren: String) {
        val current = _uiState.value
        pushUndoSnapshot()
        val newExpr = if (paren == "(") {
            if (current.expression.isBlank() || current.expression.endsWith(" ") || current.isEvaluated) {
                if (current.isEvaluated) "(" else current.expression + "("
            } else {
                current.expression + " × ("
            }
        } else {
            current.expression + ")"
        }

        _uiState.update {
            it.copy(
                expression = newExpr,
                activeInputToken = "",
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onBitwiseNot() {
        val current = _uiState.value
        pushUndoSnapshot()
        val newVal = CalculatorEngine.bitwiseNot(current.displayValue, current.wordSize)
        val formatted = CalculatorEngine.formatForRadix(newVal, current.activeRadix, current.wordSize, current.signMode)

        _uiState.update {
            it.copy(
                displayValue = newVal,
                activeInputToken = formatted,
                expression = formatted,
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onNegate() {
        val current = _uiState.value
        pushUndoSnapshot()
        val newVal = CalculatorEngine.negate(current.displayValue, current.wordSize)
        val formatted = CalculatorEngine.formatForRadix(newVal, current.activeRadix, current.wordSize, current.signMode)

        _uiState.update {
            it.copy(
                displayValue = newVal,
                activeInputToken = formatted,
                expression = formatted,
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onBitToggled(bitIndex: Int) {
        val current = _uiState.value
        if (bitIndex !in 0 until current.wordSize.bits) return

        pushUndoSnapshot()
        val newVal = CalculatorEngine.toggleBit(current.displayValue, bitIndex, current.wordSize)
        val formatted = CalculatorEngine.formatForRadix(newVal, current.activeRadix, current.wordSize, current.signMode)

        val newExpr = if (current.expression.isBlank() || current.isEvaluated) {
            formatted
        } else {
            updateExpressionWithToken(current.expression, current.activeInputToken, formatted)
        }

        _uiState.update {
            it.copy(
                displayValue = newVal,
                activeInputToken = formatted,
                expression = newExpr,
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onRadixSelected(radix: Radix) {
        val current = _uiState.value
        if (current.activeRadix == radix) return

        pushUndoSnapshot()
        val formatted = CalculatorEngine.formatForRadix(current.displayValue, radix, current.wordSize, current.signMode)

        // Convert active token/expression to new radix representation
        val newExpr = if (current.isEvaluated || current.expression.isBlank()) {
            formatted
        } else {
            // Re-render expression tokens in new radix if simple, or preserve current
            formatted
        }

        _uiState.update {
            it.copy(
                activeRadix = radix,
                activeInputToken = formatted,
                expression = newExpr,
                errorMessage = null
            )
        }
    }

    fun onCycleWordSize() {
        val current = _uiState.value
        pushUndoSnapshot()
        val nextWordSize = current.wordSize.next()
        val maskedVal = CalculatorEngine.mask(current.displayValue, nextWordSize)
        val formatted = CalculatorEngine.formatForRadix(maskedVal, current.activeRadix, nextWordSize, current.signMode)

        _uiState.update {
            it.copy(
                wordSize = nextWordSize,
                displayValue = maskedVal,
                activeInputToken = formatted,
                expression = formatted,
                errorMessage = null
            )
        }
    }

    fun onSelectWordSize(wordSize: WordSize) {
        val current = _uiState.value
        if (current.wordSize == wordSize) return
        pushUndoSnapshot()
        val maskedVal = CalculatorEngine.mask(current.displayValue, wordSize)
        val formatted = CalculatorEngine.formatForRadix(maskedVal, current.activeRadix, wordSize, current.signMode)

        _uiState.update {
            it.copy(
                wordSize = wordSize,
                displayValue = maskedVal,
                activeInputToken = formatted,
                expression = formatted,
                errorMessage = null
            )
        }
    }

    fun onToggleSignMode() {
        val current = _uiState.value
        pushUndoSnapshot()
        val nextSignMode = current.signMode.toggle()
        val formatted = CalculatorEngine.formatForRadix(current.displayValue, current.activeRadix, current.wordSize, nextSignMode)

        _uiState.update {
            it.copy(
                signMode = nextSignMode,
                activeInputToken = formatted,
                expression = if (current.isEvaluated || current.expression.isBlank()) formatted else current.expression
            )
        }
    }

    fun onBackspace() {
        val current = _uiState.value
        if (current.isEvaluated) {
            onClear()
            return
        }

        pushUndoSnapshot()

        if (current.activeInputToken.isNotEmpty()) {
            val newToken = current.activeInputToken.dropLast(1)
            val effectiveToken = if (newToken.isEmpty()) "0" else newToken
            val parsedVal = CalculatorEngine.parseNumber(effectiveToken, current.activeRadix, current.wordSize, current.signMode)
            val newExpr = if (current.expression.isNotEmpty()) current.expression.dropLast(1).trimEnd() else ""

            _uiState.update {
                it.copy(
                    activeInputToken = if (newToken.isEmpty()) "" else newToken,
                    displayValue = parsedVal,
                    expression = newExpr,
                    errorMessage = null
                )
            }
        } else if (current.expression.isNotEmpty()) {
            val newExpr = current.expression.trimEnd().dropLast(1).trimEnd()
            _uiState.update {
                it.copy(
                    expression = newExpr,
                    errorMessage = null
                )
            }
        }
    }

    fun onClear() {
        pushUndoSnapshot()
        _uiState.update {
            it.copy(
                expression = "",
                activeInputToken = "0",
                displayValue = 0uL,
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    fun onEvaluate() {
        val current = _uiState.value
        val exprToEval = if (current.expression.isNotBlank()) {
            current.expression
        } else {
            current.activeInputToken
        }

        if (exprToEval.isBlank()) return
        pushUndoSnapshot()

        val result = CalculatorEngine.evaluateExpression(
            expression = exprToEval,
            radix = current.activeRadix,
            wordSize = current.wordSize,
            signMode = current.signMode
        )

        result.onSuccess { resValue ->
            val formatted = CalculatorEngine.formatForRadix(resValue, current.activeRadix, current.wordSize, current.signMode)

            // Save to database
            viewModelScope.launch {
                val entity = CalculationEntity(
                    expression = exprToEval,
                    resultHex = CalculatorEngine.formatHex(resValue, current.wordSize),
                    resultDec = CalculatorEngine.formatDec(resValue, current.wordSize, current.signMode),
                    resultOct = CalculatorEngine.formatOct(resValue, current.wordSize),
                    resultBin = CalculatorEngine.formatBin(resValue, current.wordSize, false),
                    resultRawULong = resValue.toLong(),
                    wordSize = current.wordSize.name,
                    signMode = current.signMode.name,
                    timestamp = System.currentTimeMillis()
                )
                repository.saveCalculation(entity)
            }

            _uiState.update {
                it.copy(
                    displayValue = resValue,
                    activeInputToken = formatted,
                    expression = formatted,
                    isEvaluated = true,
                    errorMessage = null
                )
            }
        }.onFailure { ex ->
            _uiState.update {
                it.copy(
                    errorMessage = ex.localizedMessage ?: "Calculation Error"
                )
            }
        }
    }

    fun onToggleHistory(open: Boolean) {
        _uiState.update { it.copy(isHistoryOpen = open) }
    }

    fun onHistoryItemClicked(item: CalculationEntity) {
        pushUndoSnapshot()
        val value = item.resultRawULong.toULong()
        val wordSize = runCatching { WordSize.valueOf(item.wordSize) }.getOrDefault(WordSize.QWORD)
        val signMode = runCatching { SignMode.valueOf(item.signMode) }.getOrDefault(SignMode.SIGNED)
        val formatted = CalculatorEngine.formatForRadix(value, _uiState.value.activeRadix, wordSize, signMode)

        _uiState.update {
            it.copy(
                displayValue = value,
                activeInputToken = formatted,
                expression = item.expression,
                wordSize = wordSize,
                signMode = signMode,
                isEvaluated = true,
                isHistoryOpen = false,
                errorMessage = null
            )
        }
    }

    fun onClearHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    private fun updateExpressionWithToken(expr: String, oldToken: String, newToken: String): String {
        return if (expr.isEmpty() || oldToken.isEmpty()) {
            newToken
        } else if (expr.endsWith(oldToken)) {
            expr.dropLast(oldToken.length) + newToken
        } else {
            expr + newToken
        }
    }

    companion object {
        fun provideFactory(repository: CalculationRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProgrammerCalculatorViewModel(repository) as T
                }
            }
        }
    }
}
