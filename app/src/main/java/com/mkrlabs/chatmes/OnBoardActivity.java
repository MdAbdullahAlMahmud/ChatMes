package com.mkrlabs.chatmes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mkrlabs.chatmes.adapter.OnBoardAdapter;
import com.mkrlabs.chatmes.model.OnBoardItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardActivity extends AppCompatActivity {
    ViewPager onboardViewPager;
    OnBoardAdapter adapter;
    FloatingActionButton onboardContinueButton;
    List<OnBoardItem> sliderList;
    TabLayout tabLayout;
    TextView skipTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.systembartransparent(this);
        setContentView(R.layout.activity_on_board);
        init();

        sliderList.add(new OnBoardItem("Connect With Friends","Connect with your friends with a single tap easily. ",R.drawable.item_1));
        sliderList.add(new OnBoardItem("Do Real-Time Messaging","Send message to your friend and receive message from your friend anytime, anywhere.",R.drawable.item_2));
        sliderList.add(new OnBoardItem("Share Photos & Videos","Share photos & Videos with your friend & family to enjoy your social time",R.drawable.item_3));

        adapter= new OnBoardAdapter(sliderList,this);
        onboardViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(onboardViewPager);


        onboardViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==sliderList.size()-1){
                    onboardContinueButton.setVisibility(View.VISIBLE);
                }else {
                    onboardContinueButton.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        onboardContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        });
        skipTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        });


    }

    private void gotoLoginActivity() {
        Intent intent= new Intent(OnBoardActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {
        onboardViewPager=findViewById(R.id.onboardViewPager);
        onboardContinueButton=findViewById(R.id.onboardContinueButton);
        tabLayout=findViewById(R.id.onboardTabIndicator);
        skipTV=findViewById(R.id.onboard_skipTV);

        sliderList= new ArrayList<>();
    }
}