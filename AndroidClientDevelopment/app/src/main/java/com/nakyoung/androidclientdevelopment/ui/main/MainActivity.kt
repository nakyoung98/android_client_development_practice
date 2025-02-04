package com.nakyoung.androidclientdevelopment.ui.main

import android.os.Bundle
import com.google.android.material.navigation.NavigationBarView
import com.nakyoung.androidclientdevelopment.databinding.ActivityMainBinding
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.statics.MENU
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity
import com.nakyoung.androidclientdevelopment.ui.profile.ProfileFragment
import com.nakyoung.androidclientdevelopment.ui.timeline.TimelineFragment
import com.nakyoung.androidclientdevelopment.ui.today.TodayFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivityString = this.localClassName

        binding = ActivityMainBinding.inflate(layoutInflater)

        //bottomNavigationSetting
        navigationSetting(binding.navView)
        Logger("생성완료 ")

        setContentView(binding.root)
    }

    private fun navigationSetting(navigationBarView: NavigationBarView) {
        navigationBarView.setOnItemSelectedListener {
            val fragmentManager = supportFragmentManager.beginTransaction()

            when (it.itemId) {
                MENU.TODAY.id -> fragmentManager.replace(binding.host.id, TodayFragment())
                MENU.TIME_LINE.id -> fragmentManager.replace(binding.host.id, TimelineFragment())
                MENU.PROFILE.id -> {
                    fragmentManager.replace(binding.host.id, ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString(ProfileFragment.ARG_UID,AuthManager.uid)
                        }
                    })}
            }
            fragmentManager.commit()
            true
        }

        navigationBarView.selectedItemId = MENU.TODAY.id
    }
}