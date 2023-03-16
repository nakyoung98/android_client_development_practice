package com.nakyoung.androidclientdevelopment.ui.timeline.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.api.response.Answer
import com.nakyoung.androidclientdevelopment.databinding.ItemAnswerBinding

class AnswerAdapter(val context: Context) : RecyclerView.Adapter<AnswerViewHolder>() {

    val inflater = LayoutInflater.from(context)
    var items: List<Answer>? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(ItemAnswerBinding.inflate(inflater,parent,false))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(items!![position])
    }

}