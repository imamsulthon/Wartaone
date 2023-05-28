package com.imams.wartaone.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imams.core.util.asStringWithComma
import com.imams.domain.model.News
import com.imams.wartaone.R
import com.imams.wartaone.databinding.ItemNewsPortraitBinding

class PortraitNewsAdapter(
    private var list: List<News>,
    private val callback: ((News) -> Unit)? = null,
): RecyclerView.Adapter<PortraitNewsItemVH>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submit(list: List<News>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortraitNewsItemVH {
        return PortraitNewsItemVH(
            ItemNewsPortraitBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_news_portrait, parent, false
            )
        ))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PortraitNewsItemVH, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            callback?.invoke(list[position])
        }
    }

}

class PortraitNewsItemVH(private val binding: ItemNewsPortraitBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: News) {
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
            tvSubTitle.text = item.categories.asStringWithComma()
        }
    }

}

