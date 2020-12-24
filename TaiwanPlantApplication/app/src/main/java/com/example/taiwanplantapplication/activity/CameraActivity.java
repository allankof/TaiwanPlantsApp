package com.example.taiwanplantapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.utils.UtilPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * 未使用
 */
public class CameraActivity extends AppCompatActivity {

    private final int REQUEST_IMG_CAPTURE=1;
    private ImageView imgViewCamera;
    private Button btnCancel, btnOK;
    private String currentPhotoPath;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();
        takePicIntent();
        //openCamera();
        //LayoutInflater inflater=CameraActivity.this.getLayoutInflater();
    }

    // 取得縮圖
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMG_CAPTURE && resultCode == RESULT_OK){

            Bundle extras=data.getExtras();
            imgBitmap=(Bitmap) extras.get("data");
            imgViewCamera.setImageBitmap(imgBitmap);
            //setPic();
        }

        // 點擊儲存圖片,請求儲存權限
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                //saveImage(getApplicationContext(), imgBitmap);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraActivity.this.finish();
            }
        });
        //Log.d("camera_picture", "儲存完成");
    }

    // Dialog 是否開啓相機
    private void openCamera() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_msg);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePicIntent();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    // 開啓相機的方法
    private void takePicIntent(){
        Intent picIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager()) != null){

            File photoFile=null;
            try {
                photoFile = createImgFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photoFile != null){
                Uri photoUri= FileProvider.getUriForFile(this, "com.example.taiwanplantapplication.fileprovider", photoFile);
                //Log.i("Uri_Photo", photoUri.getPath());       //  Uri : context:/images_path/Pictures/.jpg
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);      // MediaStore.EXTRA_OUTPUT=指定儲存Uri的key
                startActivityForResult(picIntent, REQUEST_IMG_CAPTURE);
                //Toast.makeText(this, "file not null", Toast.LENGTH_SHORT);
            }else {
                Toast.makeText(this, "file is null", Toast.LENGTH_SHORT);
            }
        }
    }

    // 建立圖片
    private File createImgFile() throws IOException{
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());        // 把時間加進檔名
        String imgFileName="Plant_"+timeStamp+"_";      // 圖片名稱
        File storageDir=UtilPath.STORAGE_DIR;          // 位置 : /storage/emulated/0/Picture/.jpg
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir=Environment.getExternalStorageDirectory();
        File image=File.createTempFile(imgFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();             // 圖片絕對路徑
        //Log.i("Picture_Path", currentPhotoPath);
        return image;
    }

    // 建立元件
    private void init(){
        imgViewCamera=findViewById(R.id.camera_img_id);
        btnCancel=findViewById(R.id.camera_btn_cancel_id);
        btnOK=findViewById(R.id.camera_btn_ok_id);
    }

    // 請求權限
    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            //讀取sd卡的許可權
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(getApplicationContext(), mPermissionList)) {
                //已經同意過
                saveImage(getApplicationContext(), imgBitmap);
            } else {
                //未同意過,或者說是拒絕了，再次申請許可權
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "儲存圖片需要讀取sd卡的許可權",      //提示
                        REQUEST_IMG_CAPTURE,                        //請求碼 REQUEST_CODE_SAVE_IMG
                        mPermissionList                             //許可權列表
                );
            }
        } else {
            saveImage(getApplicationContext(), imgBitmap);
        }
    }

    // 儲存圖片的方法
    private void saveImage(Context context, Bitmap bitmap){

        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());        // 把時間加進檔名
        String imgFileName="Plant_"+timeStamp+"_"+".jpg";      // 圖片名稱
        File imgDir=new File(UtilPath.STORAGE_PATH);

        if(!imgDir.exists()){
            imgDir.mkdir();
        }
        File imageFile=new File(imgDir, imgFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            boolean isSuccess=bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            Uri photoUri=Uri.fromFile(imageFile);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photoUri));

            if(isSuccess){
                Toast.makeText(this, "儲存完成", Toast.LENGTH_SHORT);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 調整顯示圖片
    private void setPic() {
        // 獲取你在actiivity_layout地方的ImageView大小
        int targetW = imgViewCamera.getWidth();
        int targetH = imgViewCamera.getHeight();

        //獲取剛剛拍照圖片的大小
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //修改你要顯示圖片的尺寸大小
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        //使用Bitmap size去調整ImageView內顯示的圖片
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        imgBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        //顯示影像
        imgViewCamera.setImageBitmap(imgBitmap);
    }
}
