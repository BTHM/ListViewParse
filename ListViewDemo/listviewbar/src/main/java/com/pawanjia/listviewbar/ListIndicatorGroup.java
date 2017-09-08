package com.pawanjia.listviewbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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

public class ListIndicatorGroup extends ViewGroup {


    private RelativeLayout rl;
    private RelativeLayout mLayout;
    private ListView lv;
    private ListIndicator2 indicator;
    private FrameLayout frame;
    private ImageView iv;

    public ListIndicatorGroup(Context context) {
        this(context, null);
    }

    public ListIndicatorGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListIndicatorGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mLayout = (RelativeLayout) layoutInflater.inflate(R.layout.activity_viewgroup, this, false);
        rl = (RelativeLayout) mLayout.findViewById(R.id.indicator_rl);
        lv = (ListView) mLayout.findViewById(R.id.indicator_lv);
        indicator = (ListIndicator2) mLayout.findViewById(R.id.indicator_list);
        frame = (FrameLayout) mLayout.findViewById(R.id.indicator_frame);
        iv = (ImageView) mLayout.findViewById(R.id.indicator_iv);


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }


}
