package com.nakyoung.androidclientdevelopment.ui.today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.nakyoung.androidclientdevelopment.api.ApiService
import com.nakyoung.androidclientdevelopment.api.response.HelloWorld
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.domain.FormatDateUseCase
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TodayFragment : BaseFragment(){
    private var binding: FragmentTodayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(inflater,container,false)
        return binding!!.root
    }


    /**Called immediately after onCreateView has returned,
     * but before any saved state has been restored in to the view.**/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {

            val api = ApiService.create(requireContext())

            val qidDateFormat = FormatDateUseCase().formatter()
            val qid = qidDateFormat.format(Date())
            val question = api.getQuestion(qid)

            Log.i("TODAY",question.text)
            binding?.date?.text = qidDateFormat.format(question.id)
            binding?.questionTextview?.text = question.text

        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}