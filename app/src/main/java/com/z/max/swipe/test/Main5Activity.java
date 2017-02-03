package com.z.max.swipe.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z.max.swipe.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

public class Main5Activity extends AppCompatActivity {

    protected TextView tv;
    protected RelativeLayout activityMain5;
    protected RecyclerView rv;
    private int i;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main5);
        initView();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        tv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.e("xixi", "layout发生了");
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setX(i = i + 10);
                log();
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList();
        list.add("xixi");
        list.add("");
        list.add("xixi");
        list.add("");
        list.add("xixi");
        list.add("");
        adapter = new CommonAdapter<String>(getBaseContext(), R.layout.refresh_layout, list) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {

            }
        };
        rv.setAdapter(adapter);
        activityMain5 = (RelativeLayout) findViewById(R.id.activity_main5);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                log();
            }
        }, 1000);

    }

    private void log() {
        float translationX = tv.getTranslationX();
        float x = tv.getX();
        int left = tv.getLeft();
        int right = tv.getRight();
        Log.e("xixi", "尺寸,tx:" + translationX + "x:" + x + "left:" + left + "right:" + right);
    }
}
