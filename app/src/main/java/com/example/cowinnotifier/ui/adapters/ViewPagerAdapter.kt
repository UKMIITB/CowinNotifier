package com.example.cowinnotifier.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cowinnotifier.ui.DistrictSearchFragment
import com.example.cowinnotifier.ui.PincodeSearchFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PincodeSearchFragment()
            1 -> DistrictSearchFragment()
            else -> PincodeSearchFragment()
        }
    }
}