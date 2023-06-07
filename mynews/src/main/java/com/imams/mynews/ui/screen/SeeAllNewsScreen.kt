package com.imams.mynews.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.imams.domain.model.News

@Composable
fun SeeAllNewsScreen(
    viewModel: SeeAllNewsVM = hiltViewModel(),
    onClick: (String) -> Unit
) {
    val allNews = viewModel.fetchData().collectAsLazyPagingItems()
    ListContent(listState = allNews, onClick = { onClick.invoke(it.uuid) })
}

@Composable
internal fun ListContent(
    listState: LazyPagingItems<News>,
    onClick: (News) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        handleState(listState.loadState.refresh)
        handleState(listState.loadState.prepend)
        items(
            count = listState.itemCount,
            key = listState.itemKey(),
            contentType = listState.itemContentType()
        ) { index ->
            val item = listState[index]
            item?.let {
                LandscapeItemNews(
                    modifier = Modifier
                        .height(120.dp)
                        .padding(vertical = 10.dp),
                    item = it,
                    onClickItem = { onClick.invoke(it) }
                )
            }
        }
        handleState(listState.loadState.append)
    }
}

fun LazyListScope.handleState(state: LoadState) {
    when (state) {
        is LoadState.NotLoading -> Unit
        is LoadState.Loading -> {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
        is LoadState.Error -> {
            item {
                Text(
                    text = "Error",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}