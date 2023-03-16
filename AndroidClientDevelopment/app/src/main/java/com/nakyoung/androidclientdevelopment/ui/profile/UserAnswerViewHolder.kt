package com.nakyoung.androidclientdevelopment.ui.profile

import android.content.Intent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.QuestionAndAnswer
import com.nakyoung.androidclientdevelopment.databinding.ActivityProfileBinding
import com.nakyoung.androidclientdevelopment.databinding.ItemUserAnswerCardBinding
import com.nakyoung.androidclientdevelopment.ui.timeline.detail.DetailActivity
import com.nakyoung.androidclientdevelopment.ui.today.ImageViewerActivity
import java.time.format.DateTimeFormatter

class UserAnswerViewHolder(
    val binding: ItemUserAnswerCardBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy. M. d.")
    }

    fun bind(item: QuestionAndAnswer) {
        val question = item.question
        val answer = item.answer

        binding.date.text = DATE_FORMATTER.format(question.id)

        binding.question.text = question.text
        binding.textAnswer.text = answer.text
        answer.photo?.let {
            binding.photoAnswer.load(it){
                placeholder(R.drawable.ph_image)
                error(R.drawable.ph_image)
            }
        }
        binding.textAnswer.isVisible = !answer.text.isNullOrEmpty()
        binding.photoAnswer.isVisible = !answer.photo.isNullOrEmpty()

        binding.photoAnswer.setOnClickListener {
            val context = itemView.context
            context.startActivity(
                Intent(context,
                    ImageViewerActivity::class.java).apply {
                        putExtra(ImageViewerActivity.EXTRA_URL, answer.photo)
                })
        }

        binding.root.setOnClickListener{
            val context = itemView.context
            context.startActivity(
                Intent(context,
            DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_QID,question.id)
                })
        }
    }
}