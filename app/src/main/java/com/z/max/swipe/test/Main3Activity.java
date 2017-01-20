package com.z.max.swipe.test;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.z.max.swipe.R;

import z.swipelibrary.PullLayout;

public class Main3Activity extends AppCompatActivity {

    private TextView mTvTextView;
    private RecyclerView mRvRecyclerView;
    private RelativeLayout mActivityMain3RelativeLayout;
    private PullLayout mPullPullLayout;
    private PullLayout mPulllayPullLayout;
    private TextView mTv1TextView;
    private boolean flag;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mTvTextView = (TextView) findViewById(R.id.tv);
        mRvRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mPulllayPullLayout = (PullLayout) findViewById(R.id.pulllay);
        mTv1TextView = (TextView) findViewById(R.id.tv1);
        mActivityMain3RelativeLayout = (RelativeLayout) findViewById(R.id.activity_main3);

        i = 0;
        mTv1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 50;
                if (i > 150) {
                    smoothTo(mTv1TextView, 0);
                } else
                    smoothTo(mTv1TextView, i);
            }
        });

    }

    private void smoothBy(View view, float offset) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, translationY + offset).
                setDuration(200)//时间2秒
                .start();//开始
    }

    private void smoothTo(View view, float newTranslationY) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, newTranslationY).
                setDuration(200)//时间2秒
                .start();//开始
    }
}
