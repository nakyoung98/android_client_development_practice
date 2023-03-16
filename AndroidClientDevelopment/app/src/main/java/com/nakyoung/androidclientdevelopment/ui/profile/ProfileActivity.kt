package com.nakyoung.androidclientdevelopment.ui.profile

import android.os.Bundle
import android.view.MenuItem
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.databinding.ActivityProfileBinding
import com.nakyoung.androidclientdevelopment.ui.base.BaseActivity

class ProfileActivity : BaseActivity() {
    companion object {
        const val EXTRA_UID = "uid"
    }

    lateinit var binding:ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getStringExtra(EXTRA_UID)

        supportActionBar?.title = uid
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = ProfileFragment()
            fragment.arguments = Bundle().apply {
                putString(ProfileFragment.ARG_UID, uid)
            }
            val fragmentManager = supportFragmentManager.beginTransaction()
            fragmentManager.add(binding.host.id,fragment)
            fragmentManager.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}