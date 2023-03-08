package com.nakyoung.androidclientdevelopment.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.nakyoung.androidclientdevelopment.databinding.ActivitySplashBinding
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.login.LoginActivity
import com.nakyoung.androidclientdevelopment.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(1000)

            if (AuthManager.accessToken.isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }

            finish()
        }
    }
}