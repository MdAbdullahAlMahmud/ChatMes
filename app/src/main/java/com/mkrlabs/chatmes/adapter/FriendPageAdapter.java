package com.mkrlabs.chatmes.adapter;

import com.mkrlabs.chatmes.fragment.AllFriendsFragment;
import com.mkrlabs.chatmes.fragment.FriendRequestFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FriendPageAdapter  extends FragmentPagerAdapter {

    private List<Fragment> fragmentList ;
    private  String[] titleList = new String[]{"Request","All Friends"};
    public FriendPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList = new ArrayList<>();
        fragmentList.add(new FriendRequestFragment());
        fragmentList.add(new AllFriendsFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }
}
