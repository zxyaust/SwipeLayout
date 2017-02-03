package z.swipelibrary;

import android.animation.ObjectAnimator;
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

public class PullLayout1 extends RelativeLayout {
    public static final int STATE_INIT = 0;
    public static final int REFRESH_STATE_RELEASE_TO_INIT = 1;
    public static final int REFRESH_STATE_RELEASE_TO_REFRESHING = 2;
    public static final int REFRESH_STATE_COMPLETE = 3;
    public static final int REFRESH_STATE_REFRESHING = 4;


    private RefresherView refresherView;
    private LoaderView loaderView;
    private RecyclerView rv;
    private int mState = STATE_INIT;
    private LinearLayoutManager layoutManager;
    private float yDown;
    private int height;
    private boolean isRefreshOrLoadmore;
    protected float limitRefreshHeight = 200;
    private int delayMillis = 500;
    private int oldState = STATE_INIT;
    private float dy;
    private int refreshViewY;
    private int loaderViewY;

    public PullLayout1(Context context) {
        this(context, null);
    }

    public PullLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        height = getMeasuredHeight();
        loaderViewY = height;
        refreshViewY = -height;
        refresherView.setTranslationY(refreshViewY);//隐藏刷新和加载布局
        loaderView.setTranslationY(loaderViewY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    return true;
                    //第一个条目完全可见,并且向下拉动时拦截触摸事件,此时,pulllayout的ontouch方法会被调用,下面的工作全部由此方法处理
                }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                dy = event.getY() - yDown;
                dy = (float) (0.35 * dy);
                if (mState < REFRESH_STATE_REFRESHING) {//如果是刷新阶段,并且不是正在刷新状态,那么只能拉动高度为正
                    if (dy < 0)//下拉高度为负,实际已经是上拉了,那么直接break
                        break;//否则进行位移
                    rv.setTranslationY(dy);
                    refresherView.setTranslationY(refreshViewY + dy);
                }else if(mState==REFRESH_STATE_REFRESHING){//正在刷新,那么

                }
                 /*else {//如果是加载相关操作
                    if (dy > 0)//如果上拉高度为正,其实已经是下拉了,那么直接break
                        break;//否则进行位移
                    rv.setTranslationY(dy);
                    loaderView.setTranslationY(loaderViewY + dy);
                }*/
                updateState();//根据高度的变化设置state.
                break;
            case MotionEvent.ACTION_UP:
                onStateChanged();//手指抬起时,根据state值改变view位置
                break;
        }
        return false;
    }

    private void onStateChanged() {
        switch (mState) {
            case REFRESH_STATE_RELEASE_TO_INIT://如果是放开回复,那么rv和refreshview回复原位,state设置为init
                smoothTo(rv, 0);
                smoothTo(refresherView, refreshViewY);
                mState = STATE_INIT;
                break;
            case REFRESH_STATE_RELEASE_TO_REFRESHING://如果是放开刷新,那么rv和refreshview到达刷新的位置,state设置为refreshing
                smoothTo(rv, limitRefreshHeight);//rv下移到limit高度
                smoothTo(refresherView, refreshViewY + limitRefreshHeight);//refreshview下移到刷新的位置
                mState = REFRESH_STATE_REFRESHING;
                onStateChanged();
                break;
            case REFRESH_STATE_REFRESHING:
                if (loadListener != null)
                    loadListener.onRefresh();//当正在刷新时,调用refresh方法
                break;
            case REFRESH_STATE_COMPLETE://完成刷新之后,过一段时间回复原位
                refresherView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smoothTo(rv, 0);
                        smoothTo(refresherView, refreshViewY);
                        mState = STATE_INIT;
                    }
                }, delayMillis);
                break;
        }
        updateRefreshAndLoadViewState();
     /*       case LOAD_STATE_RELEASE_TO_LOADING:
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
     */

    }

    private void smoothTo(View view, float newTranslationY) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, newTranslationY).
                setDuration(delayMillis)//时间2秒
                .start();//开始
    }

    private void updateState() {
        if (dy > limitRefreshHeight) {
            mState = REFRESH_STATE_RELEASE_TO_REFRESHING;//只有状态与原来不同了之后才重新设置state
        } else if (dy < limitRefreshHeight && dy >= 0) {
            mState = REFRESH_STATE_RELEASE_TO_INIT;
        }
        updateRefreshAndLoadViewState();
    }

    private void updateRefreshAndLoadViewState() {
        if (oldState != mState) {//只有新状态不同于旧状态时才改变loadview和refreshview的状态
            loaderView.onStateChage(mState);//最后调用这个
            refresherView.onStateChage(mState);
            oldState = mState;
        }
    }

    private OnLoadListener loadListener;

    public interface OnLoadListener {
        void onRefresh();

    }

    public void setOnLoadListener(OnLoadListener listener) {
        loadListener = listener;
    }

    /**
     * 刷新完成或者加载完成
     */
    public void completeRefreshOrLoadMore() {
        if (mState != REFRESH_STATE_COMPLETE) {
            mState = REFRESH_STATE_COMPLETE;
            onStateChanged();
        }
    }
/*
    private void smoothBy(View view, float offset) {
        float translationY = view.getTranslationY();
        ObjectAnimator.ofFloat(view, "translationY", translationY, translationY + offset).
                setDuration(delayMillis)
                .start();
    }*/
}
