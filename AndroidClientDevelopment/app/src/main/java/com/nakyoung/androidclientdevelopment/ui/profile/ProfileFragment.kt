package com.nakyoung.androidclientdevelopment.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import coil.load
import coil.transform.CircleCropTransformation
import com.nakyoung.androidclientdevelopment.R
import com.nakyoung.androidclientdevelopment.api.response.User
import com.nakyoung.androidclientdevelopment.databinding.FragmentProfileBinding
import com.nakyoung.androidclientdevelopment.manager.AuthManager
import com.nakyoung.androidclientdevelopment.ui.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.ConnectException

class ProfileFragment :BaseFragment(){
    companion object{
        const val ARG_UID = "uid"
    }

    var _binding: FragmentProfileBinding? = null
    val binding
        get() = _binding!!

    val uid: String by lazy {
        requireArguments().getString(ARG_UID)!!
    }

    var adapter: UserAnswerAdapter? = null

    val userAnswerFlow = Pager(PagingConfig(pageSize = 5)){
        UserAnswerPagingSource(api, uid)
    }.flow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            try {
                val userResponse = api.getUser(uid)
                if (!userResponse.isSuccessful) {
                    return@launch
                }

                val user = userResponse.body() ?: return@launch
                setupProfile(user)
            } catch (e: ConnectException) {

            }
        }

        lifecycleScope.launch {
            adapter = UserAnswerAdapter(requireContext())
            binding.pager.adapter = adapter

            userAnswerFlow.collectLatest {
                adapter?.submitData(it)
            }
        }
    }

    fun setupProfile(user: User) {
        binding.name.text = user.name
        binding.description.text = user.description
        binding.answerCount.text = user.answerCount.toString()
        binding.followerCount.text = user.followerCount.toString()
        binding.followingCount.text = user.followingCount.toString()
        user.photo?.let {
            binding.photo.load(it) {
                placeholder(R.drawable.ph_user)
                error(R.drawable.ph_user)
                transformations(CircleCropTransformation())
            }
        }

        when {
            user.id == AuthManager.uid -> {
                binding.followButton.setText(R.string.me)
                binding.followButton.isEnabled = false
            }
            user.isFollowing == true -> {
                binding.followButton.setText(R.string.unfollow)
                binding.followButton.isEnabled = true
                binding.followButton.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(),R.color.unfollow_button)
                binding.followButton.setOnClickListener {
                    lifecycleScope.launch{
                        val response = api.unfollow(user.id)
                        if (response.isSuccessful) {
                            setupProfile(user.copy(isFollowing = false))
                        }
                    }
                }
            }
            else ->{
                binding.followButton.setText(R.string.follow)
                binding.followButton.isEnabled = true
                binding.followButton.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(),R.color.follow_button)
                binding.followButton.setOnClickListener {
                    lifecycleScope.launch{
                        val response = api.follow(user.id)
                        if (response.isSuccessful) {
                            setupProfile(user.copy(isFollowing = true))
                        }
                    }
                }
            }
        }

    }
}