package com.pawanjia.listviewbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        text = (TextView) findViewById(R.id.text);
        String str="由于您的账号数据异常，现在无法操作，如有疑问，请联系客服热线：4001028120";
        SpannableString span=new SpannableString(str);
        span.setSpan(new ClickableSpan(){
            @Override
            public void updateDrawState(TextPaint ds){
                super.updateDrawState(ds);
                //ds.setTextSize(35);//设置字体大小
                //ds.setFakeBoldText(true);//设置粗体
                //ds.setColor(Color.argb(255,38,157,241));//设置字体颜色
                ds.setUnderlineText(false);//设置取消下划线
            }
            @Override
            public void onClick(View widget){
                //添加点击事件
                Log.d("tag","wobeidianji");
            }
        }, str.length()-10, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.append(span);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
