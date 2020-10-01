package com.example.firebasechat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.firebasechat.fragments.ChatsFragment
import com.example.firebasechat.fragments.UsersFragment

class ViewPagerAdpater(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return UsersFragment()
            }
            1 ->{
                return ChatsFragment()
            }
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> return "FRIENDS"
            1 -> return "CHATS"
        }
        return null!!
    }

}