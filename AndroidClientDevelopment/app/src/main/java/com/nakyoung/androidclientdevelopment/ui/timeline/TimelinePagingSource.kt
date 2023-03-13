package com.nakyoung.androidclientdevelopment.ui.timeline

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nakyoung.androidclientdevelopment.api.response.Question
import com.nakyoung.androidclientdevelopment.api.service.ApiService
import java.time.LocalDate

class TimelinePagingSource(val api: ApiService) : PagingSource<LocalDate, Question>() {

    override suspend fun load(params: LoadParams<LocalDate>): LoadResult<LocalDate, Question> {
        val fromData: LocalDate = params.key?: LocalDate.now()
        val questionsResponse = api.getQuestions(fromData, params.loadSize)

        if (questionsResponse.isSuccessful) {
            val questions:List<Question> = questionsResponse.body()!!

            if (questions.isNotEmpty()) {
                val oldest = questions.minOf { it.id }
                val nextKey = oldest.minusDays(1)

                return LoadResult.Page(
                    data = questions,
                    prevKey = null,
                    nextKey = nextKey
                )
            }

            return LoadResult.Page(
                data = questions,
                prevKey = null,
                nextKey = null
            )
        }

        return LoadResult.Error(
            Throwable("Paging Error")
        )
    }

    override fun getRefreshKey(state: PagingState<LocalDate, Question>): LocalDate? = null

}