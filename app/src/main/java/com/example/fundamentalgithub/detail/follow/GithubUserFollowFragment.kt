package com.example.fundamentalgithub.detail.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalgithub.UserAdapter
import com.example.fundamentalgithub.data.model.User
import com.example.fundamentalgithub.databinding.FragmentFollowBinding
import com.example.fundamentalgithub.detail.GithubUserDetailViewModel
import com.example.fundamentalgithub.utils.ResultSealed

class GithubUserFollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {
        UserAdapter {

        }
    }
    private val viewModel by activityViewModels<GithubUserDetailViewModel>()

    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()
    }

    private fun setupRecyclerView() {
        binding.followRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@GithubUserFollowFragment.adapter
        }
    }

    private fun setupObserver() {
        when (type){
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner,this::manageResultFollow)
            }
            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner,this::manageResultFollow)
            }
        }
    }

    private fun manageResultFollow(state:ResultSealed){
        when (state){
            is ResultSealed.Success<*> -> {
                handleSuccess(state)
            }
            is ResultSealed.Error -> {
                handleError(state)
            }
            is ResultSealed.Loading -> {
                handleLoading(state)
            }
        }
    }

    private fun handleSuccess(state: ResultSealed.Success<*>) {
        adapter.setData(state.data as MutableList<User>)
    }

    private fun handleError(state: ResultSealed.Error) {
        Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun handleLoading(state: ResultSealed.Loading) {
        binding.followProgressBar.isVisible = state.isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 200
        fun newInstance(type: Int) = GithubUserFollowFragment()
            .apply {
                this.type = type
            }
    }
}