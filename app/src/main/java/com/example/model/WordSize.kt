package com.example.model

enum class WordSize(val bits: Int, val label: String, val mask: ULong) {
    QWORD(64, "64 BIT", 0xFFFFFFFFFFFFFFFFuL),
    DWORD(32, "32 BIT", 0x00000000FFFFFFFFuL),
    WORD(16, "16 BIT", 0x000000000000FFFFuL),
    BYTE(8, "8 BIT", 0x00000000000000FFuL);

    fun next(): WordSize = when (this) {
        QWORD -> DWORD
        DWORD -> WORD
        WORD -> BYTE
        BYTE -> QWORD
    }

    val signBitMask: ULong
        get() = 1uL shl (bits - 1)
}
