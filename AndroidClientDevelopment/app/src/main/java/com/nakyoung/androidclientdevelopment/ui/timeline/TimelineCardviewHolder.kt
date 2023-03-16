package com.nakyoung.androidclientdevelopment.ui.timeline

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.databinding.ItemTimelineCardBinding
import com.nakyoung.androidclientdevelopment.ui.timeline.detail.DetailActivity
import java.time.format.DateTimeFormatter

class TimelineCardviewHolder(val binding: ItemTimelineCardBinding)
    : RecyclerView.ViewHolder(binding.root) {
        companion object{
            val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy. M. d")
        }

    fun bind(question: Question) {
        binding.date.text = DATE_FORMATTER.format(question.id)
        binding.question.text = question.text?: ""

        binding.answerCount.text =
            if (question.answerCount > 0) {
                binding.root.context.getString(R.string.answer_count_format, question.answerCount)
            }else{
                binding.root.context.getString(R.string.no_answer_yet)
            }

        binding.card.setOnClickListener{
            val context = binding.root.context

            context.startActivity(
                Intent(
                context,DetailActivity::class.java
            ).apply{
                putExtra(DetailActivity.EXTRA_QID, question.id)
                })
        }
    }
}