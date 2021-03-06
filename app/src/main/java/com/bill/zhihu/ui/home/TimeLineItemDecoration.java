package com.bill.zhihu.ui.home;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.bill.zhihu.R;
import com.bill.zhihu.ZhihuApp;

/**
 * recycler view item divider
 * 
 * 算是一个对item的装饰类，一个recycler可以添加多个，比如这个是画一个简单的divider
 * 需要获取item的位置然后在ondraw中绘制一个divider，当然也可以是各种各样的drawable rect等
 * 
 * @author Bill Lv
 *
 */
public class TimeLineItemDecoration extends ItemDecoration {

    private boolean enableMargin;
    private Paint dividerPaint;
    private Rect dividerRect;

    public TimeLineItemDecoration() {
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(ZhihuApp.getContext().getResources()
                .getColor(R.color.grey));
        dividerRect = new Rect();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = parent.getChildAt(i);
            int marginLeft = 0;
            int marginRight = 0;
            if (enableMargin) {
                RecyclerView.LayoutParams lp = (LayoutParams) v
                        .getLayoutParams();
                marginLeft = lp.leftMargin;
                marginRight = lp.rightMargin;
            }
            int left = parent.getLeft() + marginLeft;
            int right = parent.getRight() - marginRight;
            int top = v.getTop();
            dividerRect.set(left, top, right, top + 1);
            c.drawRect(dividerRect, dividerPaint);
        }
    }

    /**
     * item view 的margin应用于divider
     */
    public void enableItemMargin() {
        enableMargin = true;
    }

    /**
     * divider 不受item view的margin影响
     */
    public void disableItemMargin() {
        enableMargin = false;
    }

}
