package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.model.OnBoardItem;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OnBoardAdapter extends PagerAdapter {

    private List<OnBoardItem> slideitemList;
    private Context context;

    public OnBoardAdapter(List<OnBoardItem> slideitemList, Context context) {
        this.slideitemList = slideitemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideitemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.slide_item,container,false);
        TextView title = view.findViewById(R.id.slideTitleTV);
        TextView desc = view.findViewById(R.id.slideDescTV);
        ImageView imageView =  view.findViewById(R.id.slideImageView);


        title.setText(slideitemList.get(position).getTitle());
        desc.setText(slideitemList.get(position).getDescription());
        imageView.setImageResource(slideitemList.get(position).getImage());


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
