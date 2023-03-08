package com.nakyoung.androidclientdevelopment.ui.today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TodayFragment : BaseFragment(){
    private var binding: FragmentTodayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater,container,false)
        return binding!!.root
    }


    /**Called immediately after onCreateView has returned,
     * but before any saved state has been restored in to the view.**/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val question = api.getQuestion(LocalDate.now())
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy. M. d")
            Log.i("TODAY",question.text)
            binding!!.date.text = dateFormatter.format(question.id)
            binding!!.questionTextview.text = question.text
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}