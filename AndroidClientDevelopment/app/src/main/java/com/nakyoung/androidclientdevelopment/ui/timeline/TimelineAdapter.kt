package com.nakyoung.androidclientdevelopment.ui.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.databinding.ItemTimelineCardBinding

class TimelineAdapter(context: Context)
    : PagingDataAdapter<Question, TimelineCardviewHolder>(QuestionComparator){

    object QuestionComparator : DiffUtil.ItemCallback<Question>() {
        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }

    val inflater= LayoutInflater.from(context)

    override fun onBindViewHolder(holder: TimelineCardviewHolder, position: Int) {
        getItem(position)?.let { question ->
            holder.bind(question)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineCardviewHolder {
        return TimelineCardviewHolder(ItemTimelineCardBinding.inflate(inflater, parent, false))
    }

}