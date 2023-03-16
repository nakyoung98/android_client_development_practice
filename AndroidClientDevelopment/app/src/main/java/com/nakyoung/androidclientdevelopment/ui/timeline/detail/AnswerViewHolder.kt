package com.nakyoung.androidclientdevelopment.ui.timeline.detail

import android.content.Intent
import android.text.format.DateUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.Answer
import com.nakyoung.androidclientdevelopment.databinding.ItemAnswerBinding
import com.nakyoung.androidclientdevelopment.ui.profile.ProfileActivity
import com.nakyoung.androidclientdevelopment.ui.today.ImageViewerActivity

class AnswerViewHolder(
    val binding: ItemAnswerBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(answer: Answer) {
        binding.userName.text = answer.answerer?.name

        //유저 이미지가 있을 경우 load
        if (!answer.answerer?.photo.isNullOrBlank()) {
            binding.userPhoto.load(answer.answerer?.photo){
                placeholder(R.drawable.ph_user)
                error(R.drawable.ph_user)
                transformations(CircleCropTransformation())
            }
        }

        binding.textAnswer.text = answer.text
        binding.textAnswer.isVisible = !answer.text.isNullOrEmpty()

        binding.photoAnswer.load(answer.photo){
            placeholder(R.drawable.ph_image)
            error(R.drawable.ph_image)
        }
        binding.photoAnswer.isVisible= !answer.photo.isNullOrEmpty()
        binding.photoAnswer.setOnClickListener {
            val context = itemView.context
            context.startActivity(Intent(context,
            ImageViewerActivity::class.java).apply {
                putExtra(ImageViewerActivity.EXTRA_URL, answer.photo)
            })
        }

        binding.userPhoto.setOnClickListener {
            val context = itemView.context
            context.startActivity(Intent(context,
            ProfileActivity::class.java).apply {
                putExtra(ProfileActivity.EXTRA_UID, answer.answerer?.id)
            })
        }

        binding.elapsedTime.text = DateUtils.getRelativeTimeSpanString(answer.createdAt.time)
    }
}