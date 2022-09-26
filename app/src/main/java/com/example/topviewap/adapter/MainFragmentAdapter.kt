package com.example.topviewap.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

open class MainFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    lateinit var fragmentList:MutableList<Fragment>
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}