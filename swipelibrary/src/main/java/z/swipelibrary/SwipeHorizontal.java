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
public class SwipeHorizontal extends FrameLayout {

    private boolean isLeftOpen;
    private boolean isRrightOpen;
    private ViewDragHelper viewDragHelper;
    //中间布局
    private View middleView;
    //右边布局
    private View rightView;
    //左边布局
    private View leftView;
    //极限速率
    private double SPEED_LIMIT = 800.0;

    //右边布局的宽度
    private int wRight;
    //中间布局的宽度
    private int wMiddle;
    //左边布局的宽度
    private int wLeft;
    //左边布局的可移动范围总长度，以下同
    private int rangeLeft;
    private int rangeMiddle;
    private int rangeRight;
    //中间布局的最大left，下同
    private int maxLeftMiddleView;
    //中间布局的最小left，下同
    private int minLeftMiddleView;
    private int minLeftRightView;
    private int maxLeftRightView;
    private int minLeftLeftView;
    private int maxLeftLeftView;
    //middleview已经拖动的距离
    private int middeViewDragedx;
    private int mLeftViewId;
    private int mContentViewId;
    private int mRightViewId;
    private int offsetTimes;

    public double getSPEED_LIMIT() {
        return SPEED_LIMIT;
    }

    public void setSPEED_LIMIT(double SPEED_LIMIT) {
        this.SPEED_LIMIT = SPEED_LIMIT;
    }

    public boolean isLeftOpen() {
        return isLeftOpen;
    }

    public boolean isRrightOpen() {
        return isRrightOpen;
    }

    public SwipeHorizontal(Context context) throws XmlPullParserException {
        this(context, null);
    }

    public SwipeHorizontal(Context context, AttributeSet attrs) throws XmlPullParserException {
        this(context, attrs, -1);
    }

    public SwipeHorizontal(Context context, AttributeSet attrs, int defStyleAttr) throws XmlPullParserException {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, z.swipelibrary.R.styleable.SwipeHorizontal);
        mLeftViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeHorizontal_app_leftView_id, mLeftViewId);
        mContentViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeHorizontal_app_contentView_id, mContentViewId);
        mRightViewId = typedArray.getResourceId(z.swipelibrary.R.styleable.SwipeHorizontal_app_rightView_id, mRightViewId);
        if (mContentViewId == 0 || mLeftViewId == 0 || mRightViewId == 0) {
            throw new XmlPullParserException(getContext().getString(z.swipelibrary.R.string.exception_no_field));
        }
        typedArray.recycle();
        viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getPaddingLeft() != 0 || getPaddingRight() != 0) {
            setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        }
        if (3 != getChildCount()) {
            StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
            Log.e(stack.getClassName() + "-->" + stack.getMethodName() + "-->" + stack.getLineNumber(),
                    getContext().getString(z.swipelibrary.R.string.error_string));
        }
        leftView = findViewById(mLeftViewId);
        rightView = findViewById(mRightViewId);
        middleView = findViewById(mContentViewId);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        wRight = rightView.getMeasuredWidth();
        wLeft = leftView.getMeasuredWidth();
        wMiddle = middleView.getMeasuredWidth();
        leftView.offsetLeftAndRight(-wLeft);
        rightView.offsetLeftAndRight(wMiddle);

        offsetTimes++;
        maxLeftMiddleView = wLeft;
        minLeftMiddleView = -wRight;
        minLeftRightView = wMiddle - wRight;
        maxLeftRightView = wMiddle + wLeft;
        minLeftLeftView = -wRight;
        maxLeftLeftView = 0;
        rangeMiddle = maxLeftMiddleView - minLeftMiddleView;
        rangeRight = maxLeftRightView - minLeftRightView;
        rangeLeft = maxLeftLeftView - minLeftLeftView;
    }


    private class DragHelperCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View view, int i) {
            return view == middleView || view == rightView || view == leftView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == middleView) {
                middeViewDragedx = left;
                leftView.offsetLeftAndRight(dx);
                rightView.offsetLeftAndRight(dx);
            } else if (changedView == leftView) {
                middeViewDragedx = left + wLeft;
                middleView.offsetLeftAndRight(dx);
                rightView.offsetLeftAndRight(dx);
            } else {
                middeViewDragedx = left - wMiddle;
                middleView.offsetLeftAndRight(dx);
                leftView.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        //这里的left是子view在父view中的相对值，左边距离父view的左边的距离
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == middleView) {//这里就是限制他的范围的，minLeft就是最小值，max就是最大值，小于最小值就让他等于最小值，大于最大值就让他等于最大值
                return getLeft(left, maxLeftMiddleView, minLeftMiddleView);
            } else if (child == rightView) {
                return getLeft(left, maxLeftRightView, minLeftRightView);
            } else {
                return getLeft(left, maxLeftLeftView, minLeftLeftView);
            }
        }

        private int getLeft(int left, int max, int min) {
            if (left > max) {
                left = max;
            } else if (left < min) {
                left = min;
            }
            return left;
        }

        //确定范围，在侧拉出的view需要点击事件时，必须处理这个方法，否则会出错
        @Override
        public int getViewHorizontalDragRange(View child) {
            if (child == leftView) {
                return rangeLeft;
            } else if (child == rightView) {
                return rangeRight;
            } else {
                return rangeMiddle;
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            System.out.println("之前结果" + isLeftOpen + ":" + isRrightOpen + middeViewDragedx + ":" + wLeft / 2);
            if (xvel >= SPEED_LIMIT) {
                if (isRrightOpen) {//右边拉开的,那就关闭
                    close();
                } else if (!isLeftOpen && !isRrightOpen) {//两边都没有拉开,那就拉开左边
                    openLeft();
                }//还有就是左边打开的,不用管
            } else if (xvel < SPEED_LIMIT && xvel > 0) {
                //这里就是需要用距离来判断了
                if (isLeftOpen) {
                    if (middeViewDragedx > wLeft / 2) {
                        openLeft();
                    } else {
                        close();
                    }

                } else if (isRrightOpen) {
                    //middleview本身就是-wrihgt位置的
                    if (middeViewDragedx > -wRight / 2) {
                        openRight();
                    } else {
                        close();
                    }

                } else {
                    if (middeViewDragedx > wLeft / 2) {
                        openLeft();
                    } else {
                        close();
                    }

                }
            } else if (xvel == 0) {
                //距离判断
                if (isLeftOpen) {
                    if (middeViewDragedx > wLeft / 2) {
                        openLeft();
                    } else {
                        close();
                    }

                } else if (isRrightOpen) {
                    if (middeViewDragedx > -wRight / 2) {
                        close();
                    } else {
                        openRight();
                    }
                } else {//都不开
                    if (middeViewDragedx > wLeft / 2) {
                        openLeft();
                    } else if (middeViewDragedx < -wRight / 2) {
                        openRight();
                    } else {
                        close();
                    }
                }

            } else if (xvel < 0 && xvel >= -SPEED_LIMIT) {
                if (isLeftOpen) {
                    if (middeViewDragedx > wLeft / 2) {
                        openLeft();
                    } else {
                        close();
                    }
                } else if (isRrightOpen) {
                    if (middeViewDragedx > -wRight / 2) {
                        close();
                    } else {
                        openRight();
                    }
                } else {
                    if (Math.abs(middeViewDragedx) > wRight / 2) {
                        openRight();
                    } else {
                        close();
                    }
                }

            } else {//xvel<-SPEED_LIMIT
                if (isLeftOpen) {//左边打开的
                    close();
                } else if (isRrightOpen) {
                } else {
                    openRight();
                }
            }
            System.out.println("后面结果" + "速度" + xvel + isLeftOpen + ":" + isRrightOpen + middeViewDragedx + ":" + wLeft / 2);
        }

    }

    /**
     * 关闭两边
     */
    public void close() {
        if (listener != null)
            listener.onStartClose();
        viewDragHelper.smoothSlideViewTo(middleView, 0, 0);
        isLeftOpen = false;
        isRrightOpen = false;
        ViewCompat.postInvalidateOnAnimation(SwipeHorizontal.this);
        if (listener != null)
            listener.onClosed();
    }

    /**
     * 打开左边
     */
    public void openLeft() {
        if (listener != null)
            listener.onStartOpenLeft();
        viewDragHelper.smoothSlideViewTo(middleView, wLeft, 0);
        isLeftOpen = true;
        ViewCompat.postInvalidateOnAnimation(SwipeHorizontal.this);
        if (listener != null)
            listener.onLeftOpened();
    }

    /**
     * 打开右边
     */
    public void openRight() {
        if (listener != null)
            listener.onStartOpenRight();
        viewDragHelper.smoothSlideViewTo(middleView, -wRight, 0);
        isRrightOpen = true;
        ViewCompat.postInvalidateOnAnimation(SwipeHorizontal.this);
        if (listener != null)
            listener.onRightOpened();
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

        void onStartOpenLeft();

        void onStartOpenRight();

        void onLeftOpened();

        void onClosed();

        void onRightOpened();
    }
}
