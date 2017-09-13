package com.pawanjia.listviewbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Description
 *
 * @author liupeng502
 * @data 2017/9/8
 */

public class ListIndicatorGroup2 extends FrameLayout {


    private  boolean mIsClose;
    private  ViewDragHelper.Callback callBack;
    private RelativeLayout rl;
    private ViewGroup mLayout;
    private ListView lv;
    private ListIndicator indicator;
    private FrameLayout frame;
    private ImageView iv;


    private ObjectAnimator animatorIn;
    private  ObjectAnimator animatorOut;
    private ObjectAnimator animatorLeft;
    private ObjectAnimator animatorRight;

    public ListIndicatorGroup2(Context context) {
        this(context, null);
    }

    public ListIndicatorGroup2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListIndicatorGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mLayout = (ViewGroup) layoutInflater.inflate(R.layout.activity_viewgroup, this, true);
        rl = (RelativeLayout) mLayout.findViewById(R.id.indicator_rl);
        lv = (ListView) mLayout.findViewById(R.id.indicator_lv);
        indicator = (ListIndicator) mLayout.findViewById(R.id.indicator_list);
        frame = (FrameLayout) mLayout.findViewById(R.id.indicator_frame);
        iv = (ImageView) mLayout.findViewById(R.id.indicator_iv);


        //lv.setAdapter(new MyAdapter());
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("tag","firstVisibleItem="+firstVisibleItem);
                if (firstVisibleItem+visibleItemCount < totalItemCount) {
                    indicator.setSelectedPosition(firstVisibleItem);
                }
            }
        });

        indicator.setOnTouchListner(new ListIndicator.OnTouchListner() {
            @Override
            public void onTouch(int position) {
                Log.d("tag","position="+position);
                lv.setSelection(position);
            }
        });

        indicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int textAreaWidth = indicator.getTextAreaWidth();
                loadAnim(textAreaWidth);
            }
        });

        mIsClose = false;
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsClose) {
                    animatorIn.start();
                    animatorRight.start();
                    mIsClose=false;
                }else{
                    animatorOut.start();
                    animatorLeft.start();
                    mIsClose=true;
                }
            }
        });
    }

    public void loadAnim(int textAreaWidth){
        //from、to位置是指targat的本身
        animatorOut = ObjectAnimator.ofFloat(rl, "translationX", 0, textAreaWidth);
        animatorOut.setDuration(500);

        //to位置是指target 起始和终点位置就是上面位置相反的
        animatorIn = ObjectAnimator.ofFloat(rl, "translationX", textAreaWidth,0);
        animatorIn.setDuration(500);

        animatorLeft = ObjectAnimator.ofFloat(iv, "rotation", 0,180);
        animatorLeft.setDuration(500);

        animatorRight = ObjectAnimator.ofFloat(iv, "rotation", 180,0);
        animatorRight.setDuration(500);

    }



    public void setAdapter(BaseAdapter adapter){
        BaseAdapter lvAdapter = (BaseAdapter) lv.getAdapter();
        if (lvAdapter != null) {
            lvAdapter.notifyDataSetChanged();
        }else{
            lv.setAdapter(adapter);
        }

    }

}
