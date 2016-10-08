package z.swipelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by Bruce on 11/24/14.
 */
public class SwipeVertical extends FrameLayout {

    private boolean isTopOpen;
    private boolean isBottomOpen;
    private ViewDragHelper viewDragHelper;
    //中间布局
    private View middleView;
    //右边布局
    private View bottomView;
    //左边布局
    private View topView;
    //极限速率
    private double SPEED_LIMIT = 800.0;

    //右边布局的宽度
    private int hBottom;
    //中间布局的宽度
    private int hMiddle;
    //左边布局的宽度
    private int hTop;
    //左边布局的可移动范围总长度，以下同
    private int rangeTop;
    private int rangeMiddle;
    private int rangeBottom;
    //中间布局的最大left，下同
    private int maxTopMiddleView;
    //中间布局的最小left，下同
    private int minTopMiddleView;
    private int minTopBottView;
    private int maxTopBottomView;
    private int minTopTopView;
    private int maxTopTopView;
    //middleview已经拖动的距离
    private int middeViewDragedy;
    private int mTopViewId;
    private int mContentViewId;
    private int mBottomViewId;
    private int offsetTimes;

    public double getSPEED_LIMIT() {
        return SPEED_LIMIT;
    }

    public void setSPEED_LIMIT(double SPEED_LIMIT) {
        this.SPEED_LIMIT = SPEED_LIMIT;
    }

    public boolean isTopOpen() {
        return isTopOpen;
    }

    public boolean isBottomOpen() {
        return isBottomOpen;
    }

    public SwipeVertical(Context context) throws XmlPullParserException {
        this(context, null);
    }

    public SwipeVertical(Context context, AttributeSet attrs) throws XmlPullParserException {
        this(context, attrs, -1);
    }

    public SwipeVertical(Context context, AttributeSet attrs, int defStyleAttr) throws XmlPullParserException {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, z.swipelibrary.R.styleable.SwipeVertical);
        mTopViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeVertical_app_TopView_id, mTopViewId);
        mContentViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeVertical_app_MiddleView_id, mContentViewId);
        mBottomViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeVertical_app_BottomView_id, mBottomViewId);
        if (mContentViewId == 0 || mTopViewId == 0 || mBottomViewId == 0) {
            throw new XmlPullParserException(getContext().getString(z.swipelibrary.R.string.exception_no_field));
        }
        typedArray.recycle();
        viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getPaddingTop() != 0 || getPaddingBottom() != 0) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        }
        if (3 != getChildCount()) {
            StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
            Log.e(stack.getClassName() + "-->" + stack.getMethodName() + "-->" + stack.getLineNumber(),
                    getContext().getString(z.swipelibrary.R.string.error_string));
        }
        topView = findViewById(mTopViewId);
        bottomView = findViewById(mBottomViewId);
        middleView = findViewById(mContentViewId);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        hBottom = bottomView.getMeasuredHeight();
        hTop = topView.getMeasuredHeight();
        hMiddle = middleView.getMeasuredHeight();
        topView.offsetTopAndBottom(-hTop);
        bottomView.offsetTopAndBottom(hMiddle);
        offsetTimes++;
        maxTopMiddleView = hTop;
        minTopMiddleView = -hBottom;
        minTopBottView = hMiddle - hBottom;
        maxTopBottomView = hMiddle + hTop;
        minTopTopView = -hBottom;
        maxTopTopView = 0;
        rangeMiddle = maxTopMiddleView - minTopMiddleView;
        rangeBottom = maxTopBottomView - minTopBottView;
        rangeTop = maxTopTopView - minTopTopView;
    }


    private class DragHelperCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View view, int i) {
            return view == middleView || view == bottomView || view == topView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == middleView) {
                middeViewDragedy = top;
                topView.offsetTopAndBottom(dy);
                bottomView.offsetTopAndBottom(dy);
            } else if (changedView == topView) {
                middeViewDragedy = top + hTop;
                middleView.offsetTopAndBottom(dy);
                bottomView.offsetTopAndBottom(dy);
            } else {
                middeViewDragedy = top - hMiddle;
                middleView.offsetTopAndBottom(dy);
                topView.offsetTopAndBottom(dy);
            }
            invalidate();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (child == middleView) {//这里就是限制他的范围的，minLeft就是最小值，max就是最大值，小于最小值就让他等于最小值，大于最大值就让他等于最大值
                return getTop(top, maxTopMiddleView, minTopMiddleView);
            } else if (child == bottomView) {
                return getTop(top, maxTopBottomView, minTopBottView);
            } else {
                return getTop(top, maxTopTopView, minTopTopView);
            }
        }


        private int getTop(int top, int max, int min) {
            if (top > max) {
                top = max;
            } else if (top < min) {
                top = min;
            }
            return top;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            if (child == topView) {
                return rangeTop;
            } else if (child == bottomView) {
                return rangeBottom;
            } else {
                return rangeMiddle;
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (yvel >= SPEED_LIMIT) {
                if (isBottomOpen) {//右边拉开的,那就关闭
                    close();
                } else if (!isTopOpen && !isBottomOpen) {//两边都没有拉开,那就拉开左边
                    openTop();
                }//还有就是左边打开的,不用管
            } else if (yvel < SPEED_LIMIT && yvel > 0) {
                //这里就是需要用距离来判断了
                if (isTopOpen) {
                    if (middeViewDragedy > hTop / 2) {
                        openTop();
                    } else {
                        close();
                    }

                } else if (isBottomOpen) {
                    //middleview本身就是-wrihgt位置的
                    if (middeViewDragedy < -hBottom / 2) {
                        openBottom();
                    } else {
                        close();

                    }

                } else {
                    if (middeViewDragedy > hTop / 2) {
                        openTop();
                    } else {
                        close();
                    }

                }
            } else if (yvel == 0) {
                //距离判断
                if (isTopOpen) {
                    if (middeViewDragedy > hTop / 2) {
                        openTop();
                    } else {
                        close();
                    }

                } else if (isBottomOpen) {
                    if (middeViewDragedy > -hBottom / 2) {
                        close();
                    } else {
                        openBottom();
                    }
                } else {//都不开
                    if (middeViewDragedy > hTop / 2) {
                        openTop();
                    } else if (middeViewDragedy < -hBottom / 2) {
                        openBottom();
                    } else {
                        close();
                    }
                }

            } else if (yvel < 0 && yvel >= -SPEED_LIMIT) {
                if (isTopOpen) {
                    if (middeViewDragedy > hTop / 2) {
                        openTop();
                    } else {
                        close();
                    }
                } else if (isBottomOpen) {
                    if (middeViewDragedy > -hBottom / 2) {
                        close();
                    } else {
                        openBottom();
                    }
                } else {
                    if (Math.abs(middeViewDragedy) > hBottom / 2) {
                        openBottom();
                    } else {
                        close();
                    }
                }

            } else {//yvel<-SPEED_LIMIT
                if (isTopOpen) {//左边打开的
                    close();
                } else if (isBottomOpen) {
                } else {
                    openBottom();
                }
            }
        }

    }

    /**
     * 关闭两边
     */
    public void close() {
        if (listener != null)
            listener.onStartClose();
        viewDragHelper.smoothSlideViewTo(middleView, 0, 0);
        isTopOpen = false;
        isBottomOpen = false;
        ViewCompat.postInvalidateOnAnimation(SwipeVertical.this);
        if (listener != null)
            listener.onClosed();
    }

    /**
     * 打开左边
     */
    public void openTop() {
        if (listener != null)
            listener.onStartOpenTop();
        viewDragHelper.smoothSlideViewTo(middleView, 0, hTop);
        isTopOpen = true;
        ViewCompat.postInvalidateOnAnimation(SwipeVertical.this);
        if (listener != null)
            listener.onTopOpened();
    }

    /**
     * 打开右边
     */
    public void openBottom() {
        if (listener != null)
            listener.onStartOpenBottom();
        viewDragHelper.smoothSlideViewTo(middleView, 0, -hBottom);
        isBottomOpen = true;
        ViewCompat.postInvalidateOnAnimation(SwipeVertical.this);
        if (listener != null)
            listener.onBottomOpened();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            //当我们取消触摸事件时，dragHelper不处理事件，如果不加这个判断，手指划出控件之后仍然能操作，用户体验不正常
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //draghelper全权接管触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    OnDragActionListener listener;

    public void setOnDragActionListener(OnDragActionListener listener) {
        this.listener = listener;
    }

    interface OnDragActionListener {
        void onStartClose();

        void onStartOpenTop();

        void onStartOpenBottom();

        void onTopOpened();

        void onClosed();

        void onBottomOpened();
    }
}
