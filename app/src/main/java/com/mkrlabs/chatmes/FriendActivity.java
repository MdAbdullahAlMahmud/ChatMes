package com.mkrlabs.chatmes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mkrlabs.chatmes.adapter.FriendPageAdapter;

public class FriendActivity extends AppCompatActivity {

    TabLayout friendTabLayout;
    ViewPager friendViewPager;
    FriendPageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        init();
        setUpViewPager();
    }

    private void setUpViewPager() {
        pageAdapter= new FriendPageAdapter(getSupportFragmentManager(),1);
        friendTabLayout.setupWithViewPager(friendViewPager);
        friendViewPager.setAdapter(pageAdapter);


    }

    private void init() {
        friendTabLayout=findViewById(R.id.friendTabLayout);
        friendViewPager=findViewById(R.id.friendViewPager);
    }
}