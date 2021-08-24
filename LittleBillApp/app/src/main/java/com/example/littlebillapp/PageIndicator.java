package com.example.littlebillapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageIndicator extends LinearLayout implements PageGridView.PageIndicator {
    public PageIndicator(Context context){this(context,null);}
    public PageIndicator(Context context, AttributeSet attrs){super(context,attrs);}

    @Override
    public void InitIndicatorItems(int itemsNumber){
        removeAllViews();
        for (int i=0;i<itemsNumber;i++){
            ImageView imageView=new ImageView(getContext());
            imageView.setImageResource(R.drawable.dot_unselected);
            imageView.setPadding(10,0,10,0);
            addView(imageView);
        }
    }

    @Override
    public void onPageSelected(int pageIndex){
        ImageView imageView=(ImageView)getChildAt(pageIndex);
        if (imageView!=null){
            imageView.setImageResource(R.drawable.dot_selected);
        }
    }

    @Override
    public void onPageUnSelected(int pageIndex){
        ImageView imageView=(ImageView)getChildAt(pageIndex);
        if (imageView!=null) {
            imageView.setImageResource(R.drawable.dot_unselected);
        }
    }
}
