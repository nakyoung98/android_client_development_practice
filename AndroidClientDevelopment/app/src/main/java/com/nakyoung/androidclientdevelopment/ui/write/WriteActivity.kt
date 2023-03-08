package com.nakyoung.androidclientdevelopment.ui.write

import android.media.MediaCodec.MetricsConstants.MODE
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.Answer
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.databinding.ActivityWriteBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WriteActivity: BaseActivity() {
    companion object{
        const val EXTRA_QID = "qid"
        const val EXTRA_MODE = "mode"
    }

    enum class Mode{
        WRITE, EDIT
    }

    private lateinit var binding: ActivityWriteBinding
    private lateinit var mode: Mode

    private lateinit var question: Question
    private var answer: Answer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * TIRAMISU 버전(API33) 이상에서는 getSerializableExtra(name)이 동작하지 않는다
         * **/
        val qid: LocalDate?
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            qid = intent.getSerializableExtra(EXTRA_QID, LocalDate::class.java)
            mode = intent?.getSerializableExtra(EXTRA_MODE, Mode::class.java)!!}
        else{
            qid = intent.getSerializableExtra(EXTRA_QID) as LocalDate
            mode= intent?.getSerializableExtra(EXTRA_MODE)!! as Mode
        }

        supportActionBar?.title = DateTimeFormatter.ofPattern(getString(R.string.date_format)).format(qid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch{
            question = qid?.let { api.getQuestion(it).body() }!!
            answer = api.getAnswer(qid).body()

            binding.question.text = question.text
            //editTextView에서는 text를 그냥 설정할 수 없음. setText 사용
            binding.answer.setText(answer?.text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.done -> {
                write()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun write(){
        val text = binding.answer.text.toString().trimEnd()

        lifecycleScope.launch{
            val answerResponse =
                if (answer == null){
                    api.writeAnswer(question.id, text)
                }else{
                    api.editAnswer(question.id, text)
                }

            if (answerResponse.isSuccessful){
                //RESULT_OK: Standard activity result, operation succeeded.
                setResult(RESULT_OK)
                finish()
            }else{
                Toast.makeText(
                    this@WriteActivity,
                    answerResponse.message(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}