package com.example.engine

import com.example.model.Radix
import com.example.model.SignMode
import com.example.model.WordSize

object CalculatorEngine {

    fun mask(value: ULong, wordSize: WordSize): ULong {
        return value and wordSize.mask
    }

    fun toSignedLong(value: ULong, wordSize: WordSize): Long {
        val masked = mask(value, wordSize)
        return when (wordSize) {
            WordSize.BYTE -> masked.toByte().toLong()
            WordSize.WORD -> masked.toShort().toLong()
            WordSize.DWORD -> masked.toInt().toLong()
            WordSize.QWORD -> masked.toLong()
        }
    }

    fun toUnsignedString(value: ULong, wordSize: WordSize): String {
        val masked = mask(value, wordSize)
        return if (wordSize == WordSize.QWORD) {
            java.lang.Long.toUnsignedString(masked.toLong())
        } else {
            masked.toString()
        }
    }

    fun formatHex(value: ULong, wordSize: WordSize): String {
        val masked = mask(value, wordSize)
        return masked.toString(16).uppercase()
    }

    fun formatDec(value: ULong, wordSize: WordSize, signMode: SignMode): String {
        val masked = mask(value, wordSize)
        return if (signMode == SignMode.SIGNED) {
            toSignedLong(masked, wordSize).toString()
        } else {
            toUnsignedString(masked, wordSize)
        }
    }

    fun formatOct(value: ULong, wordSize: WordSize): String {
        val masked = mask(value, wordSize)
        return masked.toString(8)
    }

    fun formatBin(value: ULong, wordSize: WordSize, padded: Boolean = false): String {
        val masked = mask(value, wordSize)
        val raw = masked.toString(2)
        if (!padded) return raw
        val totalBits = wordSize.bits
        val paddedStr = raw.padStart(totalBits, '0')
        return paddedStr.chunked(4).joinToString(" ")
    }

    fun formatForRadix(value: ULong, radix: Radix, wordSize: WordSize, signMode: SignMode): String {
        return when (radix) {
            Radix.HEX -> formatHex(value, wordSize)
            Radix.DEC -> formatDec(value, wordSize, signMode)
            Radix.OCT -> formatOct(value, wordSize)
            Radix.BIN -> formatBin(value, wordSize, false)
        }
    }

    fun isBitSet(value: ULong, bitIndex: Int): Boolean {
        if (bitIndex !in 0..63) return false
        return ((value shr bitIndex) and 1uL) == 1uL
    }

    fun toggleBit(value: ULong, bitIndex: Int, wordSize: WordSize): ULong {
        if (bitIndex !in 0 until wordSize.bits) return value
        val bitMask = 1uL shl bitIndex
        return mask(value xor bitMask, wordSize)
    }

    fun setBit(value: ULong, bitIndex: Int, bitValue: Boolean, wordSize: WordSize): ULong {
        if (bitIndex !in 0 until wordSize.bits) return value
        val bitMask = 1uL shl bitIndex
        val result = if (bitValue) (value or bitMask) else (value and bitMask.inv())
        return mask(result, wordSize)
    }

    fun negate(value: ULong, wordSize: WordSize): ULong {
        // Two's complement negation: ~x + 1 which equals 0 - x (mod 2^N)
        return mask(0uL - value, wordSize)
    }

    fun bitwiseNot(value: ULong, wordSize: WordSize): ULong {
        return mask(value.inv(), wordSize)
    }

    fun parseNumber(text: String, radix: Radix, wordSize: WordSize, signMode: SignMode): ULong {
        val clean = text.trim()
        if (clean.isEmpty() || clean == "-") return 0uL

        if (radix == Radix.DEC && signMode == SignMode.SIGNED && clean.startsWith("-")) {
            val numStr = clean.substring(1)
            val num = numStr.toLongOrNull() ?: 0L
            return mask((-num).toULong(), wordSize)
        }

        val raw = try {
            when (radix) {
                Radix.HEX -> clean.toULong(16)
                Radix.DEC -> clean.toULong(10)
                Radix.OCT -> clean.toULong(8)
                Radix.BIN -> clean.toULong(2)
            }
        } catch (_: Exception) {
            0uL
        }
        return mask(raw, wordSize)
    }

    fun evaluateBinaryOp(
        op: String,
        a: ULong,
        b: ULong,
        wordSize: WordSize,
        signMode: SignMode
    ): ULong {
        return when (op) {
            "+" -> mask(a + b, wordSize)
            "-" -> mask(a - b, wordSize)
            "×", "*" -> mask(a * b, wordSize)
            "÷", "/" -> {
                if (b == 0uL) throw ArithmeticException("Division by zero")
                if (signMode == SignMode.SIGNED) {
                    val sA = toSignedLong(a, wordSize)
                    val sB = toSignedLong(b, wordSize)
                    mask((sA / sB).toULong(), wordSize)
                } else {
                    mask(a / b, wordSize)
                }
            }
            "%", "MOD" -> {
                if (b == 0uL) throw ArithmeticException("Modulo by zero")
                if (signMode == SignMode.SIGNED) {
                    val sA = toSignedLong(a, wordSize)
                    val sB = toSignedLong(b, wordSize)
                    mask((sA % sB).toULong(), wordSize)
                } else {
                    mask(a % b, wordSize)
                }
            }
            "AND" -> mask(a and b, wordSize)
            "OR" -> mask(a or b, wordSize)
            "XOR" -> mask(a xor b, wordSize)
            "NAND" -> mask((a and b).inv(), wordSize)
            "NOR" -> mask((a or b).inv(), wordSize)
            "<<" -> {
                val shift = (b.toLong() % wordSize.bits).toInt()
                if (shift < 0) mask(a, wordSize)
                else mask(a shl shift, wordSize)
            }
            ">>" -> {
                val shift = (b.toLong() % wordSize.bits).toInt()
                if (shift < 0) mask(a, wordSize)
                else if (signMode == SignMode.SIGNED) {
                    val signedA = toSignedLong(a, wordSize)
                    mask((signedA shr shift).toULong(), wordSize)
                } else {
                    mask(a shr shift, wordSize)
                }
            }
            else -> a
        }
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "OR", "NOR" -> 1
            "XOR" -> 2
            "AND", "NAND" -> 3
            "<<", ">>" -> 4
            "+", "-" -> 5
            "×", "*", "÷", "/", "%", "MOD" -> 6
            "NOT" -> 7
            else -> 0
        }
    }

    private fun isOperator(token: String): Boolean {
        return token in listOf("+", "-", "×", "*", "÷", "/", "%", "MOD", "AND", "OR", "XOR", "NAND", "NOR", "<<", ">>", "NOT")
    }

    /**
     * Evaluates a full arithmetic/bitwise expression.
     * Tokens are separated by whitespace.
     */
    fun evaluateExpression(
        expression: String,
        radix: Radix,
        wordSize: WordSize,
        signMode: SignMode
    ): Result<ULong> {
        return runCatching {
            val tokens = tokenize(expression)
            if (tokens.isEmpty()) return@runCatching 0uL

            val outputQueue = mutableListOf<String>()
            val operatorStack = ArrayDeque<String>()

            for (token in tokens) {
                when {
                    token == "(" -> {
                        operatorStack.addFirst(token)
                    }
                    token == ")" -> {
                        while (operatorStack.isNotEmpty() && operatorStack.first() != "(") {
                            outputQueue.add(operatorStack.removeFirst())
                        }
                        if (operatorStack.isNotEmpty() && operatorStack.first() == "(") {
                            operatorStack.removeFirst()
                        }
                    }
                    isOperator(token) -> {
                        val p = precedence(token)
                        while (operatorStack.isNotEmpty() &&
                            operatorStack.first() != "(" &&
                            precedence(operatorStack.first()) >= p
                        ) {
                            outputQueue.add(operatorStack.removeFirst())
                        }
                        operatorStack.addFirst(token)
                    }
                    else -> {
                        // Number literal
                        outputQueue.add(token)
                    }
                }
            }

            while (operatorStack.isNotEmpty()) {
                val op = operatorStack.removeFirst()
                if (op != "(" && op != ")") {
                    outputQueue.add(op)
                }
            }

            // Evaluate RPN
            val evalStack = ArrayDeque<ULong>()
            for (token in outputQueue) {
                if (token == "NOT") {
                    val a = if (evalStack.isNotEmpty()) evalStack.removeFirst() else 0uL
                    evalStack.addFirst(bitwiseNot(a, wordSize))
                } else if (isOperator(token)) {
                    val b = if (evalStack.isNotEmpty()) evalStack.removeFirst() else 0uL
                    val a = if (evalStack.isNotEmpty()) evalStack.removeFirst() else 0uL
                    val res = evaluateBinaryOp(token, a, b, wordSize, signMode)
                    evalStack.addFirst(res)
                } else {
                    val value = parseNumber(token, radix, wordSize, signMode)
                    evalStack.addFirst(value)
                }
            }

            val finalResult = if (evalStack.isNotEmpty()) evalStack.first() else 0uL
            mask(finalResult, wordSize)
        }
    }

    fun tokenize(expression: String): List<String> {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return emptyList()

        val tokens = mutableListOf<String>()
        val multiWordOps = listOf("AND", "NAND", "XOR", "NOR", "NOT", "OR", "MOD", "<<", ">>")
        var i = 0
        val len = trimmed.length

        while (i < len) {
            val c = trimmed[i]
            if (c.isWhitespace()) {
                i++
                continue
            }

            // Check multi-character operators
            var matchedOp: String? = null
            for (op in multiWordOps) {
                if (trimmed.regionMatches(i, op, 0, op.length, ignoreCase = true)) {
                    matchedOp = op
                    break
                }
            }

            if (matchedOp != null) {
                tokens.add(matchedOp)
                i += matchedOp.length
                continue
            }

            if (c in "+-×*÷/%()") {
                tokens.add(c.toString())
                i++
                continue
            }

            // Accumulate alphanumeric number token
            val start = i
            while (i < len && !trimmed[i].isWhitespace() && trimmed[i] !in "+-×*÷/%()") {
                // Check if an operator starts here
                var isOpStart = false
                for (op in multiWordOps) {
                    if (trimmed.regionMatches(i, op, 0, op.length, ignoreCase = true)) {
                        isOpStart = true
                        break
                    }
                }
                if (isOpStart) break
                i++
            }
            if (i > start) {
                tokens.add(trimmed.substring(start, i))
            }
        }

        return tokens
    }
}
