package com.pingan.listviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author：liupeng on 2016/12/27 15:41
 * Address：liupeng264@pingan.com.cn
 * 使用tagflag 实现异步加载而不会乱
 */
public class ImageAdapter2 extends ArrayAdapter<String> {

    private LruCache<String, BitmapDrawable> mLruCache;
    private ListView                         mListview;

    public ImageAdapter2(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        mLruCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    /*public ImageAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        mLruCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*增加 findViewwithFlag */
        if (mListview == null) {
            mListview = (ListView) parent;
        }
        String url = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item, null);
        } else {
            view = convertView;
        }
        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);
        image.setTag(url); //设置tag标止
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if (drawable != null) {
            image.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(image);
            task.execute(url);
        }
        return view;
    }


    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key      LruCache的键，这里传入图片的URL地址。
     * @param drawable LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (key == null) {
            mLruCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的BitmapDrawable对象，或者null。
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mLruCache.get(key);
    }


    /**
     * 建立HTTP请求，并获取Bitmap对象。
     *
     * @param
     * @return 解析后的Bitmap对象
     */
    private class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        private ImageView mImageView;
        private String mImageUrl;

        public BitmapWorkerTask(ImageView imageView) {
            super();
            //mImageView = imageView;
        }

        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            mImageUrl = strings[0];
            Bitmap bitmap = downloadBitmap(mImageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(mImageUrl, drawable);
            return drawable;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            // 获取标志
            ImageView imageview = (ImageView) mListview.findViewWithTag(mImageUrl);
            if (imageview != null && drawable != null) {
                imageview.setImageDrawable(drawable);
            }
        }

        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }
    }
}
