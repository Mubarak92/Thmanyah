package com.mubarak.thmanyah.domain.model

enum class SectionType {
    SQUARE, BIG_SQUARE, TWO_LINES_GRID, QUEUE;

    companion object {
        fun from(raw: String): SectionType = when (raw.lowercase().trim()) {
            "square" -> SQUARE
            "big_square", "big square" -> BIG_SQUARE
            "2_lines_grid" -> TWO_LINES_GRID
            "queue" -> QUEUE
            else -> SQUARE
        }
    }
}
