package com.example.model

enum class SignMode(val label: String) {
    SIGNED("SIGNED"),
    UNSIGNED("UNSIGNED");

    fun toggle(): SignMode = if (this == SIGNED) UNSIGNED else SIGNED
}
