# 安卓swipeLayout双向侧滑控件

标签（空格分隔）： 安卓 双向侧滑

---
#说明:
可以实现横向的双向滑动和纵向的竖向滑动,使用非常方便,只需要在布局中指定leftview,contentview和rightview即可实现左右滑动效果,使用起来跟普通控件一样简单.
![此处输入图片的描述][1]
#1.用法
##1.引用
```
 compile 'com.z:SwipeLayout:1.0.0'
```
##2.布局中使用
该控件继承自framlayout,必须要有三个自布局,一个是左边view,一个是中间view一个是右边view,使用中要指定view的id,自布局不用区分先后顺序.
注意一下三个属性的设置:
        app_contentView_id="@+id/middle"
        
        app_leftView_id="@+id/left"
        
        app_rightView_id="@+id/right"
```
<--如果用实现竖向的双向滑动,要用SwipeVertical布局即可-->
    <z.swipelibrary.SwipeHorizontal

        android:id="@+id/swiper"

        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginTop="50dp"
        app:app_contentView_id="@+id/middle"
        app:app_leftView_id="@+id/left"
        app:app_rightView_id="@+id/right">

        <LinearLayout

            android:id="@id/left"
            android:layout_width="wrap_content"
            android:layout_height="match_parent">

            <TextView
                android:id="@+id/dismiss"
                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#0f0"
                android:text="left12345678901234" />

            <TextView
                android:id="@+id/tv2"
                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#ff0"
                android:text="left12345678901234" />

        </LinearLayout>

        <LinearLayout
            android:id="@id/right"
            android:layout_width="200dp"
            android:layout_height="match_parent">

            <TextView

                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#5f00"
                android:text="right12345678901234" />

            <TextView

                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#f00"
                android:text="right12345678901234" />

        </LinearLayout>

        <LinearLayout
            android:id="@id/middle"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <TextView
                android:layout_width="200dp"
                android:layout_height="50dp"
                android:background="#500f"
                android:text="content" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#00f"
                android:text="content" />

        </LinearLayout>

    </z.swipelibrary.SwipeHorizontal>

```

##3.直接找到布局中的对应的控件,随意使用

```
mLeftLinearLayout = (LinearLayout) findViewById(R.id.left);
        mRightLinearLayout = (LinearLayout) findViewById(R.id.right);
        mMiddleLinearLayout = (LinearLayout) findViewById(R.id.middle);
        mSwiperSwipeHorizontal = (SwipeHorizontal) findViewById(R.id.swiper);
```
##4.其他方法的使用
```
//获取滑动速度阈值
    public double getSPEED_LIMIT() {
        return SPEED_LIMIT;
    }
//设置滑动素的阈值
    public void setSPEED_LIMIT(double SPEED_LIMIT) {
        this.SPEED_LIMIT = SPEED_LIMIT;
    }
//左边是否打开
    public boolean isLeftOpen() {
        return isLeftOpen;
    }
//右边是否打开
    public boolean isRrightOpen() {
        return isRrightOpen;
    }
    //打开右边
 public void openRight()
 //打开左边
 public void openLeft()
 //关闭,无论那边是打开的都可以关闭
 public void close()
 //设置打开关闭的监听器
public void setOnDragActionListener(OnDragActionListener listener)
```
###5.竖向的双向滑动实现示例
跟横向的非常相像,只是把leftview换成topview,rightview换成了bottomview而已,打开方法是openTop().....
```
    <z.swipelibrary.SwipeVertical
        android:id="@+id/swiper1"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginTop="50dp"
        app:app_BottomView_id="@+id/bottom"
        app:app_MiddleView_id="@+id/middle1"
        app:app_TopView_id="@+id/top">

        <LinearLayout
            android:id="@id/top"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <TextView
                android:id="@+id/dismiss1"
                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#0f0"
                android:text="left12345678901234" />

            <TextView
                android:id="@+id/tv3"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#ff0"
                android:text="left12345678901234" />

        </LinearLayout>

        <LinearLayout
            android:id="@id/bottom"
            android:layout_width="match_parent"
            android:layout_height="50dp">

            <TextView
                android:layout_width="100dp"
                android:layout_height="50dp"
                android:background="#5f00"
                android:text="right12345678901234" />

            <TextView

                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#f00"
                android:text="right12345678901234" />

        </LinearLayout>

        <LinearLayout
            android:id="@id/middle1"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <TextView
                android:layout_width="200dp"
                android:layout_height="50dp"
                android:background="#500f"
                android:text="content" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#50ff"
                android:text="content" />

        </LinearLayout>

    </z.swipelibrary.SwipeVertical>

```
###6.已知bug和注意事项
1.bug:横向滑动的不支持设置左右padding值,纵向滑动不支持设置上下padding值,如果设置不会起到任何作用,
2.注意:此控件中有三个直接的子控件,必须设置,如果要实现只打开一边等功能,直接把另一边的view宽度或者高度设置为0即可,
#2.原理
使用viewdraghelper实现

  [1]: https://github.com/zxyaust/SwipeLayout/blob/master/SCR_20161008_1033141.gif?raw=true