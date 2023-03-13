package com.nakyoung.androidclientdevelopment.ui.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.databinding.ItemTimelineLoadStateBinding

class TimelineLoadStateAdapter(
    val retry:() -> Unit
): LoadStateAdapter<TimelineLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): TimelineLoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = ItemTimelineLoadStateBinding.inflate(layoutInflater, parent, false)

        return TimelineLoadStateViewHolder(viewHolder, retry)
    }

    override fun onBindViewHolder(holder: TimelineLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}