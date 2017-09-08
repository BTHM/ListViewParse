package com.pawanjia.listviewbar;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> datas=new ArrayList<>();
    private ListView mLv;
    private ListIndicator2 mIndicator;
    private RelativeLayout rl;
    private ImageView iv;
    private boolean mIsClose;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datas.add("0");
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");
        datas.add("5");
        datas.add("6");
        mLv = (ListView) findViewById(R.id.lv);
        mIndicator = (ListIndicator2) findViewById(R.id.list);
        rl = (RelativeLayout) findViewById(R.id.rl);
        iv = (ImageView) findViewById(R.id.iv);
        frame = (FrameLayout) findViewById(R.id.frame);
        MyAdapter myAdapter = new MyAdapter();
        mLv.setAdapter(myAdapter);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("tag","firstVisibleItem="+firstVisibleItem);
                //TODO 设置第一个可见行会导致指示器 最后几个下不去  已解决
                if (firstVisibleItem+visibleItemCount < totalItemCount) {
                    mIndicator.setSelectedPosition(firstVisibleItem);
                }
            }
        });

        mIndicator.setOnTouchListner(new ListIndicator2.OnTouchListner() {
            @Override
            public void onTouch(int position) {
                mLv.setSelection(position);
            }
        });

        final Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        final Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.push_right_out);

        //from、to位置是指targat的本身
        final ObjectAnimator animatorOut = ObjectAnimator.ofFloat(rl, "translationX", 0, mIndicator.getWidth()+200);
        animatorOut.setDuration(500);

        //to位置是指target 起始和终点位置就是上面位置相反的
        final ObjectAnimator animatorIn = ObjectAnimator.ofFloat(rl, "translationX", mIndicator.getWidth()+200,0);
        animatorIn.setDuration(500);

        mIsClose = false;
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsClose) {
                    animatorIn.start();
                    mIsClose=false;
                }else{
                    animatorOut.start();
                    mIsClose=true;
                }

            }
        });
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content,parent,false);
            }
            WebView webview  = (WebView) convertView.findViewById(R.id.webview);
            WebSettings settings = webview.getSettings();
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            webview.loadUrl("http://blog.csdn.net/u012910985/article/details/38397139");
            TextView text  = (TextView) convertView.findViewById(R.id.content_text);
            text.setText(datas.get(position));
            return convertView;
        }
    }

}
