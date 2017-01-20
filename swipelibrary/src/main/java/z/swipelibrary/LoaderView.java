package z.swipelibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Miller Zhang  on 2017/1/17.
 * desc:
 * github:https://github.com/zxyaust  CSDN:http://blog.csdn.net/qq_31340657
 * Whatever happens tomorrow,we've had today.
 */

public class LoaderView extends RelativeLayout {
    public LoaderView(Context context) {
        this(context, null);
    }

    public LoaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void onStateChage(int state) {
        switch (state) {
            case PullLayout.LOAD_STATE_COMPLETE_LOAD:
                Log.e("xixi", "完成加载");
                break;
            case PullLayout.LOAD_STATE_LOADING:
                Log.e("xixi", "正在加载");
                break;
            case PullLayout.LOAD_STATE_RELEASE_TO_INIT:
                Log.e("xixi", "放开恢复原位");
                break;
            case PullLayout.LOAD_STATE_RELEASE_TO_LOADING:
                Log.e("xixi", "放开加载");
                break;
            case PullLayout.STATE_INIT:
                Log.e("xixi", "恢复原位");
                break;
        }
    }

    ;
}
