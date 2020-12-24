package com.example.taiwanplantapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.taiwanplantapplication.R;

/**
 * 未使用
 */
public class SelectedPhotoActivity extends AppCompatActivity {
    private final int REQUEST_IMG_ALBUMS=2;
    ImageView ivPhoto;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);

        ivPhoto=findViewById(R.id.albums_sel_id);

        //Bundle bundle=getIntent().getExtras();
        //Bitmap bitmap=(Bitmap) bundle.get("data");

        //Bitmap bitmap=getIntent().getParcelableExtra("bitmap");
        //ivPhoto.setImageBitmap(bitmap);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_IMG_ALBUMS && resultCode == RESULT_OK){
            Log.i("--Select Tag--", "Result data");
            Bundle extras=getIntent().getExtras();
            Bitmap photo=(Bitmap) extras.get("data");

            //Bitmap resizedBitmap=BitmapReSize.getImageThumbnail(photo, 300, 600);
            ivPhoto.setImageBitmap(photo);
            /*
            int oldwidth = photo.getWidth();
            int oldheight = photo.getHeight();
            float scaleWidth = 100 / (float) oldwidth;
            float scaleHeight = 100 / (float) oldheight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // create the new Bitmap object
            Bitmap resizedBitmap = Bitmap.createBitmap(photo, 0, 0, oldwidth,
                    oldheight, matrix, true);
            */
        }
    }

    // 開啟相簿
    private void openAlbum(){
        //Intent albumIntent=new Intent(Intent.ACTION_PICK);
        Intent albumIntent=new Intent("android.intent.action.GET_CONTENT");
        albumIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, REQUEST_IMG_ALBUMS);
    }
}
