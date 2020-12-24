package com.example.taiwanplantapplication.model;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.taiwanplantapplication.R;

/**
 * 未使用
 */
public class PhotoDialogFragment extends DialogFragment {
    private static final String EXTRAS_KEY="bitmap";
    ImageView ivSelectPhoto;
    private String photoPath;
    private Uri uri;
    Bitmap bitmapImg;

    public static PhotoDialogFragment newInstance(Bundle bundle) {
        PhotoDialogFragment fragment = new PhotoDialogFragment();
        //Bundle bundle=new Bundle();
        //bundle.putBundle(EXTRAS_KEY, bundle);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bitmapImg=(Bitmap) getArguments().get("data");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //photoPath=getArguments().getString(EXTRAS_KEY);
        //uri=Uri.parse(photoPath);

       // Log.i("-Dialog path-", photoPath);
        //Log.i("--Dialog uri--", uri.toString());
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_NoActionBar_Fullscreen);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().setCanceledOnTouchOutside(true);
        View v=inflater.inflate(R.layout.dialog_fragment_photo, container, false);
        //View iv=v.findViewById(R.id.album_select_id);
        ivSelectPhoto=v.findViewById(R.id.album_select_id);
        if(bitmapImg!=null){
            ivSelectPhoto.setImageBitmap(bitmapImg);
        }else {
            ivSelectPhoto.setImageResource(R.drawable.back01);
        }
        //((ImageView)iv).setImageBitmap(bitmapImg);
        //LinearLayoutManager manager=new LinearLayoutManager(getApplication(), LinearLayout.VERTICAL, false);
        //ivSelectPhoto.setImageURI(uri);

        return v;
    }
}
