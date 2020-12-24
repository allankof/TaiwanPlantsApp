package com.example.taiwanplantapplication.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.adapter.PagerViewAdapter;
import com.example.taiwanplantapplication.bean.InfoData;
import com.example.taiwanplantapplication.en.TypeEn;
import com.example.taiwanplantapplication.model.PhotoDialogFragment;
import com.example.taiwanplantapplication.utils.UtilPath;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 顯示植物的個別資訊與圖片
 */
public class PlantsInfoActivity extends AppCompatActivity {

    private final int REQUEST_IMG_CAPTURE = 1;
    private final int REQUEST_IMG_ALBUMS = 2;
    private final int REQUEST_CODE_LOCATION = 3;
    private ViewPager viewPager;     // 顯示圖片
    private PagerViewAdapter pagerViewAdapter;
    private TextView tvFamily, tvDate, tvNum, tvLocation, tvLng, tvLat, tvDes;
    private String currentPhotoPath;
    private DatabaseReference databaseReference;
    private String type;
    private String id;
    private String name;
    private List<String> pagerPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_info);
        pagerPhotoList=new ArrayList<>();
        //viewList=new ArrayList<>();
        //pagerPhotoList.add("");

        init();
        // 取得recycler傳送的資料(id,name.family...)
        Bundle bundle= getIntent().getExtras();

        type = bundle.getString(TypeEn.TYPE.getCode());      // 取得植物類別
        id = bundle.getString(TypeEn.ID.getCode());           // 取得子節點的id
        name = bundle.getString(TypeEn.NAME.getCode());
        String family = bundle.getString(TypeEn.FAMILY.getCode());
        String location = bundle.getString(TypeEn.LOCATION.getCode());
        String date = bundle.getString(TypeEn.DATE.getCode());
        String num = bundle.getString(TypeEn.Number.getCode());
        String lng = bundle.getString(TypeEn.LNG.getCode());
        String lat = bundle.getString(TypeEn.LAT.getCode());
        String des = bundle.getString(TypeEn.DES.getCode());
        Log.i("Tag_id-1", " "+id);

        //getDatabasePhoto();
        getPhotoPath();

        tvFamily.setText(family);
        tvLocation.setText(location);
        tvDate.setText(date);
        tvNum.setText(num);
        tvLng.setText(lng);
        tvLat.setText(lat);
        tvDes.setText(des);

        // Toolbar設定
        final Toolbar toolbar = findViewById(R.id.info_toolbar_id);
        toolbar.setTitle(name);     // 植物名稱

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      // 左上返回鍵顯示
        getSupportActionBar().setHomeButtonEnabled(true);           // 左上返回鍵可用
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        // 監聽firebase資料
        databaseReference.addValueEventListener(new ValueEventListener() {
            InfoData infoData=new InfoData();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshots:dataSnapshot.getChildren()){
                    infoData=dataSnapshots.getValue(InfoData.class);
                }
                // Attempt to invoke virtual method InfoData.getFamily() on a null object reference
                tvFamily.setText(infoData.getFamily());
                tvDate.setText(infoData.getDate());
                tvTude.setText(infoData.getTude());
                tvLocation.setText(infoData.getLocation());
                tvNum.setText(infoData.getNumber());
                tvDes.setText(infoData.getDes());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_IMG_CAPTURE:
                if(resultCode == RESULT_OK){
                    galleryAddPic();
                    updatePhoto();
                }
                break;
            case REQUEST_IMG_ALBUMS:
                data.setClass(PlantsInfoActivity.this, SelectedPhotoActivity.class);
                setResult(RESULT_OK);
                startActivityForResult(data, REQUEST_IMG_ALBUMS);
                break;
        }

        /*
        if(requestCode == REQUEST_IMG_ABLUMS && resultCode == RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap bitmap=(Bitmap) extras.get("data");

            Intent i=new Intent(PlantsInfoActivity.this, SelectedPhotoActivity.class);
            i.putExtra("bitmap", bitmap);
            startActivity(i);
            //data.setClass(PlantsInfoActivity.this, SelectedPhotoActivity.class);
        }
        */
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);

        return true;
    }

    // Action Bar 監聽
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_camera_id:
                openCamera();
                break;
            case R.id.menu_delete_id:
                //openAlbum();
                //startActivity(new Intent(PlantsInfoActivity.this, SelectedPhotoActivity.class));
                removePlantDate();
                break;
            case R.id.menu_album_id:
                openAlbum();
                break;
            case R.id.homeAsUp:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 監聽更換圖片
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // 初始化元件
    private void init() {
        tvFamily = findViewById(R.id.info_family_id);
        tvDate = findViewById(R.id.info_date_id);
        tvLocation = findViewById(R.id.info_location_id);
        tvNum = findViewById(R.id.info_num_id);
        tvLng = findViewById(R.id.info_tvLng_id);
        tvLat = findViewById(R.id.info_tvLat_id);
        tvDes = findViewById(R.id.info_des_id);

        viewPager = findViewById(R.id.info_viewpager_id);
        //view1=getLayoutInflater().inflate(R.layout.viewpager_item, null);
        //viewList.add(view1);
    }

    // 取得Database的圖片，設置ViewPager
    private void getPhotoPath(){
        databaseReference = FirebaseDatabase.getInstance().getReference(type).child(LoginActivity.inputUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GenericTypeIndicator<List<String>> t=new GenericTypeIndicator<List<String>>() {
                        };
                        pagerPhotoList=dataSnapshot.child(id).child(TypeEn.PHOTO.getCode()).getValue(t);
                        Log.i("Tag_id-2", " "+id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pagerViewAdapter = new PagerViewAdapter(PlantsInfoActivity.this, pagerPhotoList);
                                viewPager.setAdapter(pagerViewAdapter);
                                viewPager.addOnPageChangeListener(changeListener);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // 開啟相機
    private void openCamera() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImgFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.taiwanplantapplication.fileprovider", photoFile);
                //  Uri : context:/images_path/Pictures/.jpg
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);      // MediaStore.EXTRA_OUTPUT=指定儲存Uri的key
                startActivityForResult(picIntent, REQUEST_IMG_CAPTURE);
            } else {
                Toast.makeText(this, "file is null", Toast.LENGTH_SHORT);
            }
        }
    }

    // 開啟相簿
    private void openAlbum() {
        //Intent albumIntent=new Intent(Intent.ACTION_PICK);
        Intent albumIntent = new Intent("android.intent.action.GET_CONTENT");
        albumIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, REQUEST_IMG_ALBUMS);
    }

    // 建立圖片檔案
    private File createImgFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());        // 把時間加進檔名
        String imgFileName = "Plant_" + timeStamp + "_";      // 圖片名稱
        File storageDir = UtilPath.STORAGE_DIR;          // 位置 : /storage/emulated/0/Picture/.jpg
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 將照片資訊更新到圖片庫
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     *在 user/id 的子節點下新增圖片
     */
    private void updatePhoto() {
        List pathList = new ArrayList<>();
        pathList.clear();
        if (currentPhotoPath != null) {
            pathList.add(currentPhotoPath);
            Log.i("Tag_id-3", " "+id);
            databaseReference.child(id).child(TypeEn.PHOTO.getCode()).setValue(pathList);           // user/id/photoPath/img.jpg
        }
    }

    private void updatePhotoByAlbum(){

    }

    // 刪除資料的方法
    private void removePlantDate() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("刪除資料")
                .setMessage("是否刪除此筆資料");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(id).removeValue();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        PlantsInfoActivity.this.finish();
    }

    /*
    private void showAlertDialog(Bitmap bitmap){

        AlertDialog.Builder builder=new AlertDialog.Builder(PlantsInfoActivity.this);
        builder.setTitle("圖片");
        View v=LayoutInflater.from(getApplication()).inflate(R.layout.activity_selected_photo, null, false);
        ivDialog=v.findViewById(R.id.album_select_id);
        ivDialog.setImageBitmap(bitmap);
        builder.setView(ivDialog);
    }

    // 實作DialogFragment
    private void showDialog(Bundle bundle){
        DialogFragment photoFragment=PhotoDialogFragment.newInstance(bundle);
        photoFragment.show(getSupportFragmentManager(), "dialog");
    }
    */
}
