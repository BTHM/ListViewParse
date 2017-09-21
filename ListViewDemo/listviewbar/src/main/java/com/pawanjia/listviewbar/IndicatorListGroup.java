package com.pawanjia.listviewbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

public class IndicatorListGroup extends FrameLayout {


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
    private ObjectAnimator animatorIn;
    private ObjectAnimator animatorOut;
    private int textAreaWidth;
    private Scroller mScroller;
    private ObjectAnimator animatorLeft;
    private ObjectAnimator animatorRight;
    private boolean isDrawed;
    private int loadAnimationDuration = 500;
    private int scrollDuration = 200;
    private IndicatorAdapter indicatorAdapter;
    private int indicatorSelcPosition;
    private  List<String> textList=new ArrayList<>();
    private boolean isClick;

    public IndicatorListGroup(Context context) {
        this(context, null);
    }

    public IndicatorListGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorListGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mLayout = (ViewGroup) layoutInflater.inflate(R.layout.indicator_list_group, this, true);
        rl = (RelativeLayout) mLayout.findViewById(R.id.indicator_rl);
        lv = (ListView) mLayout.findViewById(R.id.indicator_lv);
        indicator = (ListView) mLayout.findViewById(R.id.indicator_list);
        frame = (FrameLayout) mLayout.findViewById(R.id.indicator_frame);
        iv = (ImageView) mLayout.findViewById(R.id.indicator_iv);

        mScroller = new Scroller(getContext());
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    indicatorSelcPosition = firstVisibleItem;
                if (indicatorAdapter != null) {
                    indicatorAdapter.notifyDataSetChanged();
                }
            }
        });

        indicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                isDrawed = true;
                textAreaWidth = indicator.getWidth();
                loadAnim(textAreaWidth);
                closeIndector();
                ObjectAnimator animatorClose = ObjectAnimator.ofFloat(rl, "translationX", 0, textAreaWidth).setDuration(loadAnimationDuration);

                animatorOut.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rl.setVisibility(VISIBLE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                mIsClose=true;
            }
        });
        indicator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indicatorSelcPosition=position;
                isClick=true;
                indicatorAdapter.notifyDataSetChanged();
                lv.setSelection(position);
            }
        });
        frame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsClose) {
                    animatorIn.start();
                    animatorRight.start();
                    mIsClose = false;
                } else {
                    animatorOut.start();
                    animatorLeft.start();
                    mIsClose = true;
                }
            }
        });

    }

    /**
     * 设置关闭导航栏
     */
    public void closeIndector() {
        if (isDrawed) {//绘制完了才能关闭
            animatorOut.start();
            animatorLeft.start();
            mIsClose = true;
            totalDx = textAreaWidth;
        }
    }


    public void loadAnim(int textAreaWidth) {
        //from、to位置是指targat的本身
        animatorOut = ObjectAnimator.ofFloat(rl, "translationX", 0, textAreaWidth);
        animatorOut.setDuration(loadAnimationDuration);

        //to位置是指target 起始和终点位置就是上面位置相反的
        animatorIn = ObjectAnimator.ofFloat(rl, "translationX", textAreaWidth, 0);
        animatorIn.setDuration(loadAnimationDuration);

        animatorLeft = ObjectAnimator.ofFloat(iv, "rotation", 0, 180);
        animatorLeft.setDuration(loadAnimationDuration);

        animatorRight = ObjectAnimator.ofFloat(iv, "rotation", 180, 0);
        animatorRight.setDuration(loadAnimationDuration);
        if (!mIsClose) {//如果绘制完未关闭则关闭
            closeIndector();
        }
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
                if (Math.abs(moveY - downY) < Math.abs(moveX - downX)) {
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
                totalDx += dx;
                //左边界
                if (totalDx <= 0) {
                    totalDx = 0;
                    mIsClose=false;
                }
                //右边界
                if (totalDx >= textAreaWidth) {
                    totalDx = textAreaWidth;
                    mIsClose=true;
                }
                float percent = totalDx * 1.f / textAreaWidth;
                rl.setTranslationX(evaluate(percent, 0, textAreaWidth));
                downX = moveX;
                //旋转箭头
                iv.setRotation(evaluate(percent, 0, 180));
                break;
            case MotionEvent.ACTION_UP:
                if (totalDx <= textAreaWidth / 2) {
                    //展开 注：dx大于0往右
                    mScroller.startScroll(Math.round(totalDx), 0, Math.round(-totalDx), 0, scrollDuration);
                    mIsClose=false;
                } else {
                    //收缩
                    mScroller.startScroll(Math.round(totalDx), 0, Math.round(textAreaWidth - totalDx), 0, scrollDuration);
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
            rl.setTranslationX(currX);
            float percent = currX * 1.f / textAreaWidth;
            iv.setRotation(evaluate(percent, 0, 180));
            postInvalidate();
        }

    }

    //避免每次创建
    private Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
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
        if (animatorOut != null)
            animatorOut.end();
        if (animatorIn != null)
            animatorIn.end();
        if (animatorLeft != null)
            animatorLeft.end();
        if (animatorRight != null)
            animatorRight.end();
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
