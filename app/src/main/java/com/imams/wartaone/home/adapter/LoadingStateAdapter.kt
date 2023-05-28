package com.imams.wartaone.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imams.wartaone.R
import com.imams.wartaone.databinding.LoadingBinding

class LoadingStateAdapter: LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    class ViewHolder(private val binding: LoadingBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Loading -> {}
                is LoadState.Error -> {}
                is LoadState.NotLoading -> {}
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            LoadingBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading, parent, false)
        ))
    }

}