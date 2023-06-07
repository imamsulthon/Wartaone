package com.imams.mynews.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.imams.core.util.simpleFormattedDate
import com.imams.domain.model.News
import com.imams.mynews.R
import com.imams.mynews.ui.component.ChildLayout
import com.imams.mynews.ui.component.LoadItemAfterSafeCast
import com.imams.mynews.ui.component.VerticalScroll

@Composable
fun HomeNewsScreen(
    viewModel: HomeScreenVM = hiltViewModel(),
    seeAll: () -> Unit,
    onClickItem: (String) -> Unit,
) {
    val onRefreshState by viewModel.onRefresh.collectAsState()
    val isRefreshing = rememberSwipeRefreshState(isRefreshing = onRefreshState)
    val headlineNews by viewModel.headlineNews.collectAsState()
    val topNews by viewModel.topNews.collectAsState()
    val allNews by viewModel.allNews.collectAsState()
    val loading1 by viewModel.loading1.collectAsState()
    val loading2 by viewModel.loading2.collectAsState()
    val loading3 by viewModel.loading3.collectAsState()

    Content(
        isLoading1 = loading1, isLoading2 = loading2, isLoading3 = loading3,
        isRefreshing = isRefreshing,
        headlineNews = headlineNews, topNews = topNews, allNews = allNews,
        onRefresh = { viewModel.refresh() },
        onClickSeeAll = { seeAll.invoke() },
        onClickItem = { onClickItem.invoke(it.uuid) }
    )

    LaunchedEffect(Unit) {
        viewModel.fetchData()
    }
}

@Composable
private fun Content(
    isLoading1: Boolean,
    isLoading2: Boolean,
    isLoading3: Boolean,
    isRefreshing: SwipeRefreshState,
    headlineNews: List<News>,
    topNews: List<News>,
    allNews: List<News>,
    onRefresh: () -> Unit,
    onClickSeeAll: () -> Unit,
    onClickItem: (News) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = isRefreshing,
            onRefresh = { onRefresh.invoke() }
        ) {
            VerticalScroll(modifier = Modifier.fillMaxSize(),
                ChildLayout(
                    contentType = "Headline_News",
                    content = {
                        HeadlineNewsSection(
                            isLoading = isLoading1, list = headlineNews,
                            onClickItem = { onClickItem.invoke(it) }
                        )
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                ),
                ChildLayout(
                    contentType = "Top_News",
                    content = {
                        TopNewsSection(
                            isLoading = isLoading2,
                            list = topNews,
                            seeAll = { onClickSeeAll.invoke() },
                            onClickItem = { onClickItem.invoke(it) }
                        )
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                ),
                *AllNewsSection(
                    isLoading = isLoading3,
                    allNews = allNews,
                    onClickItem = { onClickItem.invoke(it) },
                    seeAll = { onClickSeeAll.invoke() }
                )
            )
        }

    }
}

@Composable
private fun HeadlineNewsSection(
    isLoading: Boolean,
    list: List<News>,
    onClickItem: (News) -> Unit,
) {
    val loading by remember { derivedStateOf { isLoading } }
    val isNotEmpty by remember { derivedStateOf { list.isNotEmpty() && !isLoading } }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Headline News",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
        Shimmer(isLoading = loading)
        AnimatedVisibility(visible = isNotEmpty) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                items(items = list) {
                    HeadlineItem(
                        modifier = Modifier
                            .width(200.dp)
                            .height(160.dp)
                            .padding(start = 10.dp),
                        item = it,
                        onClickItem = { onClickItem.invoke(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopNewsSection(
    isLoading: Boolean,
    list: List<News>,
    seeAll: () -> Unit,
    onClickItem: (News) -> Unit
) {
    val loading = remember { mutableStateOf(isLoading) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (tvTitle, tvSeeAll, vLoading, vList) = createRefs()
        Text(
            text = "Top News",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .constrainAs(tvTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Shimmer(isLoading = isLoading,
            modifier = Modifier.constrainAs(vLoading) {
                top.linkTo(tvTitle.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
        })
        AnimatedVisibility(visible = list.isNotEmpty(),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .constrainAs(tvSeeAll) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {
            Text(
                text = "See All",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { seeAll.invoke() },
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(vList) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(tvTitle.bottom)
                },
            horizontalArrangement = Arrangement.Start
        ) {
            items(items = list) {
                PortraitItem(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                        .padding(start = 10.dp),
                    item = it,
                    onLoading = false,
                    onClickItem = { onClickItem.invoke(it) }
                )
            }
        }
    }
}

@Composable
private fun AllNewsSection(
    isLoading: Boolean,
    allNews: List<News>,
    onClickItem: (News) -> Unit,
    seeAll: () -> Unit,
    loading: MutableState<Boolean> = mutableStateOf(isLoading)
) = arrayOf(
    ChildLayout(
        contentType = "all_news_title",
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "All News",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                )
                AnimatedVisibility(visible = allNews.isNotEmpty()) {
                    Text(
                        text = "See All",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { seeAll.invoke() },
                    )
                }
            }
        }
    ),

    ChildLayout(
        contentType = "All_News",
        items = allNews,
        content = { item ->
            Shimmer(isLoading = loading.value)
            AnimatedVisibility(visible = !loading.value) {
                LoadItemAfterSafeCast<News>(item) {
                    LandscapeItemNews(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        item = it,
                        onClickItem = { onClickItem.invoke(it) }
                    )
                }
            }
        }
    ))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlineItem(
    modifier: Modifier,
    item: News,
    onClickItem: () -> Unit
) {
    Card(
        onClick = { onClickItem.invoke() },
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(5.dp))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
        ) {
            val (img, tvTitle) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clipToBounds(),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = item.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.newspaper)
                        }).build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "some_description",
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(tvTitle) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f))
                    .clipToBounds(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    maxLines = 2,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitItem(
    modifier: Modifier,
    item: News,
    onLoading: Boolean,
    onClickItem: () -> Unit,
) {
    Card(
        onClick = { onClickItem.invoke() },
        modifier = modifier
            .placeholder(visible = onLoading, highlight = PlaceholderHighlight.shimmer())
            .wrapContentSize()
            .clip(RoundedCornerShape(5.dp))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            val (img, tvTitle) = createRefs()
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.3f)
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clipToBounds(),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = item.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.newspaper)
                        }).build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "some_description",
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(tvTitle) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f))
                    .clipToBounds(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = item.title,
                    fontSize = 11.sp,
                    maxLines = 1,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeItemNews(
    modifier: Modifier,
    item: News,
    onClickItem: () -> Unit,
) {
    Card(
        onClick = { onClickItem.invoke() },
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(5.dp))
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = item.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.newspaper)
                        }).build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "some_description",
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = item.title,
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
                Text(
                    text = item.publishedAt.simpleFormattedDate(),
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}

@Composable
private fun Shimmer(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    AnimatedVisibility(modifier = modifier, visible = isLoading) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 10.dp)
                .placeholder(isLoading, highlight = PlaceholderHighlight.shimmer()),
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MyPreview2() {
    val myList = listOf(
        News("uuid", "Title News 1", "Some Description of News 1"),
        News("uuid", "Title of News 2 in Very Long Text", "Some Description of News 2"),
        News("uuid", "Example of Title of News 3", "Some Description of News 3"),
        News("uuid", "Title of News 4", "Some Description of News 4")
    )
    Content(
        isLoading1 = true, isLoading2 = true, isLoading3 = true,
        isRefreshing = rememberSwipeRefreshState(isRefreshing = true),
        headlineNews = myList, topNews = myList, allNews = myList,
        onClickSeeAll = { },
        onClickItem = { },
        onRefresh = {}
    )
}