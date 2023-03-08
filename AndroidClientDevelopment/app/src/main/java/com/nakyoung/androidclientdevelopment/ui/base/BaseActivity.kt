package com.nakyoung.androidclientdevelopment.ui.base

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationBarView
import com.nakyoung.androidclientdevelopment.api.ApiService
import com.nakyoung.androidclientdevelopment.databinding.ActivityMainBinding

abstract class BaseActivity : AppCompatActivity() {
    protected var thisActivityString: String?= null

    val api: ApiService by lazy { ApiService.getInstance() }

    inner class Logger(vararg log: String){ //로그를 출력하는 내부클래스
        private val title: String? = thisActivityString

        init{
            Log.i(title!!, log.joinToString(separator = ",", prefix = "[", postfix = "]"))
        }
    }

    protected abstract fun navigationSetting(navigationBarView: NavigationBarView)

    /**
     * 옵션 메뉴에서 항목을 선택할 때마다 호출됩니다. 기본 구현은 단순히 false를 반환하여 정상적인 처리가 이루어지도록 합니다
     * (항목의 Runnable을 호출하거나 적절하게 Handler에 메시지를 보냄).
     * 다른 기능 없이 처리하려는 모든 항목에 이 방법을 사용할 수 있습니다.
     * 파생 클래스는 기본 메뉴 처리를 수행하기 위해 기본 클래스를 호출해야 합니다.
     **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home ->{ //뒤로가기
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}