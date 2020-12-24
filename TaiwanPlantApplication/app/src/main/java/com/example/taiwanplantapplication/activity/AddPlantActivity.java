package com.example.taiwanplantapplication.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.bean.InfoData;
import com.example.taiwanplantapplication.en.TypeEn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    private final int REQUEST_CODE_LOCATION=1;
    private Spinner spinnerType;        // 選擇type
    private EditText etName, etFamily, etLocation, etDate, etLng, etLat, etNumber;     // 輸入資料
    private TextInputEditText inputDes;
    private Button btnAdd, btnClean;
    private String[] selectType={TypeEn.GYMN.getCode(),TypeEn.ANGI.getCode(),TypeEn.FERN.getCode(),TypeEn.OTHER.getCode()};
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Toolbar toolbar=findViewById(R.id.add_toolbar_id);
        setSupportActionBar(toolbar);

        spinnerType=findViewById(R.id.add_spinner_id);

        etName=findViewById(R.id.add_etPlantName_id);
        etFamily=findViewById(R.id.add_etFamily_id);
        etLocation=findViewById(R.id.add_etLocation_id);
        etDate=findViewById(R.id.add_etDate_id);
        etLng=findViewById(R.id.add_etLng_id);
        etLat=findViewById(R.id.add_etLat_id);
        etNumber=findViewById(R.id.add_etNum_id);
        inputDes=findViewById(R.id.add_inputDes_id);
        btnAdd=findViewById(R.id.add_btn_id);
        btnClean=findViewById(R.id.clean_btn_id);


        selectedType();

        getDay();
        getLocation();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGymn();
            }
        });
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlantActivity.this.finish();
            }
        });
    }

    private void selectedType(){
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i("Tag Type", selectType[position]);
                // 連結指定的database節點
                databaseReference= FirebaseDatabase.getInstance().getReference(selectType[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // 新增資料的方法
    private void addGymn(){
        String name=etName.getText().toString().trim();
        String family=etFamily.getText().toString().trim();
        String location=etLocation.getText().toString().trim();
        String date=etDate.getText().toString().trim();
        String lng=etLng.getText().toString().trim();
        String lat=etLat.getText().toString().trim();
        String num=etNumber.getText().toString().trim();
        String des=inputDes.getText().toString().trim();
        String user=LoginActivity.inputUser;

        if (!TextUtils.isEmpty(name)){
            // 建立一個子節點並取得id
            String id=databaseReference.push().getKey();
            // 把輸入的資料加到bean
            InfoData infoData=new InfoData(id, name, family, location, date, lng, lat, num, des);
            // 寫入資料到指定的子節點
            databaseReference.child(user).child(id).setValue(infoData);
            // 確認視窗
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("新增完成");
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //etName.clearComposingText();
                    //etFamily.clearComposingText();
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {
            Toast.makeText(this,"請輸入名稱", Toast.LENGTH_SHORT).show();
        }
    }

    // 取得經緯度
    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(2000);

        FusedLocationProviderClient client = new FusedLocationProviderClient(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // ACCESS_FINE_LOCATION = 精確位置
        int i = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);

        }else {
            if (!gps) {
                Toast.makeText(this, "GPS 未開啟", Toast.LENGTH_LONG).show();
                // 開啟GPS設定的頁面
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                private double longitude;
                private double latitude;

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.getResult() != null) {
                        latitude = task.getResult().getLatitude();
                        longitude = task.getResult().getLongitude();

                        List<Address> fromLocation = null;
                        try {
                            fromLocation = new Geocoder(getApplicationContext()).getFromLocation(latitude, longitude, 1);
                            for (Address address : fromLocation) {
                                //Log.d(TAG, "onComplete: " + address.getAddressLine(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        latitude = 0;
                        longitude = 0;
                    }

                    String lng=String.valueOf(latitude);
                    String lat=String.valueOf(longitude);
                    etLng.setText(lng);
                    etLat.setText(lat);
                }
            });
        }
    }

    // 取得今天的日期
    private void getDay(){
        String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        etDate.setText(date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddPlantActivity.this.finish();
    }

    // 清除輸入框文字
    private void cleanInput(){
        //etName.clearComposingText();
        //etFamily.clearComposingText();
    }
}
