package z.swipelibrary;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miller Zhang  on 2017/1/17.
 * desc:
 * github:https://github.com/zxyaust  CSDN:http://blog.csdn.net/qq_31340657
 * Whatever happens tomorrow,we've had today.
 */

public class PullLayout extends RelativeLayout {
    public static final int STATE_INIT = 0;
    public static final int REFRESH_STATE_REFRESHING = 1;
    public static final int REFRESH_STATE_RELEASE_TO_INIT = 2;
    public static final int REFRESH_STATE_RELEASE_TO_REFRESHING = 3;
    public static final int REFRESH_STATE_COMPLETE = 4;
    public static final int LOAD_STATE_LOADING = 6;
    public static final int LOAD_STATE_RELEASE_TO_LOADING = 7;
    public static final int LOAD_STATE_RELEASE_TO_INIT = 8;
    public static final int LOAD_STATE_COMPLETE_LOAD = 9;


    private RefresherView refresherView;
    private LoaderView loaderView;
    private RecyclerView rv;
    private int mState = STATE_INIT;
    private LinearLayoutManager layoutManager;
    private float yDown;
    private int rvtop;
    private int height;
    private int refresherViewTop;
    private int loaderViewTop;
    private int rvBottom;
    private int refresherViewBottom;
    private int loaderViewBottom;
    private boolean isRefreshOrLoadmore;
    protected float limitRefreshHeight = 200;
    private int delayMillis = 500;
    private int oldState = STATE_INIT;
    private float dy;

    public PullLayout(Context context) {
        this(context, null);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        height = getMeasuredHeight();
        loaderView.offsetTopAndBottom(height);
        refresherView.offsetTopAndBottom(-height);
        rvtop = rv.getTop();
        rvBottom = rv.getBottom();
        refresherViewTop = refresherView.getTop();
        refresherViewBottom = refresherView.getBottom();
        loaderViewTop = loaderView.getTop();
        loaderViewBottom = loaderView.getBottom();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mState != REFRESH_STATE_REFRESHING) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yDown = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dy = ev.getY() - yDown;
                    if (dy > 0) {
                        isRefreshOrLoadmore = true;
                        return true;//第一个条目完全可见,并且向下拉动
                    }
            }
        } else if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1
                && mState != LOAD_STATE_LOADING) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yDown = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dy = ev.getY() - yDown;
                    if (dy < 0) {
                        isRefreshOrLoadmore = false;
                        return true;
                    }
                    break;

            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                dy = event.getY() - yDown;
                dy = (float) (0.5 * dy);
                if (isRefreshOrLoadmore) {
                    if (dy < 0)
                        break;
                    rv.setTranslationY(dy);
                    refresherView.setTranslationY(dy);
//                    rv.setTop((int) (rvtop + dy));
//                    rv.setBottom((int) (rvBottom + dy));
//                    refresherView.setTop((int) dy + refresherViewTop);
//                    refresherView.setBottom((int) (refresherViewBottom + dy));
//                    }
                } else {
                    if (dy > 0)
                        break;
                    rv.setTop((int) (rvtop + dy));
                    rv.setBottom((int) (rvBottom + dy));
                    loaderView.setBottom((int) (loaderViewBottom + dy));
                    loaderView.setTop((int) (dy + loaderViewTop));
                }
                //dy为-时,上拉,dy为正时下拉
                if (dy > limitRefreshHeight) {
                    mState = REFRESH_STATE_RELEASE_TO_REFRESHING;
                } else if (dy < limitRefreshHeight && dy > 0) {
                    mState = REFRESH_STATE_RELEASE_TO_INIT;
                } else if (dy == 0) {
                    mState = STATE_INIT;
                } else if (dy < 0 && dy > -limitRefreshHeight) {
                    mState = LOAD_STATE_RELEASE_TO_INIT;
                } else if (dy < -limitRefreshHeight) {
                    mState = LOAD_STATE_RELEASE_TO_LOADING;
                }
                updateState();
                break;
            case MotionEvent.ACTION_UP:
                onStateChanged();
                break;
        }
        return false;
    }

    private void onStateChanged() {
        updateState();
        switch (mState) {
            case REFRESH_STATE_RELEASE_TO_INIT:
                smoothTo(rv,0);
                smoothTo(loaderView,0);
//                if (loaderView.getTop() != loaderViewTop) {
//                    rv.setTop(rvtop);
//                    rv.setBottom(rvBottom);
//                    loaderView.setBottom(loaderViewBottom);
//                    loaderView.setTop(loaderViewTop);
//                }
//                close(0);
                break;
            case REFRESH_STATE_RELEASE_TO_REFRESHING:
                close((int) limitRefreshHeight);
                mState = REFRESH_STATE_REFRESHING;
                onStateChanged();
                break;
            case REFRESH_STATE_REFRESHING:
                //网络请求,完了就调用complete
                refresherView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mState = REFRESH_STATE_COMPLETE;
                        onStateChanged();
                    }
                }, 4000);
                break;
            case REFRESH_STATE_COMPLETE:
                refresherView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mState = STATE_INIT;
                        onStateChanged();
                    }
                }, delayMillis);
                break;
            case STATE_INIT:
                if (loaderView.getTop() != loaderViewTop) {
                    rv.setTop(rvtop);
                    rv.setBottom(rvBottom);
                    loaderView.setBottom(loaderViewBottom);
                    loaderView.setTop(loaderViewTop);
                } else if (refresherView.getTop() != refresherViewTop) {
                    rv.setTop(rvtop);
                    rv.setBottom(rvBottom);
                    refresherView.setTop(refresherViewTop);
                    refresherView.setBottom(refresherViewBottom);
                }
                break;
            case LOAD_STATE_RELEASE_TO_LOADING:
                mState = LOAD_STATE_LOADING;
                onStateChanged();
                refresherView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mState = LOAD_STATE_COMPLETE_LOAD;
                        onStateChanged();
                    }
                }, delayMillis);
                onStateChanged();
                break;

            case LOAD_STATE_RELEASE_TO_INIT:
                if (refresherView.getTop() != refresherViewTop) {
                    rv.setTop(rvtop);
                    rv.setBottom(rvBottom);
                    refresherView.setTop(refresherViewTop);
                    refresherView.setBottom(refresherViewBottom);
                }
                close(0);
                break;
            case LOAD_STATE_LOADING:
                close((int) -limitRefreshHeight);
                break;
            case LOAD_STATE_COMPLETE_LOAD:
                refresherView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mState = STATE_INIT;
                        onStateChanged();
                    }
                }, delayMillis);
                break;
        }
    }

    private void close(int i) {

        if (isRefreshOrLoadmore) {
            rv.setTop(rvtop + i);
            rv.setBottom(rvBottom + i);
            refresherView.setTop(refresherViewTop + i);
            refresherView.setBottom(refresherViewBottom + i);
        } else {
            rv.setTop(rvtop + i);
            rv.setBottom(rvBottom + i);
            loaderView.setBottom(loaderViewBottom + i);
            loaderView.setTop(loaderViewTop + i);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresherView = (RefresherView) getChildAt(0);
        loaderView = (LoaderView) getChildAt(1);
        rv = (RecyclerView) getChildAt(2);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("1");
        strings.add("1");
        strings.add("1");
        rv.setAdapter(new CommonAdapter<String>(getContext(), R.layout.load_layout, strings) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        });
    }

    private void smoothBy(View view, float offset) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, translationY + offset).
                setDuration(delayMillis)
                .start();
    }
    private void smoothTo(View view, float newTranslationY) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, newTranslationY).
                setDuration(delayMillis)//时间2秒
                .start();//开始
    }
    private void updateState() {
        if (oldState != mState) {
            loaderView.onStateChage(mState);//最后调用这个
            refresherView.onStateChage(mState);
            oldState = mState;
        }
    }
}
