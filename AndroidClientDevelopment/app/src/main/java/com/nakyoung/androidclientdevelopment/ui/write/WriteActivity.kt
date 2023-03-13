package com.nakyoung.androidclientdevelopment.ui.write

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.extension.asRequestBody
import com.nakyoung.androidclientdevelopment.api.response.Answer
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.databinding.ActivityWriteBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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
    private var imageUrl: String? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    val imageUri = result.data?.data ?: return@launch
                    val requestBody = imageUri.asRequestBody(contentResolver)

                    val part = MultipartBody.Part.createFormData("image", "filename", requestBody)
                    val imageResponse = api.uploadImage(part)

                    if (imageResponse.isSuccessful) {
                        imageUrl = imageResponse.body()!!.url

                        binding.photo.load(imageUrl){
                            transformations(RoundedCornersTransformation(resources.getDimension(R.dimen.thumbnail_rounded_corner)))
                        }

                        binding.photoArea.isVisible = true
                    }
                }
            }
        }

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

            imageUrl = answer?.photo
            binding.photoArea.isVisible= !imageUrl.isNullOrEmpty()

            imageUrl?.let {
                binding.photo.load(it){
                    transformations(RoundedCornersTransformation(resources.getDimension(R.dimen.thumbnail_rounded_corner)))
                }
            }
        }

        binding.photoArea.setOnClickListener{
            showDeleteCofirmDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                write()
                return true
            }

            R.id.add_photo -> {
                startForResult.launch(
                    Intent(Intent.ACTION_GET_CONTENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                    })
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
                    api.writeAnswer(question.id, text, imageUrl)
                }else{
                    api.editAnswer(question.id, text, imageUrl)
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

    fun showDeleteCofirmDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.dialog_msg_are_you_sure_to_delete)
            .setPositiveButton(R.string.ok) { dialog, which ->
                binding.photo.setImageResource(0)
                binding.photoArea.isVisible = false
                imageUrl = null
            }
            .setNegativeButton(R.string.cancel){ dialog, which -> }
            .show()
    }
}