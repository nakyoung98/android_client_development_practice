package com.nakyoung.androidclientdevelopment.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.api.response.QuestionAndAnswer
import com.nakyoung.androidclientdevelopment.databinding.ItemUserAnswerCardBinding

class UserAnswerAdapter(
    val context: Context
): PagingDataAdapter<QuestionAndAnswer, UserAnswerViewHolder>(QuestionResponseDiffCallback){

    object QuestionResponseDiffCallback : DiffUtil.ItemCallback<QuestionAndAnswer>() {
        override fun areContentsTheSame(
            oldItem: QuestionAndAnswer,
            newItem: QuestionAndAnswer
        ): Boolean = (oldItem == newItem)

        override fun areItemsTheSame(
            oldItem: QuestionAndAnswer,
            newItem: QuestionAndAnswer
        ): Boolean = (oldItem.question.id == newItem.question.id)
    }

    val inflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: UserAnswerViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswerViewHolder
    = UserAnswerViewHolder(ItemUserAnswerCardBinding.inflate(inflater,parent,false))

}