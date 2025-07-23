package com.example.core_ui.theme

import androidx.compose.ui.graphics.Color

fun getSecondaryForPrimary(primary: Color): Color = when (primary) {
    Color(0xFF2AE881) -> Color(0xFFD4FAE6) // зелёный → светло-зелёный
    Color(0xFFE46962) -> Color(0xFFFFB4A9) // красный → светло-красный
    Color(0xFF62A3E4) -> Color(0xFFB3DAF7) // синий → светло-синий
    Color(0xFFF39C12) -> Color(0xFFFFE5B4) // оранжевый → светло-оранжевый
    Color(0xFF8E44AD) -> Color(0xFFE1BEE7) // фиолетовый → светло-фиолетовый
    else -> Color(0xFFD4FAE6)
} 