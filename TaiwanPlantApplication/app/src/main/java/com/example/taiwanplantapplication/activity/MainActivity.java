package com.example.taiwanplantapplication.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.fragment.AngiospermFragment;
import com.example.taiwanplantapplication.fragment.FernFragment;
import com.example.taiwanplantapplication.fragment.GymnospermFragment;
import com.example.taiwanplantapplication.fragment.OtherFragment;
import com.example.taiwanplantapplication.en.TypeEn;
import com.example.taiwanplantapplication.utils.UtilPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_IMG_CAPTURE=1;
    private String currentPhotoPath;        // 圖片絕對路徑
    RadioGroup radioGroup_main;
    FrameLayout frameLayout_main;

    String type;        // 植物類別

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        radioGroup_main=findViewById(R.id.rb_group_id);
        radioGroup_main.check(R.id.rb_gymnosperm_id);
        radioGroup_main.setOnCheckedChangeListener(new RadioGroupListener());
        //預設載入的頁面
        type=TypeEn.GYMN.getCode();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_id, new GymnospermFragment()).commit();

        // 側拉開關
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestPermission();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            exitProgress();
        }
    }

    // Setting Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // action bar 監聽
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // 側拉選單
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_id) {
            startActivity(new Intent(MainActivity.this, AddPlantActivity.class));
        } else if (id == R.id.nav_myData_id) {

        } else if (id == R.id.nav_search_id) {

        } else if (id == R.id.nav_camera_id) {
            //requestPermission();
            openCamera();
        } else if (id == R.id.nav_setting_id) {

        } else if (id == R.id.nav_login_id) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 處理拍照回傳的圖片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            galleryAddPic();
    }

    // 請求權限
    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= 23) {

            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(getApplicationContext(), mPermissionList)) {

            } else {
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "儲存圖片需要讀取sd卡的許可權",  //提示
                        REQUEST_IMG_CAPTURE,                       //請求碼 REQUEST_CODE_SAVE_IMG
                        mPermissionList                            //許可權列表
                );
            }
        } else {

        }
    }

    //初始化元件
    private void init(){


    }

    // 切換fragment
    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Fragment selectFragment=null;
            switch(checkedId){
                case R.id.rb_gymnosperm_id:
                    //type=TypeEn.GYMN.getCode();
                    selectFragment=new GymnospermFragment();
                    break;
                case R.id.rb_angiosperm_id:
                    //type=TypeEn.ANGI.getCode();
                    selectFragment=new AngiospermFragment();
                    break;
                case R.id.rb_fern_id:
                    //type=TypeEn.FERN.getCode();
                    selectFragment=new FernFragment();
                    break;
                case R.id.rb_other_id:
                    //type=TypeEn.OTHER.getCode();
                    selectFragment=new OtherFragment();
                    break;
            }
            //進行頁面轉換                                         載入到此frameLayout,建立選擇的頁面
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_id,selectFragment).commit();
        }
    }

    // 開啟相機
    private void openCamera(){
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
                //  Uri : context:/images_path/Pictures/.jpg
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);      // MediaStore.EXTRA_OUTPUT=指定儲存Uri的key
                startActivityForResult(picIntent, REQUEST_IMG_CAPTURE);
            }else {
                Toast.makeText(this, "file is null", Toast.LENGTH_SHORT);
            }
        }
    }

    // 建立圖片檔案
    private File createImgFile() throws IOException{
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());        // 把時間加進檔名
        String imgFileName="Plant_"+timeStamp+"_";      // 圖片名稱
        File storageDir=UtilPath.STORAGE_DIR;          // 位置 : /storage/emulated/0/Picture/.jpg
        File image=File.createTempFile(imgFileName, ".jpg", storageDir);
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

    // 離開App
    private void exitProgress(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("結束應用程式")
                .setMessage("是否離開");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
