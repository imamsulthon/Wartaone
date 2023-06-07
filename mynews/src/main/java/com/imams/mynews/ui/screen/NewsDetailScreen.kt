package com.imams.mynews.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.imams.core.util.simpleFormattedDate
import com.imams.domain.model.News
import com.imams.mynews.R

@Composable
fun NewsDetailScreen(
    uuid: String,
    viewModel: NewsDetailVM = hiltViewModel<NewsDetailVM>().apply { fetchData(uuid) }
) {
    val isRefresh by viewModel.isRefreshing.collectAsState()
    val data by viewModel.data.collectAsState()
    val isRefreshing = rememberSwipeRefreshState(isRefreshing = isRefresh)
    NewsDetailContent(
        item = data,
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh(uuid)
        },
    )
    LaunchedEffect(Unit) {
        viewModel.fetchData(uuid)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsDetailContent(
    item: News,
    isRefreshing: SwipeRefreshState,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = item.title, maxLines = 1) },
            )
        }
    ) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = isRefreshing,
            onRefresh = { onRefresh.invoke() }
        ) {
            val webViewState = rememberWebViewState(item.url)
            val modComp = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .placeholder(
                    visible = isRefreshing.isRefreshing,
                    highlight = PlaceholderHighlight.shimmer(),
                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
            ) {
                Image(
                    modifier = modComp
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = item.imageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(R.drawable.newspaper)
                            }).build()
                    ),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "some_description",
                )

                Text(
                    modifier = modComp, text = item.title,
                    style = MaterialTheme.typography.titleLarge)

                Text(modifier = modComp, text = item.description,
                    style = MaterialTheme.typography.titleMedium)

                Row(modifier = modComp,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = item.source, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 5.dp))
                    Text(text = item.publishedAt.simpleFormattedDate(), style = MaterialTheme.typography.labelSmall)
                }

                LazyRow(modifier = modComp, horizontalArrangement = Arrangement.Start) {
                    items(item.categories) {
                        CategoryLabel(label = it)
                    }
                }

                Card(
                    modifier = Modifier.padding(vertical = 5.dp),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                ) {
                    WebView(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
                        state = webViewState,
                    )
                }

            }
        }
    }

}

@Composable
fun CategoryLabel(label: String) {
    AssistChip(
        modifier = Modifier.padding(end = 5.dp),
        onClick = {  },
        label = {
            Text(text = label)
        }
    )
}

@Preview
@Composable
private fun DetailContentPreview() {
    val news = News("uuid", "Title News 1", "Some Description of News 1")
    NewsDetailContent(
        item = news,
        isRefreshing = rememberSwipeRefreshState(isRefreshing = true),
        onRefresh = {},
    )
}