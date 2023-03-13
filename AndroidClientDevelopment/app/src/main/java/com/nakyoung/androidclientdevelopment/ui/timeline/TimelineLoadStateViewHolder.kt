package com.nakyoung.androidclientdevelopment.ui.timeline

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.databinding.ItemTimelineLoadStateBinding

class TimelineLoadStateViewHolder(
    val binding: ItemTimelineLoadStateBinding,
    val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retry.setOnClickListener {
            retry()
        }
    }

    fun bind(loadstate: LoadState) {
        binding.progress.isVisible = loadstate is LoadState
        binding.retry.isVisible = loadstate is LoadState.Error
    }
}