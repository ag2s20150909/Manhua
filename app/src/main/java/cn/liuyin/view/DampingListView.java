package cn.liuyin.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 1. 在View中增加了overSrollBy方法,用于记录x, y 轴上滚动
 * <p>
 * 2. 在AbsListView的onTouchEvent中判断是否到达边界(顶部 或 底部) ,然后调用view.overScrollBy ,传入 mScrollY等参数
 * <p>
 * 3. overScrollBy 最终赋值给View的mScrollX, mScrollY 两个变量
 * <p>
 * 4. 在AbsListView中调用完overScrollBy之后,调用invalidate重绘
 */
public class DampingListView extends ListView {


    //
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    // 手指按下时的Y值, 用于在移动时计算移动距离


    private Context mContext;
    private int mMaxYOverscrollDistance;
    private BounceCallBack mBounceCallback;

    public DampingListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public DampingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public DampingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceListView();
    }

    public void setOnBounceCallBack(BounceCallBack callback) {
        mBounceCallback = callback;
    }

    private void doCallBack() {
        if (mBounceCallback != null) {
            if (this.getScrollY() <= -(mMaxYOverscrollDistance * 0.8)) {
                mBounceCallback.onOverScrollUp();
            } else if (this.getScrollY() >= (mMaxYOverscrollDistance * 0.8)) {
                mBounceCallback.onOverScrollDown();
            }
        }


    }

    private void initBounceListView() {

        final DisplayMetrics metrics = mContext.getResources()
                .getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP) {
            doCallBack();
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);
    }


    //回调触发接口
    public interface BounceCallBack {
        void onOverScrollUp();

        void onOverScrollDown();
    }


}
