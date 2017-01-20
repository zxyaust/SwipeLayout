package com.z.max.swipe;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {

    private RelativeLayout mHeadRelativeLayout;
    private RecyclerView mRvRecyclerView;
    private NestedScrollView mNsNestedScrollView;
    private RelativeLayout mActivityMain4RelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mHeadRelativeLayout = (RelativeLayout) findViewById(R.id.head);
        mRvRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mNsNestedScrollView = (NestedScrollView) findViewById(R.id.ns);
        mActivityMain4RelativeLayout = (RelativeLayout) findViewById(R.id.activity_main4);
        mNsNestedScrollView.fling(200000);
        mRvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        mRvRecyclerView.setAdapter(new CommonAdapter<String>(getBaseContext(), R.layout.load_layout, strings) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        });
    }
}
