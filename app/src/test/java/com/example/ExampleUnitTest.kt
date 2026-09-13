package com.example

import com.example.engine.CalculatorEngine
import com.example.model.Radix
import com.example.model.SignMode
import com.example.model.WordSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testRadixConversions() {
    val value = 255uL
    assertEquals("FF", CalculatorEngine.formatHex(value, WordSize.QWORD))
    assertEquals("255", CalculatorEngine.formatDec(value, WordSize.QWORD, SignMode.UNSIGNED))
    assertEquals("377", CalculatorEngine.formatOct(value, WordSize.QWORD))
    assertEquals("11111111", CalculatorEngine.formatBin(value, WordSize.QWORD, false))
  }

  @Test
  fun testTwosComplementSignedNegative() {
    // 8-bit mode: 0xFF is -1 in signed mode
    val minusOneByte = 0xFFuL
    assertEquals(-1L, CalculatorEngine.toSignedLong(minusOneByte, WordSize.BYTE))
    assertEquals("-1", CalculatorEngine.formatDec(minusOneByte, WordSize.BYTE, SignMode.SIGNED))
    assertEquals("255", CalculatorEngine.formatDec(minusOneByte, WordSize.BYTE, SignMode.UNSIGNED))

    // Negation of 1 in 8-bit should give 0xFF
    val oneByte = 1uL
    assertEquals(0xFFuL, CalculatorEngine.negate(oneByte, WordSize.BYTE))
  }

  @Test
  fun testBitwiseOperations() {
    // 0x0F AND 0xF0 = 0x00
    val a = 0x0FuL
    val b = 0xF0uL
    assertEquals(0x00uL, CalculatorEngine.evaluateBinaryOp("AND", a, b, WordSize.BYTE, SignMode.UNSIGNED))
    // 0x0F OR 0xF0 = 0xFF
    assertEquals(0xFFuL, CalculatorEngine.evaluateBinaryOp("OR", a, b, WordSize.BYTE, SignMode.UNSIGNED))
    // 0xAA XOR 0x55 = 0xFF
    assertEquals(0xFFuL, CalculatorEngine.evaluateBinaryOp("XOR", 0xAAuL, 0x55uL, WordSize.BYTE, SignMode.UNSIGNED))
    // NOT 0x00 in BYTE mode = 0xFF
    assertEquals(0xFFuL, CalculatorEngine.bitwiseNot(0uL, WordSize.BYTE))
    // Shift left: 1 << 3 = 8
    assertEquals(8uL, CalculatorEngine.evaluateBinaryOp("<<", 1uL, 3uL, WordSize.BYTE, SignMode.UNSIGNED))
    // Shift right: 16 >> 2 = 4
    assertEquals(4uL, CalculatorEngine.evaluateBinaryOp(">>", 16uL, 2uL, WordSize.BYTE, SignMode.UNSIGNED))
  }

  @Test
  fun testBitBoardToggle() {
    var value = 0uL
    // Set bit 0 -> 1
    value = CalculatorEngine.toggleBit(value, 0, WordSize.QWORD)
    assertEquals(1uL, value)
    assertTrue(CalculatorEngine.isBitSet(value, 0))

    // Set bit 3 -> 1 + 8 = 9
    value = CalculatorEngine.toggleBit(value, 3, WordSize.QWORD)
    assertEquals(9uL, value)
    assertTrue(CalculatorEngine.isBitSet(value, 3))

    // Toggle bit 0 off -> 8
    value = CalculatorEngine.toggleBit(value, 0, WordSize.QWORD)
    assertEquals(8uL, value)
  }

  @Test
  fun testExpressionEvaluation() {
    // 10 + 20 * 2 = 50 in decimal
    val res1 = CalculatorEngine.evaluateExpression("10 + 20 × 2", Radix.DEC, WordSize.QWORD, SignMode.SIGNED)
    assertTrue(res1.isSuccess)
    assertEquals(50uL, res1.getOrNull())

    // (10 + 20) * 2 = 60 in decimal
    val res2 = CalculatorEngine.evaluateExpression("(10 + 20) × 2", Radix.DEC, WordSize.QWORD, SignMode.SIGNED)
    assertTrue(res2.isSuccess)
    assertEquals(60uL, res2.getOrNull())

    // Hex evaluation: A + B = 15 (21 in decimal)
    val res3 = CalculatorEngine.evaluateExpression("A + B", Radix.HEX, WordSize.QWORD, SignMode.SIGNED)
    assertTrue(res3.isSuccess)
    assertEquals(21uL, res3.getOrNull())
  }
}

