package z.swipelibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Miller Zhang  on 2017/1/17.
 * desc:
 * github:https://github.com/zxyaust  CSDN:http://blog.csdn.net/qq_31340657
 * Whatever happens tomorrow,we've had today.
 */

public class RefresherView extends LoaderView {

    private TextView childAt;

    public RefresherView(Context context) {
        this(context, null);
    }

    public RefresherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefresherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public void onStateChage(int state) {
        switch (state) {
            case PullLayout.REFRESH_STATE_RELEASE_TO_INIT:
                Log.e("xixi","放开恢复原位");
                break;
            case PullLayout.REFRESH_STATE_RELEASE_TO_REFRESHING:
                Log.e("xixi","放开刷新");
                break;
            case PullLayout.REFRESH_STATE_REFRESHING:
                Log.e("xixi","正在刷新");
                break;
            case PullLayout.REFRESH_STATE_COMPLETE:
                Log.e("xixi","刷新完毕,准备回归");
                break;
            case PullLayout.STATE_INIT:
                Log.e("xixi","恢复原位");
                break;

        }
    }
}
