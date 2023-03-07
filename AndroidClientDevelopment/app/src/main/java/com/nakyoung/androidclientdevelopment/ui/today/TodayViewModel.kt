package com.nakyoung.androidclientdevelopment.ui.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakyoung.androidclientdevelopment.api.ApiService
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.domain.FormatDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*


data class TodayUiState(
    val question: Question? = null,
//    val answer: Answer? = null,
    val isAnswered: Boolean = false
)



class TodayViewModel: ViewModel() {
    private val api = ApiService.create()

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update {currentState->
                currentState.copy(
                    question = api.getQuestion("2022-09-05")
                )
            }
        }
        Log.i("TodayViewModel","초기화 완료")
    }

    fun getQuestion(): Question? {
        Log.i("TodayViewModel","getQuestion()")
        return uiState.value.question
    }
}