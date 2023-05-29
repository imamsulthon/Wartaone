package com.imams.wartaone.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imams.domain.model.News
import com.imams.wartaone.databinding.ActivityAllNewsBinding
import com.imams.wartaone.detail.DetailActivity
import com.imams.wartaone.home.adapter.LoadingStateAdapter
import com.imams.wartaone.home.adapter.PagingNewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsListActivity: AppCompatActivity() {

    private val binding by lazy { ActivityAllNewsBinding.inflate(layoutInflater) }
    private val viewModel: AllNewsListViewModel by viewModels()

    private val allNewsAdapter: PagingNewsAdapter by lazy {
        PagingNewsAdapter(
            callback = ::openDetail
        )
    }

    private var tag = ""

    companion object {
        const val TITLE = "group_title"
        const val TOP_NEWS = "top"
        const val ALL_NEWS = "all"
    }

    private fun initIntent() {
        intent?.getStringExtra(TITLE)?.let {
            tag = it
            binding.tvPageTitle.text = when (tag) {
                TOP_NEWS -> "Top News"
                ALL_NEWS -> "All News"
                else -> "Undefined"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initIntent()
        initViewAndListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {

            viewModel.fetchData(tag).collectLatest {
                setAllNews(it)
            }

            allNewsAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }

            allNewsAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvAllNews.scrollToPosition(0) }
        }
    }

    private fun initViewAndListener() {
        with(binding) {
            swipeRefresh.setOnRefreshListener { allNewsAdapter.refresh() }

            rvAllNews.layoutManager = LinearLayoutManager(this@NewsListActivity,
                LinearLayoutManager.VERTICAL, false)
            rvAllNews.adapter = allNewsAdapter.withLoadStateFooter(LoadingStateAdapter())

        }
    }

    private fun setAllNews(list: PagingData<News>) {
        allNewsAdapter.submitData(lifecycle, list)
    }

    private fun openDetail(item: News) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.ID, item.uuid)
        })
    }

}