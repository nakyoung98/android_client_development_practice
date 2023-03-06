package com.nakyoung.androidclientdevelopment.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.nakyoung.androidclientdevelopment.api.response.HelloWorld
import com.nakyoung.androidclientdevelopment.databinding.FragmentTodayBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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


        /**
         * 메인(UI)스레드에서 API호출처럼 네트워크로 인해 시간이 오래 소요되는 작업을 하는 경우, 앱이 멈췄다고 느낄 수 있음
         * 따라서 Thread(백그라운드 스레드) 에서만 네트워크 작업이 가능함. 안드로이드에서 강제함
         * **/
        Thread{
            val url = URL("http://10.0.2.2:5000/v1/hello-world")

            //HttpURLConnection은 생성자를 직접 호출할 수 없음
            //URL클래스의 생성자에 서버 주소, 포트, 경로를 입력한 후 openConnection을 호출하면 그떄 URLConnection의 객체를 념겨받음
            //여기서는 HTTP통신을 하기 떄문에 HttpURLConnection 객체를 넘겨받았음
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "GET"

            //요청에 헤더 추가 (accept헤더)
            conn.setRequestProperty("Accept","application/json")
            conn.connect()

            //해당 api는 get호출 후 단순 문자열을 받아오기 떄문에
            //읽은 값을 그대로 표시
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val body =reader.readText()
            reader.close()
            conn.disconnect()

//            //응답으로 받은 body를 JSON으로 넘김
//            //get...(키) 으로 JSON값 가져옴
//            val json= JSONObject(body)
//            val date = json.getString("date")
//            val message = json.getString("message")

            val gson = Gson()
            val helloworld = gson.fromJson(body,HelloWorld::class.java)

            //UI작업은 백그라운드에서 할 수 없음
            //이에 따라, runOnUiThread를 이용해 결과를 메인스레드에서 화면에 표시하게 함
            activity?.runOnUiThread {
                binding!!.date.text = helloworld.date
                binding!!.questionTextview.text = helloworld.message
            }
        }.start()

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}