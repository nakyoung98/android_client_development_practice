package com.nakyoung.androidclientdevelopment.ui.timeline.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.databinding.ActivityDetailsBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailActivity : BaseActivity() {
    companion object {
        const val EXTRA_QID = "qid"
    }

    lateinit var binding: ActivityDetailsBinding
    var adapter: AnswerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qid: LocalDate?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            qid = intent?.getSerializableExtra(EXTRA_QID, LocalDate::class.java)
        } else {
            qid = intent?.getSerializableExtra(EXTRA_QID) as LocalDate
        }

        supportActionBar?.title = DateTimeFormatter.ofPattern(getString(R.string.date_format)).format(qid!!)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = AnswerAdapter(this)
        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                val itemCount = adapter?.items?.size ?: 0
                binding.empty.isVisible = itemCount == 0
            }
        })

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        )

        lifecycleScope.launch {
            val questionResponse = api.getQuestion(qid)
            if (questionResponse.isSuccessful) {
                binding.question.text = questionResponse.body()?.text
            }

            val answersResponse = api.getAnswers(qid)
            if (answersResponse.isSuccessful) {
                adapter?.items = answersResponse.body()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}