# Programmer Calculator

A modern, fast, and 100% offline **Programmer Calculator** designed for software engineers, embedded system developers, and computer science students. Built with modern Android technologies: **Kotlin**, **Jetpack Compose (Material 3)**, and **Room Database**.

Developed with care by **Noor Tech Apps**.

---

## Key Features

- **Multi-Radix Real-Time Conversion:** Simultaneous live display of values in **HEX (Hexadecimal)**, **DEC (Decimal)**, **OCT (Octal)**, and **BIN (Binary)**.
- **Interactive 64-Bit Bitboard:** Tap any bit (from 0 to 63) to toggle between `0` and `1` in real-time, grouped into clean 4-bit nibbles.
- **Configurable Word Sizes:** Instant switching and bit-masking between:
  - **QWORD** (64-bit)
  - **DWORD** (32-bit)
  - **WORD** (16-bit)
  - **BYTE** (8-bit)
- **Signed & Unsigned Arithmetic:** Full support for Two's Complement signed representation and unsigned calculations.
- **Bitwise Logic Operations:** Comprehensive bitwise operator suite:
  - `AND`, `OR`, `XOR`, `NOT`, `NAND`, `NOR`
  - Bit Shifts: Logical Left Shift (`<<`) and Right Shift (`>>`)
- **Expression Evaluation:** Supports complex arithmetic expressions with operator precedence and parentheses `(`, `)`.
- **50-Step Undo History:** Dedicated snapshot stack allows seamless undo of calculations, bit toggles, and base conversions.
- **Local Calculation History:** Secure, sandboxed SQLite/Room database saves past calculations locally. Tap any historical entry to restore it instantly.
- **Copy to Clipboard:** Long-press any radix row (HEX, DEC, OCT, BIN) to immediately copy formatted results.
- **100% Offline & Private:** Zero analytics, zero ad tracking, zero telemetry. Works completely without an internet connection.

---

## Tech Stack & Architecture

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern + Clean Architecture
- **State Management:** Kotlin Coroutines & `StateFlow`
- **Local Persistence:** Android [Room Database](https://developer.android.com/training/data-storage/room)
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`) with Version Catalog (`libs.versions.toml`)

---

## Project Structure

```text
programmer-calculator/
├── app/
│   ├── src/main/java/com/example/
│   │   ├── engine/          # Bitwise and arithmetic calculation engine
│   │   ├── viewmodel/       # StateFlow and calculator state management
│   │   ├── model/           # Radix, WordSize, and SignMode models
│   │   ├── data/local/      # Room database entities and DAOs
│   │   └── ui/
│   │       ├── screens/     # Main Calculator composable screens
│   │       ├── components/  # Bitboard, Keypad, Radix rows, About/Privacy dialog
│   │       └── theme/       # Dark/Light developer color palette & typography
│   └── build.gradle.kts     # App build configuration
├── docs/                    # Privacy Policy web page for GitHub Pages
├── PRIVACY_POLICY.md        # Complete privacy policy
└── build.gradle.kts         # Root Gradle build configuration
```

---

## Getting Started & Building Locally

### Prerequisites
- [Android Studio Ladybug or newer](https://developer.android.com/studio)
- JDK 17 or JDK 21
- Android SDK Platform 36 (Min SDK: 24 / Android 7.0+)

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/okayerd/programmer-calculator.git
   cd programmer-calculator
   ```

2. Open the project in Android Studio.

3. Run unit tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. Build Debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

---

## Privacy Policy

Programmer Calculator is committed to user privacy. We do not collect, store, or transmit any personal data. For our complete policy, please see our [Privacy Policy](PRIVACY_POLICY.md) or visit our online page at [GitHub Pages](https://okayerd.github.io/programmer-calculator/).

---

## Contact & Support

**Noor Tech Apps**  
- Email: [noortechapp@gmail.com](mailto:noortechapp@gmail.com)  
- Feedback and feature requests are always welcome!
