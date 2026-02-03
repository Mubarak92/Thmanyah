package com.mubarak.thmanyah.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mubarak.thmanyah.domain.model.ContentItem
import com.mubarak.thmanyah.domain.model.Section
import com.mubarak.thmanyah.domain.model.SectionType
import kotlin.collections.withIndex

@Composable
fun SectionRow(section: Section, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = section.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        when (section.type) {
            SectionType.SQUARE -> SquareRow(section.items, 120.dp)
            SectionType.BIG_SQUARE -> SquareRow(section.items, 180.dp)
            SectionType.TWO_LINES_GRID -> TwoLinesGridRow(section.items)
            SectionType.QUEUE -> QueueRow(section.items)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SquareRow(items: List<ContentItem>, size: androidx.compose.ui.unit.Dp) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items, key = { it.id }) { item ->
            Column(modifier = Modifier.width(size)) {
                AsyncImage(
                    model = item.imageUrl, contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(size).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(Modifier.height(6.dp))
                Text(item.name, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (item.authorOrPodcastName.isNotBlank()) {
                    Text(item.authorOrPodcastName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun TwoLinesGridRow(items: List<ContentItem>) {
    val pairs = items.chunked(2)
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(pairs, key = { it.joinToString { i -> i.id } }) { pair ->
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { item -> EpisodeTile(item) }
            }
        }
    }
}

@Composable
private fun EpisodeTile(item: ContentItem) {
    Row(
        modifier = Modifier.width(280.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surface).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl, contentDescription = item.name, contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (item.authorOrPodcastName.isNotBlank()) {
                Text(item.authorOrPodcastName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            Text(item.formattedDuration, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QueueRow(items: List<ContentItem>) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items.withIndex().toList(), key = { it.value.id }) { (idx, item) ->
            Column(modifier = Modifier.width(120.dp)) {
                Box {
                    AsyncImage(
                        model = item.imageUrl, contentDescription = item.name, contentScale = ContentScale.Crop,
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    Box(
                        modifier = Modifier.align(Alignment.BottomStart).padding(4.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) { Text("#${idx + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary) }
                }
                Spacer(Modifier.height(6.dp))
                Text(item.name, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                item.episodeCount?.let { Text("$it episodes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
    }
}
