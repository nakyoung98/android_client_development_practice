package com.nakyoung.androidclientdevelopment.ui.today

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission.Write
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import com.nakyoung.androidclientdevelopment.ui.write.WriteActivity
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : BaseFragment(){
    private var _binding: FragmentTodayBinding? = null
    val binding get() = _binding!!

    var question: Question? = null

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch{
                setupAnswer()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater,container,false)
        return binding.root
    }


    /**Called immediately after onCreateView has returned,
     * but before any saved state has been restored in to the view.**/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.writeButton.setOnClickListener{
            startForResult.launch(Intent(requireContext(), WriteActivity::class.java)
                .apply {
                    putExtra(WriteActivity.EXTRA_QID, question!!.id)
                    //작성 모드 설정
                    putExtra(WriteActivity.EXTRA_MODE, WriteActivity.Mode.WRITE)
                })
        }

        binding.editButton.setOnClickListener{
            startForResult.launch(Intent(requireContext(), WriteActivity::class.java)
                .apply {
                    putExtra(WriteActivity.EXTRA_QID, question!!.id)
                    putExtra(WriteActivity.EXTRA_MODE, WriteActivity.Mode.EDIT)
                })
        }

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmDialog()

        }

        viewLifecycleOwner.lifecycleScope.launch {
            val questionResponse = api.getQuestion(LocalDate.now())
            if (questionResponse.isSuccessful){
                question = questionResponse.body()!!

                val dateFormatter = DateTimeFormatter.ofPattern("yyyy. M. d")

                binding.date.text = dateFormatter.format(question!!.id)
                binding.questionTextview.text = question!!.text

                val answer = api.getAnswer(question!!.id).body()
                //answer이 있으면 answer을 visible 하게 해라
                binding.answerArea.isVisible = (answer != null)
                binding.textAnswer.text = answer?.text
                //answer이 없으면 answer을 작성하는 버튼을 띄워라
                binding.writeButton.isVisible = (answer == null)
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * 삭제 버튼을 누를 시 화면에 발생하는 Dialog창
     * OK: 삭제
     * NO: 취소
     * **/
    private fun showDeleteConfirmDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_msg_are_you_sure_to_delete)
            .setPositiveButton(R.string.ok){ dialog, which ->
                lifecycleScope.launch{
                    val deleteResponse = api.deleteAnswer(question!!.id)
                    if (deleteResponse.isSuccessful) {
                        binding.answerArea.isVisible = false
                        binding.writeButton.isVisible = true
                    }
                }
            }
            .setNegativeButton(R.string.cancel){ dialog, which -> }
                //dialog구성해서 보이기
            .show()
    }

    private suspend fun setupAnswer() {
        val question = question ?: return

        val answer = api.getAnswer(question.id).body()

        binding.answerArea.isVisible = (answer != null)
        binding.textAnswer.text = answer?.text

        binding.writeButton.isVisible = (answer == null)
    }
}