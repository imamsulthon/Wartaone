package com.imams.wartaone.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imams.domain.model.News
import com.imams.wartaone.R
import com.imams.wartaone.databinding.ItemNewsLandscapeBinding

class PagingNewsAdapter(
    private val callback: ((News) -> Unit)?
): PagingDataAdapter<News, LandscapeNewsVH>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem.uuid == newItem.uuid

        }
    }

    override fun onBindViewHolder(holder: LandscapeNewsVH, position: Int) {
        getItem(position)?.let { news ->
            holder.bind(news)
            holder.itemView.setOnClickListener {
                callback?.invoke(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandscapeNewsVH {
        return LandscapeNewsVH(ItemNewsLandscapeBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news_landscape, parent, false
            )
        ))
    }

}

class LandscapeNewsVH(
    private val binding: ItemNewsLandscapeBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: News) {
        with(binding) {
            try {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(viewLeft)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            tvTitle.text = item.title
            tvSubTitle.text = item.publishedAt
            tvDesc.text = item.description
        }
    }
}
