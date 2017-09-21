package com.pawanjia.listviewbar;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description  带指示侧滑组合控件
 *
 * @author liupeng502
 * @data 2017/9/8
 */

public class IndicatorListGroup3 extends FrameLayout {



    private boolean mIsClose;
    private RelativeLayout rl;
    private ViewGroup mLayout;
    private ListView lv;
    private ListView indicator;
    private FrameLayout frame;
    private ImageView iv;
    private float downX;
    private float downY;
    private float totalDx;
    private Scroller mScroller;
    private int loadAnimationDuration = 500;
    private int scrollDuration = 200;
    private IndicatorAdapter indicatorAdapter;
    private int indicatorSelcPosition;
    private  List<String> textList=new ArrayList<>();
    private int mRlMeasuredWidth;
    private int mIndiMeasuredWidth;
    private int mDxWidth;
    private int mRlLeft;
    private int mRlRight;
    private int mRlBottom;
    private boolean isClick;
    private int mRlMeasuredHeight;

    public IndicatorListGroup3(Context context) {
        this(context, null);
    }

    public IndicatorListGroup3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorListGroup3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mLayout = (ViewGroup) layoutInflater.inflate(R.layout.indicator_list_group, this, true);
        rl = (RelativeLayout) mLayout.findViewById(R.id.indicator_rl);
        lv = (ListView) mLayout.findViewById(R.id.indicator_lv);
        indicator = (ListView) mLayout.findViewById(R.id.indicator_list);
        frame = (FrameLayout) mLayout.findViewById(R.id.indicator_frame);
        iv = (ImageView) mLayout.findViewById(R.id.indicator_iv);
        mScroller = new Scroller(getContext());
        mIsClose = true;
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
                    isClick=false;//触摸过内容区后由内容区控制导航栏选中
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!isClick) {
                    indicatorSelcPosition = firstVisibleItem;
                    if (indicatorAdapter != null) {
                        indicatorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        indicator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indicatorSelcPosition=position;
                isClick=true;
                indicatorAdapter.notifyDataSetChanged();
                lv.setSelection(position);
                closeIndicator();
            }
        });
        frame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsClose) {
                    openIndicator();
                } else {
                    closeIndicator();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRlMeasuredWidth = rl.getMeasuredWidth();
        mRlMeasuredHeight = rl.getMeasuredHeight();
        mIndiMeasuredWidth = indicator.getMeasuredWidth();
        mDxWidth=mRlMeasuredWidth-mIndiMeasuredWidth;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRlLeft = right - mDxWidth;
        mRlRight = right + mIndiMeasuredWidth;
        //mRlBottom=bottom;
        mRlBottom = mRlMeasuredHeight;
        setLayout((int) totalDx);
    }
    //设置位置
    private void setLayout(int touchDx){
        rl.layout(mRlLeft+touchDx,0, mRlRight+touchDx, mRlBottom);
    }


    //关闭
    private void closeIndicator() {
        mScroller.startScroll(Math.round(-mIndiMeasuredWidth), 0, Math.round(mIndiMeasuredWidth), 0, loadAnimationDuration);
        totalDx=0;
        invalidate();
        mIsClose = true;
    }

    //展开
    private void openIndicator() {
        mScroller.startScroll(0, 0, Math.round(-mIndiMeasuredWidth), 0, loadAnimationDuration);
        totalDx=-mIndiMeasuredWidth;
        invalidate();
        mIsClose = false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                if (Math.abs(moveY - downY) < Math.abs(moveX - downX)&&Math.abs(moveX - downX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return true;
                }
                break;
            default:
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = moveX - downX;

                //右移dx<0; 左移dx>0;
                totalDx += dx;
                //左边界
                if (totalDx <= -mIndiMeasuredWidth) {
                    totalDx = -mIndiMeasuredWidth;
                    mIsClose=false;
                }
                 //右边界
                if (totalDx >= 0) {
                    totalDx = 0;
                    mIsClose=true;
                }
                float percent = totalDx * 1.f / -mIndiMeasuredWidth;
                setLayout(evaluate(percent, 0, -mIndiMeasuredWidth));
                iv.setRotation(evaluate(percent, 360, 180));
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                if (totalDx <= -mIndiMeasuredWidth / 2) {
                    //展开 注：dx大于0往右
                    mScroller.startScroll(Math.round(totalDx), 0, Math.round(-mIndiMeasuredWidth - totalDx), 0, scrollDuration);
                    totalDx=-mIndiMeasuredWidth;
                    mIsClose=false;
                } else {
                    //收缩
                    mScroller.startScroll(Math.round(totalDx), 0, Math.round(-totalDx), 0, scrollDuration);
                    totalDx=0;
                    mIsClose=true;
                }
                invalidate();
                break;
            default:
        }
        return super.onTouchEvent(event);
    }


    /**
     * 计算移动view
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            setLayout(currX);
            float percent = currX * 1.f / (-mIndiMeasuredWidth);
            iv.setRotation(evaluate(percent,360, 180));
            postInvalidate();
        }

    }

    //避免每次创建
    private int evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return (int) (startFloat + fraction * (endValue.floatValue() - startFloat));
    }


    public void setIndicatorText(List<String> textList) {
        if (textList == null || textList.size() <= 0) {
            indicator.setVisibility(GONE);
            iv.setVisibility(GONE);
        } else {
            indicator.setVisibility(VISIBLE);
            iv.setVisibility(VISIBLE);
            indicatorAdapter = new IndicatorAdapter(textList);
            indicator.setAdapter(indicatorAdapter);
        }
    }



    public void setAdapter(BaseAdapter adapter) {
        BaseAdapter lvAdapter = (BaseAdapter) lv.getAdapter();
        if (lvAdapter != null) {
            lvAdapter.notifyDataSetChanged();
        } else {
            if (adapter != null) {
                lv.setAdapter(adapter);
            }
        }
    }


    public void notifyDataSetChanged() {
        BaseAdapter lvAdapter = (BaseAdapter) lv.getAdapter();
        if (lvAdapter != null) {
            lvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }



    class IndicatorAdapter extends BaseAdapter {

        private ArrayList<String> mList;

        public IndicatorAdapter(List<String> list) {
            mList = (ArrayList<String>) list;
        }

        @Override
        public int getCount() {
            return mList!=null?mList.size() : 0;
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_indecetor_text, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            if (position == indicatorSelcPosition) {
                textView.setTextColor(getResources().getColor(R.color.color_ff6602));
            }else{
                textView.setTextColor(getResources().getColor(R.color.color_353535));
            }
            textView.setText(mList.get(position));
            return convertView;
        }
    }





}
