package com.mubarak.thmanyah.domain.model

data class Section(
    val id: String,
    val name: String,
    val type: SectionType,
    val contentType: ContentType,
    val order: Int,
    val items: List<ContentItem>
)
