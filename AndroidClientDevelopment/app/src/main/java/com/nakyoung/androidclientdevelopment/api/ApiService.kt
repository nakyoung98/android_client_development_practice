package com.nakyoung.androidclientdevelopment.api

import android.content.Context
import android.media.Image
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.nakyoung.androidclientdevelopment.api.ConverterFactory.LocalDateConverterFactory
import com.nakyoung.androidclientdevelopment.api.response.Answer
import com.nakyoung.androidclientdevelopment.api.response.Question
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import com.nakyoung.androidclientdevelopment.adapter.LocalDateAdapter as LocalDateAdapter

interface ApiService {

    companion object{

        private var INSTANCE: ApiService? = null

        /**
         * 1. API 요청과 응답에 대한 로그 표시
         * 2. 타임 아웃 설정
         * **/
        private fun okHttpClient(): OkHttpClient{
            val builder = OkHttpClient.Builder()
            val logging  = HttpLoggingInterceptor()

            /**
             * None: no log
             * Basic: 요청라인과 응답라인만
             * Headers: 요청라인, 요청헤더, 응답라인, 응답헤더
             * Body: 요청라인.헤더.본문, 응답라인.헤더.본문
             *
             * [Log에서]
             * --> : 요청
             * <-- : 응답
             *
             * => logging이 남기는 log는 OkHttpClient로 로그캣에서 필터링하여 볼 수 있음
             * **/
            logging.level = HttpLoggingInterceptor.Level.BODY

            return builder
                    //연결 타임아웃 3초
                .connectTimeout(3, TimeUnit.SECONDS)
                    //쓰기 타임아웃 10초
                .writeTimeout(10, TimeUnit.SECONDS)
                    //읽기 타임아웃 10초
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
        }

        private fun create(context: Context): ApiService{
            //옵션을 지정 후 Gson을 생성하기 위해
            //GsonBuilder 사용으로 옵션 지정 후 create하여 Gson객체를 얻음
            val gson = GsonBuilder()
                    //서버는 필드 이름에 snakeCase를 사용하고있으므로 이를 정해주면 Gson이 자동 변환함
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
                .create()

            return Retrofit.Builder()
                    //Retrofit이 Gson을 사용해 메서드읭 매개변수를 HTTP 요청 본문으로 변환하거나 HTTP응답 본문을 메서드의 반환 모델로 변환
                    //생성한객체를 전달해 ConverterFactory를 만든 후 Builder의 addConverterFactory로 전달
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(LocalDateConverterFactory())
                    //retrofit 객체에 api interface를 넘기면 HTTP메서드 어노테이션(@GET같은)에 있는 경로가 baseUrl과 결합해 URI 결정
                .baseUrl("http://10.0.2.2:5000")
                    //로그 연결
                .client(okHttpClient())
                .build()
                .create(ApiService::class.java)
        }

        /**
         * 인스턴스를 생성할 때 초기에 여러 스레드에서 접근하더라도 하나의 인스턴스만 생성되도록 할 때 사용된다.
         * **/
        fun init(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: create(context).also {
                INSTANCE = it
            }
        }

        fun getInstance(): ApiService = INSTANCE!!
    }

    /**
     * @GET 어노테이션
     * HTTP는 GET을 사용하는 것을 알리고 API의 경로를 지정함
     * @POST, @PUT, @PATCH, @DELETE등도 똑같이 사용하면 됨
     *
     * param {qid} : 메서드를 호출할 때 채워질 위치
     * =====================================================
     * @Path 어노테이션
     * 해당 매개변수가 경로에서 사용됨을 의미
     * @Header: 요청의 헤더에 값 추가
     * @Query: 쿼리에 값 추가
     * @Field: 필드로 추가
     * @Part: 파트로 추가
     * @Body: 요청 본문으로 사용
     * =====================================================
     * return Question
     * 요청 응답 구조
     * =====================================================
     * suspend 제어자
     * 코루틴에서 사용하기 위해 중단함수로 선언
     * **/
    @GET("/v1/questions/{qid}")
    suspend fun getQuestion(@Path("qid") qid: LocalDate): Response<Question>


    @FormUrlEncoded
    @POST("/v1/questions/{qid}/answers")
    suspend fun writeAnswer(
        @Path("qid") qid: String,
        @Path("text") text: String? = null,
        @Path("photo") photo: String? = null
    ): Response<Answer>

    /**
     * @Body 사용시
     *
     * @POST("/v1/questions/{qid}/answers")
     * suspend fun writeAnswer(
     *      @Path("qid") qid: String,
     *      @Body("params"): WriteParams
     * ): Response<Answer>
     *
     * //WriteParams 객체를 만들어 writeAnswer()메서드의 매개변수로 사용
     * //HTTP메시지에서 Content-Type이 application/json이 되고, WriteParams가 본문의 JSON이 됨
     * **/

    @Multipart
    @POST("/images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<Image>

}