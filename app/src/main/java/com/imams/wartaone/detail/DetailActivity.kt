package com.imams.wartaone.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imams.core.util.asStringWithComma
import com.imams.core.util.simpleFormattedDate
import com.imams.domain.model.News
import com.imams.wartaone.R
import com.imams.wartaone.databinding.ActivityDetailNewsBinding
import com.imams.wartaone.home.adapter.PortraitNewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity: AppCompatActivity() {

    private val binding by lazy { ActivityDetailNewsBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by viewModels()

    private val similarNewsAdapter: PortraitNewsAdapter by lazy { PortraitNewsAdapter(
        listOf(),
        callback = ::openDetail
    ) }
    private var uuid = "undefined"

    companion object {
        const val ID = "news_uuid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initViewAndListener()
        observeViewModel()
        fetchData()
    }

    override fun onStart() {
        super.onStart()
        intent?.getStringExtra(ID)?.let {
            uuid = it
            fetchData()
        }
    }

    private fun fetchData() {
        viewModel.fetchData(uuid)
        viewModel.getSimilar(uuid)
    }

    private fun observeViewModel() {
        viewModel.data.observe(this) {
            it?.let {
                setData(it)
            }
        }
        viewModel.similarNews.observe(this) {
            it?.let {
                setSimilarNews(it)
            }
        }
    }

    private fun initViewAndListener() {
        with(binding) {
            rvHeadlineNews.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setData(item: News) {
        with(binding) {
            try {
                Glide.with(binding.root)
                    .load(item.imageUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivBanner)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            tvTitle.text = item.title
            tvSubTitle.text = item.snippet
            tvSource.text = item.source
            tvDate.text = item.publishedAt.simpleFormattedDate()
            tvCategories.text = item.categories.asStringWithComma()

            loadUrl(item.url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrl(newsUrl: String) {
        with(binding) {
            webview.settings.javaScriptEnabled = true
            webview.loadUrl(newsUrl)
        }
    }

    private fun setSimilarNews(list: List<News>) {
        similarNewsAdapter.submit(list)
    }

    private fun openDetail(item: News) {
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(ID, item.uuid)
        })
    }

}