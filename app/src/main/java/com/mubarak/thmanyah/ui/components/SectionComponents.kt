package com.mubarak.thmanyah.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mubarak.thmanyah.data.model.ContentItem
import com.mubarak.thmanyah.data.model.Section
import com.mubarak.thmanyah.data.model.SectionType

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
            SectionType.SQUARE -> SquareRow(section.items)
            SectionType.BIG_SQUARE -> BigSquareRow(section.items)
            SectionType.TWO_LINES_GRID -> TwoLinesGridRow(section.items)
            SectionType.QUEUE -> QueueRow(section.items)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SquareRow(items: List<ContentItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { "${it.id}_${items.indexOf(it)}" }) { item ->
            SquareCard(item = item, imageSize = 120.dp)
        }
    }
}

@Composable
fun SquareCard(item: ContentItem, imageSize: Dp, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(imageSize),
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(
            model = item.avatarUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (item.authorOrPodcastName.isNotBlank()) {
            Text(
                text = item.authorOrPodcastName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun BigSquareRow(items: List<ContentItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { "${it.id}_${items.indexOf(it)}" }) { item ->
            SquareCard(item = item, imageSize = 180.dp)
        }
    }
}

@Composable
private fun TwoLinesGridRow(items: List<ContentItem>) {
    val pairs = items.chunked(2)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pairs, key = { pair -> pair.joinToString { it.id } }) { pair ->
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                pair.forEach { item ->
                    EpisodeListTile(item = item)
                }
            }
        }
    }
}

@Composable
fun EpisodeListTile(item: ContentItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(280.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.avatarUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (item.authorOrPodcastName.isNotBlank()) {
                Text(
                    text = item.authorOrPodcastName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = formatDuration(item.duration),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QueueRow(items: List<ContentItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.withIndex().toList(), key = { "${it.value.id}_${it.index}" }) { (idx, item) ->
            QueueCard(item = item, rank = idx + 1)
        }
    }
}

@Composable
private fun QueueCard(item: ContentItem, rank: Int) {
    Column(
        modifier = Modifier.width(120.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box {
            AsyncImage(
                model = item.avatarUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            // Rank badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "#$rank",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (item.episodeCount != null) {
            Text(
                text = "${item.episodeCount} episodes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


fun formatDuration(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes} min"
    }
}