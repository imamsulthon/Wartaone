package com.imams.wartaone.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.imams.core.base.TheResult
import com.imams.core.util.gone
import com.imams.core.util.visible
import com.imams.domain.model.News
import com.imams.wartaone.databinding.ActivityHomeBinding
import com.imams.wartaone.detail.DetailActivity
import com.imams.wartaone.home.adapter.HeadlineAdapter
import com.imams.wartaone.home.adapter.PagingNewsAdapter
import com.imams.wartaone.home.adapter.PortraitNewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator2

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()

    private val headlineAdapter: HeadlineAdapter by lazy {
        HeadlineAdapter(
            list = listOf(),
            callback = ::openDetail
        )
    }

    private val topNewsAdapter: PortraitNewsAdapter by lazy {
        PortraitNewsAdapter(
            listOf(),
            callback = ::openDetail
        )
    }

    private val allNewsAdapter: PagingNewsAdapter by lazy {
        PagingNewsAdapter(
            callback = ::openDetail
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewAndListener()
        observeViewModel()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchData()
    }

    private fun onRefresh() {
        lifecycleScope.launch {
            allNewsAdapter.refresh()
            fetchData()
            delay(1000)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeViewModel() {

        viewModel.headlineNews.observe(this) {
            it?.let {
                setHeadlineNews(it)
            }
        }
        viewModel.topNews.observe(this) {
            when (it) {
                is TheResult.Success -> { setTopNews(it.data) }
                is TheResult.Error -> {
                    showError(it.throwable.message ?: "error", 2)
                }
            }
        }

        viewModel.allNewsPaging.observe(this) {
            it?.let {
                setAllNews(it)
            }
        }
    }

    private fun initViewAndListener() {
        with(binding) {

            showLoading(true, 1)
            showLoading(true, 2)
            showLoading(true, 3)

            swipeRefresh.setOnRefreshListener {
                onRefresh()
            }

            rvHeadlineNews.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            rvHeadlineNews.adapter = headlineAdapter
            rvHeadlineNews.addIndicator(dotIndicator)

            tvGroupTopNewsSeeAll.setOnClickListener { seeAll("top") }
            tvGroupAllNewsSeeAll.setOnClickListener { seeAll("all") }

            rvTopNews.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            rvTopNews.adapter = topNewsAdapter

            rvAllNews.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.VERTICAL, false
            )
            rvAllNews.adapter = allNewsAdapter

        }
    }

    private fun setHeadlineNews(list: List<News>) {
        showLoading(false, 1)
        headlineAdapter.submit(list)
    }

    private fun setTopNews(list: List<News>) {
        showLoading(false, 2)
        topNewsAdapter.submit(list)
    }

    private fun setAllNews(list: PagingData<News>) {
        showLoading(false, 3)
        allNewsAdapter.submitData(lifecycle, list)
    }

    private fun openDetail(item: News) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.ID, item.uuid)
        })
    }

    private fun RecyclerView.addIndicator(indicator2: CircleIndicator2) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
        indicator2.attachToRecyclerView(this, snapHelper)
        this.adapter?.registerAdapterDataObserver(indicator2.adapterDataObserver)
    }

    private fun showError(msg: String, section: Int) {
        with(binding) {
            when (section) {
                2 -> {
                    rvTopNews.gone()
                    loading2.root.visible()
                    loading2.tvLoading.text = msg
                }
            }
        }
    }

    private fun showLoading(show: Boolean, section: Int) {
        with(binding) {
            when (section) {
                1 -> {
                    if (show) {
                        rvHeadlineNews.gone()
                        dotIndicator.gone()
                        loading1.root.visible()
                    } else {
                        rvHeadlineNews.visible()
                        dotIndicator.visible()
                        loading1.root.gone()
                    }
                }
                2 -> {
                    if (show) {
                        rvTopNews.gone()
                        loading2.root.visible()
                    } else {
                        rvTopNews.visible()
                        loading2.root.gone()
                    }
                }
                3 -> {
                    if (show) {
                        rvAllNews.gone()
                        loading3.root.visible()
                    } else {
                        rvAllNews.visible()
                        loading3.root.gone()
                    }
                }
            }
        }
    }

    private fun seeAll(tag: String) {
        startActivity(Intent(this, NewsListActivity::class.java).apply {
            putExtra(NewsListActivity.TITLE, tag)
        })
    }

}