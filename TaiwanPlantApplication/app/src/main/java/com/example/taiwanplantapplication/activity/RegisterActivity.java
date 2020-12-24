package com.example.taiwanplantapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.bean.Register;
import com.example.taiwanplantapplication.en.UserEn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserName, etPass, etPass2, etEmail;
    private Button btnRegister;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        init();

        // 按鈕註冊帳號跳到登入頁面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=etUserName.getText().toString().trim();
                String pass=etPass.getText().toString().trim();
                String pass2=etPass2.getText().toString().trim();
                String email=etEmail.getText().toString().trim();
                // 檢查輸入的資料不為空
                if(!TextUtils.isEmpty(userName)){
                    if(pass.equals(pass2)){
                        // 建立一個子節點並取得id
                        String id=databaseReference.push().getKey();
                        // 把輸入的資料加到bean
                        Register reg=new Register(id, userName, pass, email);
                        // 將註冊資料寫入database
                        databaseReference.child(id).setValue(reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "註冊完成", Toast.LENGTH_SHORT).show();
                            }
                        });
                        RegisterActivity.this.finish();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "請輸入名稱", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(){
        etUserName=findViewById(R.id.reg_etUser_id);
        etPass=findViewById(R.id.reg_etPass_id);
        etPass2=findViewById(R.id.reg_etPass2_id);
        etEmail=findViewById(R.id.reg_etEmail_id);
        btnRegister=findViewById(R.id.reg_btn_id);
    }
}
