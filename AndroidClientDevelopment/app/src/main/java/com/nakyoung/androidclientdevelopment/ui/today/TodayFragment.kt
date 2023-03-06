package com.nakyoung.androidclientdevelopment.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment

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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}