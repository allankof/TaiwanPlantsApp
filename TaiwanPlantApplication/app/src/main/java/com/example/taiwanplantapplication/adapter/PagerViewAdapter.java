package com.example.taiwanplantapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.utils.BitmapReSize;

import java.util.ArrayList;
import java.util.List;

public class PagerViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageView imageViewItem;
    private ArrayList<View> viewList;
    private List<String> pathList;

    public PagerViewAdapter(Context context, ArrayList<View> views, List<String> list) {
        this.context = context;
        this.viewList = views;
        this.pathList = list;
    }

    public PagerViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.pathList = list;
    }

    @Override
    public int getCount() {
        //Log.i("=Tag PageView=", "path : "+pathList.size());
        int count=0;
        if(pathList!=null && (!pathList.isEmpty())){
            count=pathList.size();
        }
        return count;
    }

    // 是否由物件生成
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    // 實例化ViewPager
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.viewpager_item, container, false);
        imageViewItem= view.findViewById(R.id.vp_imgItem_id);

        //imageViewItem.setImageURI(uriPhoto);
        //imageViewItem.setImageResource(R.drawable.back01);
        if (!pathList.isEmpty()){
            String path=pathList.get(position);
            Uri uriPhoto=Uri.parse(path);
            Log.i("=Tag PageView=", path);
            Bitmap bitmap=BitmapReSize.getImageThumbnail(path, 256, 512);
            imageViewItem.setImageBitmap(bitmap);
        }else {
            imageViewItem.setImageResource(R.drawable.ic_waite_refresh_24);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
