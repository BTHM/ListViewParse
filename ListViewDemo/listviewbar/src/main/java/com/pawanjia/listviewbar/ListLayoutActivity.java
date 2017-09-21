package com.pawanjia.listviewbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

public class ListLayoutActivity extends AppCompatActivity {
    private List<String> datas =new ArrayList<>();
     private List<String> textArray  = new ArrayList();//{"概况", "病因", "临床表现"};//"检查", "诊断", "并发症", "治疗", "预后", "预防", "护理"};
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas.add("0");
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");
        datas.add("5");
        datas.add("6");
        datas.add("7");
        datas.add("8");
        datas.add("9");
        textArray.add("概况");
        textArray.add("病因");
        textArray.add("临床表现");
        textArray.add("检查");
        textArray.add("概况");
        textArray.add("病因");
       // textArray.add("临床表现临床表现");
        textArray.add("检查");
        textArray.add("概况");
       // textArray.add("临床表现临床表现临床表现");
        textArray.add("概况");
//        textArray.add("病因");
//        textArray.add("临床表现");
//        textArray.add("检查");
//        textArray.add("概况");
//        textArray.add("病因");
//        textArray.add("临床表现");
//        textArray.add("检查");
        setContentView(R.layout.activity_layout_list);
        //IndicatorListGroup indicatorGroup2 = (IndicatorListGroup) findViewById(R.id.group6);
        IndicatorListGroup2 indicatorGroup2 = (IndicatorListGroup2) findViewById(R.id.group6);
        indicatorGroup2.setIndicatorText(textArray);
        mScreenWidth = ToolUtils.getScreenDpi(this);
        indicatorGroup2.setAdapter(new MyAdapter());
        //indicatorGroup2.closeIndector();

    }


    class MyAdapter extends BaseAdapter {

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
            TextView text  = (TextView) convertView.findViewById(R.id.content_text);
            WebSettings settings = webview.getSettings();
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            String html="<p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><img src='https://imagetest.pawjzs.com/ueditor/jsp/upload/image/20160505/1462442404183041049.png' alt='1459258881648028134.png'/></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>最近一周，日均急诊量</span><span style='font-size:14px;font-family:'>1000</span><span style='font-size:14px;font-family: 宋体;color:#2B2B2B'>人次左右，最高峰那天达</span><span style='font-size:14px;font-family:'>1150</span><span style='font-size:14px;font-family: 宋体;color:#2B2B2B'>人次</span><span style='font-size: 14px;font-family:'>……”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>记者日前走访广州多家医院了解到，由于最近气温多变等季节性原因，不少医院呼吸科、儿科等门诊人数激增。例如广东省妇幼保健院</span><span style='font-size:14px;font-family:'>3</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>月以来门急诊量均爆满，日均就诊量在</span><span style='font-size:14px;font-family:'>800~1200</span><span style='font-size:14px;font-family: 宋体;color:#2B2B2B'>人次左右，扎堆的患儿将该院</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>挤爆</span><span style='font-size: 14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>，而就诊患儿中有</span><span style='font-size:14px;font-family:'>30%~50%</span><span style='font-size:14px;font-family: 宋体;color:#2B2B2B'>是流感患者。</span></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>每到春季阴雨连绵期，便是儿童流感高发期，孩子父母焦虑期</span><span style='font-size:14px;font-family:'>——</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>孩子感冒发烧，家长只得请假陪孩子上医院，医院人满为患，几天跑下来弄得焦头烂额，相信家长们都少不了这种不佳体验。不独广州，南方不少地方近期都受到流感困扰。潮湿春季，病菌暗生，流感不可避免，人类虽无法改变这一规律，但也不是无所作为、</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>束手就擒</span><span style='font-size:14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>，做好应对至少可以将流感的危害降到最低。一位孩子母亲的亲身体验发现，还有许多防流感细节难以令人满意。</span></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>广州市流感样病例在上周占比</span><span style='font-size:14px;font-family:'>6%</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>后，本周继续上升。有些区疾控中心规定，同一个班级一天发现</span><span style='font-size:14px;font-family:'>3</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>个病例或三天</span><span style='font-size:14px;font-family:'>5</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>个病例，则要求该班幼儿全部回家隔离，以免病毒在班上继续蔓延。对此规定，不同幼儿园执行情况不一。公立幼儿园做得比较到位，出现上述情况时关闭学生午休室，甚至有的封班停课，民办幼儿园则未必有那么</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>听话</span><span style='font-size: 14px;font-family:'>”——</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>对民办园来说，封班意味着</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>欠收</span><span style='font-size: 14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>，主动性不足。</span></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'><img src='https://imagetest.pawjzs.com/ueditor/jsp/upload/image/20160426/1461668292776015543.jpg' title='1461668292776015543.jpg' alt='Koala.jpg'/></span></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>再来说说看病，小病进社区医院，但社区医院问题也不少。医生诊断孩子患了流感，必须在流感病发的</span><span style='font-size:14px;font-family:'>48</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>小时之内服用一些抗滤过性病原体的药物，否则效果就会差很多，如治疗儿童甲型流感的达菲。可是，社区医院往往没有这些药，医生只能说去大医院看吧。家长最后还得回到大医院，若是大医院不认可社区医院的诊断结果，不愿意直接开药，家长只好带着病孩子从头开始走完所有的疾病诊断程序，才开始用药。稍不抓紧就过了</span><span style='font-size:14px;font-family:'>48</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>小时，孩子的</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>手尾</span><span style='font-size: 14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>就长了。</span></p><p style='margin-bottom: 25px;text-indent: 28px;line-height: 24px;background: white'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>带孩子到大医院，还有一番考验与折腾。现在的大医院都进入</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>网络时代</span><span style='font-size:14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>，可以在网上挂号、候诊、交款，很是方便，但一些环节还是停留在</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>手工时代</span><span style='font-size:14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>，如取药。个别医院平时的取药窗口就不多，遇上流感爆发季节更显不足，家长要等很长时间。结果，之前所有的快捷都被卡在这个</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>瓶颈</span><span style='font-size: 14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>里。有位家长抱怨，在取药窗口等待一个半小时才取到。特殊情况特殊处理，在这个特殊季节，医院能不能增派人手，多开几个取药窗口？</span></p><p style='margin-bottom: 25px; text-indent: 28px; line-height: 24px; background: white;'><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>那边厢，学校、医院要</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>以流感为本</span><span style='font-size:14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>；这边厢，患者、家长也要有</span><span style='font-size:14px;font-family:'>“</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>流感自觉</span><span style='font-size:14px;font-family:'>”</span><span style='font-size:14px;font-family:宋体;color:#2B2B2B'>。孩子患上流感，就要把孩子带回家隔离，不要继续上学，以免感染其他孩子。成年患者在公共场合要自觉戴口罩，降低人传人风险。</span></p>";
            String html2="<p><span style='box-sizing: border-box; font-weight: 700;'>养生导读</span>：饺子是冬至不能少的食物之一，但你知道为什么冬至吃饺子吗？冬至吃饺子的由来是为了纪念“医圣”张仲景，具体为什么冬至吃饺子呢？下面小编为您介绍。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>俗话说“冬至饺子夏至面”，到冬至这天，大家可别忘了吃饺子。 冬至这天吃饺子，还说：“冬至饺子<a class='art-inlink' target='_blank' href='https://www.ys137.com/zt/xiazhiys.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>夏至</a>面，谁要不吃亏半年。”那么为什么冬至吃饺子呢？有什么由来呢？下面小编为您介绍。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'><span style='box-sizing: border-box; font-weight: 700;'>为什么冬至吃饺子？</span></p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>每年农历冬至这天，不论贫富，饺子是必不可少的节日饭。谚云：“十月一，冬至到，家家户户吃水饺。”这种习俗，是因纪念“医圣”<a class='art-inlink' target='_blank' href='https://www.ys137.com/zyys/147645.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>张仲景</a>冬至舍药留下的。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>张仲景是南阳稂东人，他着《伤寒杂病论》，集医家之大成，被历代医者奉为经典。张仲景有名言：“进则救世，退则救民；不能为良相，亦当为良医。”东汉时他曾任长沙太守，访病施药，大堂行医。后毅然辞官回乡，为乡邻治病。其返乡之时，正是冬季。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>他看到白河两岸乡亲面黄肌瘦，饥寒交迫，不少人的耳朵都冻烂了。便让其弟子在南阳东关搭起医棚，支起大锅，在冬至那天舍“祛寒娇耳汤”医治冻疮。他把羊肉、辣椒和一些驱寒药材放在锅里熬煮，然后将羊肉、药物捞出来切碎，用面包成耳朵样的“娇耳”，煮熟后，分给来求药的人每人两只“娇耳”，一大碗肉汤。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>人们吃了“娇耳”，喝了“祛寒汤”，浑身暖和，两耳发热，<a class='art-inlink' target='_blank' href='https://www.ys137.com/ysrq/624340.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>冻伤</a>的耳朵都治好了。后人学着“娇耳”的样子，包成食物，也叫“饺子”或“扁食”。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>冬至吃饺子，是不忘“医圣”张仲景“祛寒娇耳汤”之恩。至今南阳仍有“冬至不端饺子碗，冻掉耳朵没人管”的民谣。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'><span style='box-sizing: border-box; font-weight: 700;'>冬至吃饺子，好处多多</span></p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>1、从烹饪科学的角度看，蒸<a class='art-inlink' target='_blank' href='https://www.ys137.com/yinshi/221516.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>煮饺子</a>以水（汽）为介质的烹饪方式，温度只在100左右，即可致熟食物又可消毒杀菌，避免了烧烤炸条件下生成苯并芘等强<a class='art-inlink' target='_blank' href='https://www.ys137.com/xinwen/142622.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>致癌物</a>，保证了食品安全。而且，食物的营养成分在蒸煮过程中也不至于因过氧化或水解而损失。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>2、从膳食结构角度看，饺子的馅料都包在面皮中，可以做到谷类与菜果、肉类的适宜组合，使主副食搭配合理，营养丰富并酸碱平衡，膳食宝塔形结构。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>3、从营养角度看，饺子以水（汽）为传热介质经蒸煮而熟，可以使食品中淀粉类多糖充分裂解，利于人体吸收。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>4、合于中国人的肠胃于<a class='art-inlink' target='_blank' href='https://www.ys137.com/baike/882806.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>饮食习惯</a>。符合“食饮有节”、“谨和五味”、“和于术数”的养生之道。</p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'><img src='https://img.ys137.com/uploads/allimg/141022/3098-14102212023J39.jpg' title='为什么冬至吃饺子？由来大揭秘' alt='为什么冬至吃饺子？由来大揭秘' style='box-sizing: border-box; border: 0px; vertical-align: middle; max-width: 500px; margin: 15px auto; display: block;'/></p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'><span style='box-sizing: border-box; font-weight: 700;'>冬至吃什么饺子？</span></p><p style='box-sizing: border-box; margin-top: 15px; margin-bottom: 15px; text-indent: 2em; color: rgb(51, 51, 51); font-family: 宋体, Arial, Helvetica; line-height: 28px; white-space: normal; background-color: rgb(255, 255, 255);'>饺子馅多种多样，每个人也都有自己的喜好。不过在冬至这天吃的饺子，大多数人会选择羊肉馅的。理由是羊肉性温热，冬至吃可起到温阳作用，帮助机体驱寒，还有温养脏腑的作用。的确，在大<a class='art-inlink' target='_blank' href='https://www.ys137.com/slys/147397.html' style='box-sizing: border-box; color: rgb(46, 160, 57); text-decoration: none; border-bottom-width: 1px; border-bottom-color: rgb(46, 160, 57); border-bottom-style: dotted; background: 0px 0px;'>冬天</a>应适当多吃些温热性的食物，如韭菜、大葱、牛羊肉等动物类食物，有助于温阳保暖，利于养生。不过，若本身就有“上火”的情况，则不宜过度食用温热食物。</p><p><br/></p>";
            String html3="<div class='para'><p><div class='para'><strong>1.肛门视诊</strong></div></p ></div><div class='para'><p>肛门视诊是对肛门周围皮肤有无增厚、红肿、血性、脓性分泌物、皮疹及瘘管等方面进行视诊。可以明确的看到肛门是否有感染等。<div class='para'><strong></strong></div></p ></div><div class='para'><p><div class='para'><strong>2.肛门触诊</strong></div></p ></div><div class='para'><p>初步判断有无其他盆腔疾病。<div class='para'><strong></strong></div></p ></div><div class='para'><p><div class='para'><strong>3.肛门指诊</strong></div></p ></div><div class='para'><p>医生用食指插入肛管，通过触摸肛管及直肠下段可以初步判断有无肛肠疾病。<div class='para'><strong></strong></div></p ></div><div class='para'><p><div class='para'><strong>4.电子肠镜</strong></div></p ></div><div class='para'><p>这是最直观，最清晰的检查方法，被检部位图像可以直接呈现显像仪上。其镜身直径小，可以从肛门处插入，进入肠道内，镜头能多角度、多方位的进行检查。对肛周炎等多种疾病的诊断和治疗有着决定性的作用。</p ></div>";

            if (position == 0) {
                Spanned sp = Html.fromHtml(html3.replace("width","").replace("height",""), new MImageGetter(text,getApplicationContext()), null);
                text.setText(sp);
            }else if (position == 1){
                Spanned sp = Html.fromHtml(html2.replace("width","").replace("height",""), new MImageGetter(text,getApplicationContext()), null);
                text.setText(sp);
            }else{
                webview.loadData(html.replace("width","").replace("height",""),"text/html; charset=UTF-8",null);
            }


            return convertView;
        }
    }

    class MImageGetter implements Html.ImageGetter {
        Context c;
        TextView container;
        private int mImgRight;
        private int mImgBottom;

        public MImageGetter(TextView text,Context c) {
            this.c = c;
            this.container = text;
        }
        public Drawable getDrawable(String source) {
            final LevelListDrawable drawable = new LevelListDrawable();
            Glide.with(c).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if(resource != null) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                        drawable.addLevel(1, 1, bitmapDrawable);
                        if (resource.getWidth() >= mScreenWidth) {
                            mImgRight = mScreenWidth- ToolUtils.dip2px(c,60);
                            mImgBottom = resource.getHeight() * mImgRight / resource.getWidth();
                        }else{
                            mImgRight=resource.getWidth();
                            mImgBottom=resource.getHeight();
                        }
                        drawable.setBounds(0, 0, mImgRight,mImgBottom);
                        drawable.setLevel(1);
                        container.invalidate();
                        container.setText(container.getText());
                    }
                }
            });


            return drawable;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}

