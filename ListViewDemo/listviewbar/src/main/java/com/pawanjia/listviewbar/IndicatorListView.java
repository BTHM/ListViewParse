package com.pawanjia.listviewbar;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

/**
 * Description  带指示的list
 *
 * @author liupeng502
 * @data 2017/9/7
 */

public class IndicatorListView extends FrameLayout {

    private  ViewGroup mLayout;
    private Paint mPaint;
   // private String[] textArray = {"概况", "病因", "临床表现", "检查", "诊断", "并发症", "治疗", "预后", "预防", "护理"};
   private List<String> textArray;
    private int measuredWidth;
    private int sectionHeight;
    private int measuredHeight;
    private float downY;
    private OnTouchListner mTouchListner;
    private int selectedPosition = 0;
    private int textSize;
    private int textX;
    private int maxPosition;
    private float downX;
    private int textAreaWidth;
    private int arrowWidth;
    private ListView list;

    public IndicatorListView(Context context) {
        this(context, null);
    }

    public IndicatorListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textSize = ToolUtils.sp2px(getContext(), 14);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_353535));
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        sectionHeight = ToolUtils.dip2px(context, 48);
        arrowWidth = ToolUtils.dip2px(getContext(), 25);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mLayout = (ViewGroup) layoutInflater.inflate(R.layout.item_listview, this, true);
        list = (ListView) mLayout.findViewById(R.id.list);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textAreaWidth= list.getWidth();
    }

    public int getTextAreaWidth() {
        return textAreaWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();

        textAreaWidth = measuredWidth - arrowWidth;
        textX = arrowWidth + textAreaWidth / 2;
    }





    public void setSelectedPosition(int position) {
        selectedPosition = position;
        invalidate();
    }

    interface OnTouchListner {
        void onTouch(int position);
    }

    /**
     * 联动外部
     */
    public void setOnTouchListner(OnTouchListner touchListner) {
        mTouchListner = touchListner;
    }


    /** 设置侧滑栏文本
     * @param textArray
     */
    public void setIndicatorText(List<String> textArray) {
        this.textArray = textArray;
        if (textArray == null || textArray.size()<=0) {
            maxPosition=0;
        }else {
            maxPosition = textArray.size() - 1;
        }
        invalidate();
    }
}
