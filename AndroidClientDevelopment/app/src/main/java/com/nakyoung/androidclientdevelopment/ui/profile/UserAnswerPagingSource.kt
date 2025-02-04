package com.nakyoung.androidclientdevelopment.ui.profile

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nakyoung.androidclientdevelopment.api.response.QuestionAndAnswer
import com.nakyoung.androidclientdevelopment.api.service.ApiService
import java.time.LocalDate

class UserAnswerPagingSource(
    val api: ApiService,
    val uid: String
) : PagingSource<LocalDate, QuestionAndAnswer>() {

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, QuestionAndAnswer> {
        val userAnswerResponse = api.getUserAnswers(uid, params.key)

        return if (userAnswerResponse.isSuccessful) {
            val userAnswers = userAnswerResponse.body()!!

            val nextKey = if (userAnswers.isNotEmpty()) {
                userAnswers.minOf { it.question.id }
            } else {
                null
            }

            LoadResult.Page(
                data = userAnswers,
                prevKey = null,
                nextKey = nextKey
            )
        } else {
            LoadResult.Error(Throwable("Paging Error"))
        }
    }

    override fun getRefreshKey(state: PagingState<LocalDate, QuestionAndAnswer>): LocalDate? = null
}