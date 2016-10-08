package com.z.max.swipe.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.z.max.swipe.R;

import z.swipelibrary.SwipeHorizontal;

public class Main2Activity extends AppCompatActivity {

    private TextView mDismissTextView;
    private TextView mTv2TextView;
    private LinearLayout mLeftLinearLayout;
    private LinearLayout mRightLinearLayout;
    private LinearLayout mMiddleLinearLayout;
    private SwipeHorizontal mSwiperSwipeHorizontal;
    private LinearLayout mAllLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDismissTextView = (TextView) findViewById(R.id.dismiss);
        mTv2TextView = (TextView) findViewById(R.id.tv2);
        mLeftLinearLayout = (LinearLayout) findViewById(R.id.left);
        mRightLinearLayout = (LinearLayout) findViewById(R.id.right);
        mMiddleLinearLayout = (LinearLayout) findViewById(R.id.middle);
        mSwiperSwipeHorizontal = (SwipeHorizontal) findViewById(R.id.swiper);
        mAllLinearLayout = (LinearLayout) findViewById(R.id.all);
        mDismissTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwiperSwipeHorizontal.close();
                mDismissTextView.setVisibility(View.GONE);
            }
        });
    }
}
