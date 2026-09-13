package com.example.model

enum class Radix(val base: Int, val label: String, val prefix: String) {
    HEX(16, "HEX", "0x"),
    DEC(10, "DEC", ""),
    OCT(8, "OCT", "0o"),
    BIN(2, "BIN", "0b");

    fun isValidChar(c: Char): Boolean {
        return when (this) {
            HEX -> c in '0'..'9' || c in 'A'..'F' || c in 'a'..'f'
            DEC -> c in '0'..'9'
            OCT -> c in '0'..'7'
            BIN -> c == '0' || c == '1'
        }
    }

    fun isKeyEnabled(charString: String): Boolean {
        if (charString.length != 1) return true
        val c = charString[0]
        return when (this) {
            HEX -> c in '0'..'9' || c in 'A'..'F'
            DEC -> c in '0'..'9'
            OCT -> c in '0'..'7'
            BIN -> c == '0' || c == '1'
        }
    }
}
