package com.nakyoung.androidclientdevelopment.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationBarView
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.databinding.ActivityMainBinding
import com.nakyoung.androidclientdevelopment.statics.MENU
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.profile.ProfileFragment
import com.nakyoung.androidclientdevelopment.ui.timeline.TimelineFragment
import com.nakyoung.androidclientdevelopment.ui.today.TodayFragment
import com.nakyoung.androidclientdevelopment.ui.today.TodayViewModel
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivityString = this.localClassName

        binding = ActivityMainBinding.inflate(layoutInflater)

        //bottomNavigationSetting
        navigationSetting(binding.navView)
        Logger("생성완료 ")

        setContentView(binding!!.root)
    }

    override fun navigationSetting(navigationBarView: NavigationBarView) {
        navigationBarView.selectedItemId = MENU.TODAY.id
        supportFragmentManager.beginTransaction().replace(binding.host.id,TodayFragment())
        supportFragmentManager.commit(allowStateLoss = false, body = supportFragmentManager.beginTransaction()->)

        navigationBarView.setOnItemSelectedListener { menuItem ->
            val fragmentManager = supportFragmentManager.beginTransaction()

            when(menuItem.itemId){
                MENU.TODAY.id ->
                    {
                        //let을 사용하여 한번 사용하고 말 확장함수 처리
                        /**
                         * todayFragment 변수를 생성하여
                         * **/
                        if(currentFragment !is TodayFragment)
                            fragmentManager.let { it->
                                val todayFragment = TodayFragment()
                                currentFragment = todayFragment
                                it.replace(binding.host.id, todayFragment)
                            }
                    }
                MENU.TIME_LINE.id ->
                    {
                        if(currentFragment !is TimelineFragment)
                            fragmentManager.let { it->
                                val timelineFragment = TimelineFragment()
                                currentFragment = timelineFragment
                                it.replace(binding.host.id, timelineFragment)
                            }
                    }
                MENU.PROFILE.id ->
                {
                    if(currentFragment !is ProfileFragment)
                        fragmentManager.let { it->
                            val profileFragment = ProfileFragment()
                            currentFragment = profileFragment
                            it.replace(binding.host.id, profileFragment)
                        }
                }
            }

            fragmentManager.commit()

            true
        }
    }
}