package com.nakyoung.androidclientdevelopment.ui.today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.google.gson.Gson
import com.nakyoung.androidclientdevelopment.api.ApiService
import com.nakyoung.androidclientdevelopment.api.response.HelloWorld
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.domain.FormatDateUseCase
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TodayFragment : BaseFragment(){
    private var binding: FragmentTodayBinding? = null
    private val viewModel: TodayViewModel by activityViewModels()

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
        var i = 1

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.uiState.collect(){
                binding!!.date.text = viewModel.getQuestion()?.id
                binding!!.questionTextview.text = viewModel.getQuestion()?.text+i++
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        Log.i("TodayFragment","뷰 파괴")
        super.onDestroyView()
    }
}