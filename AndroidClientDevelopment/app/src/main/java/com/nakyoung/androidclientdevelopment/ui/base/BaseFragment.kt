package com.nakyoung.androidclientdevelopment.ui.base

import androidx.fragment.app.Fragment
import com.nakyoung.androidclientdevelopment.api.service.ApiService

abstract  class BaseFragment : Fragment() {
    val api: ApiService by lazy { ApiService.getInstance() }

}