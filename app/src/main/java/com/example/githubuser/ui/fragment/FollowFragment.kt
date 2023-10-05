package com.example.githubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.UserDetailViewModel
import com.example.githubuser.ui.adapter.UserAdapter

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private var position: Int? = null
    private var username: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDetailViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[UserDetailViewModel::class.java]

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            binding.rvFollow.removeAllViews()
            userDetailViewModel.follower.observe(viewLifecycleOwner) {
                item -> setFollowData(item)
            }
        } else {
            binding.rvFollow.removeAllViews()
            userDetailViewModel.following.observe(viewLifecycleOwner) {
                item -> setFollowData(item)
            }
        }
    }

    private fun setFollowData(item: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(item)
        binding.rvFollow.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "username"
    }
}