package com.example.fundamentalgithub.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class GithubUserDetailAdapter(fa:FragmentActivity,
                              private val fragmentList:MutableList<Fragment>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = getFragmentCount()

    override fun createFragment(position: Int): Fragment = getFragment(position)

    private fun getFragmentCount(): Int = fragmentList.size

    private fun getFragment(position: Int): Fragment = fragmentList[position]
}