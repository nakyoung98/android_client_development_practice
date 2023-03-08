package com.nakyoung.androidclientdevelopment.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.databinding.ActivityLoginBinding
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity: BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**소프트 키로 비밀번호 입력 끝났을 때 바로 로그인 시도 함**/
        binding.userPassword.setOnEditorActionListener{ _, actionId, event->
            when (actionId){
                EditorInfo.IME_ACTION_DONE -> {
                        login()
                        return@setOnEditorActionListener true
                    }

                EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    if (event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER) {

                        login()
                        return@setOnEditorActionListener true
                    }
                }
            }
            false
        }

        /**로그인 버튼 누르면 로그인 시도**/
        binding.login.setOnClickListener {
            login()
        }
    }

    /**
     * 로그인 validation 테스트
     * **/

    //TODO 이러한 유효성 검증은 Activity에서 이루어지는가? 개인적으로 크게 복잡한 업무가 아니니 괜찮을 것도 같지만..
    fun validateUidAndPassword(uid: String, password: String): Boolean {
        binding.userIdLayout.error = null
        binding.userPasswordLayout.error = null

        if (uid.length < 5) {
            binding.userIdLayout.error = getString(R.string.error_uid_too_short)
            return false
        }

        if (password.length < 8) {
            binding.userPasswordLayout.error = getString(R.string.error_password_too_short)
            return false
        }

        val numberRegex = "[0-9]".toRegex()

        if (!numberRegex.containsMatchIn(password)) {
            binding.userPasswordLayout.error = getString(R.string.error_password_must_contain_number)
            return false
        }

        /**
         * 비밀번호 정규식
         * 이런 것들은 LoginUtility를 만들어서 넣어도 될 것같음
         * **/
//        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()]).{8,20}$".toRegex()
//        if(!passwordRegex.containsMatchIn(password)){
//            binding.userPasswordLayout.error = getString(R.string.error_password_must_contain_number) //error문구 바꿔야됨
//            return false
//        }

        return true
    }

    fun login() {
        //로그인 progress 진행중임을 확인
        //중복적으로 키를 누르는 것을 방지
        if (binding.progress.isVisible) {
            return
        }

        //trim(): Returns a sub sequence of this char sequence having leading and trailing whitespace removed
        val uid = binding.userId.text?.trim().toString()
        val password = binding.userPassword.text?.trim().toString()

        if (validateUidAndPassword(uid, password)) {
            binding.progress.isVisible = true

            lifecycleScope.launch{
                val authTokenResponse = api.login(uid, password)

                if (authTokenResponse.isSuccessful) { //Token 확인 성공시
                    val authToken = authTokenResponse.body()

                    AuthManager.uid = uid
                    AuthManager.accessToken = authToken?.accessToken
                    AuthManager.refreshToken = authToken?.refreshToken

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }else{
                    //Token 확인 실패시
                    binding.progress.isVisible = false
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.error_login_failed,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}