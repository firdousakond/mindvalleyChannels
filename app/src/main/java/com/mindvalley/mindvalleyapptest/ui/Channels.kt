package com.mindvalley.mindvalleyapptest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mindvalley.mindvalleyapptest.domain.MAX_ROW_COUNT
import com.mindvalley.mindvalleyapptest.domain.model.ChannelEntity
import com.mindvalley.mindvalleyapptest.domain.model.LatestMediaEntity
import com.mindvalley.mindvalleyapptest.ui.theme.Grey500
import com.mindvalley.mindvalleyapptest.ui.theme.Typography
import com.mindvalley.mindvalleyapptest.ui.theme.White
import com.mindvalley.mindvalleyapptest.ui.theme.dividerColor
import kotlin.collections.List

@Composable
fun Channels(modifier: Modifier = Modifier, channel: List<ChannelEntity>) {

    LazyColumn(
        modifier = modifier.padding(start = 20.dp, end = 20.dp)
    ) {
        items(count = channel.count()) { index ->
            val isSeries = channel[index].series.isNullOrEmpty().not()
            ChannelsHeaderItem(modifier, channel[index], isSeries)
            val latestMedia = channel[index].latestMedia
            var rowIndex = 0
            latestMedia?.let {
                val columnCount = if (latestMedia.count() % MAX_ROW_COUNT == 0) {
                    latestMedia.count() / MAX_ROW_COUNT
                } else {
                    latestMedia.count() / MAX_ROW_COUNT + 1
                }
                for (i in 0 until columnCount) {
                    val startIndex = rowIndex * MAX_ROW_COUNT
                    val endIndex =
                        if ((rowIndex * MAX_ROW_COUNT) + MAX_ROW_COUNT > latestMedia.count()) {
                            latestMedia.count()
                        } else {
                            (rowIndex * MAX_ROW_COUNT) + MAX_ROW_COUNT
                        }

                    val mediaList = latestMedia.subList(startIndex, endIndex)
                    LazyRow {
                        itemsIndexed(mediaList) { index, media ->
                            if (index < MAX_ROW_COUNT) {
                                ChannelsItem(modifier = modifier, media, isSeries)
                            }

                        }
                    }
                    rowIndex++
                }
            }
        }
    }
    Divider(
        color = dividerColor, modifier = modifier
            .fillMaxWidth()
            .width(1.dp)
            .padding(top = 50.dp, start = 10.dp, end = 10.dp)
    )

}

@Composable
fun ChannelsHeaderItem(modifier: Modifier, channel: ChannelEntity, isSeries: Boolean) {
    val mediaCount = if (isSeries) {
        "${channel.mediaCount} series"
    } else {
        "${channel.mediaCount} episodes"
    }
    Row(modifier = modifier) {
        AsyncImage(
            modifier = iconModifier,
            model = channel.iconAsset?.url,
            contentDescription = null,
            contentScale = ContentScale.None
        )
        Column(modifier = modifier) {
            Text(
                modifier = modifier
                    .padding(top = 10.dp),
                text = channel.title ?: "",
                style = Typography.h6, color = White
            )
            Text(
                modifier = modifier
                    .padding(top = 5.dp),
                text = mediaCount,
                style = Typography.body1, color = Grey500
            )
        }

    }

}

@Composable
fun ChannelsItem(modifier: Modifier, media: LatestMediaEntity, isSeries: Boolean) {
    val imageModifier = if (isSeries) {
        imageModifierLandscape
    } else {
        imageModifierPortrait
    }

    ConstraintLayout(modifier = modifier) {
        Column(
            modifier = modifier.padding(top = 20.dp, end = 10.dp)
        ) {
            AsyncImage(
                modifier = imageModifier.align(Alignment.CenterHorizontally),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(media.coverAsset?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = modifier
                    .padding(top = 10.dp),
                text = media.title ?: "",
                style = Typography.subtitle1
            )
        }
    }
}