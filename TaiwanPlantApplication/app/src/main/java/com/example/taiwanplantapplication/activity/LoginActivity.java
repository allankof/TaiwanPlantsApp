package com.example.taiwanplantapplication.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.bean.Register;
import com.example.taiwanplantapplication.utils.UtilPath;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText etName,etPass;
    private Button btnLogin, btnToReg;
    private DatabaseReference databaseReference;
    public static String inputUser="";        // 輸入的帳號
    private String inputPass;               // 輸入的密碼
    private List<Register> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        //Intent intent=new Intent();
        //String id=intent.getStringExtra(UserEn.ID.getCode());
        //String user=intent.getStringExtra(UserEn.NAME.getCode());
        //intent.getStringExtra(UserEn.PASS.getCode());
        //intent.getStringExtra(UserEn.EMAIL.getCode());

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        usersList=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();
                for(DataSnapshot dataSnapshots:dataSnapshot.getChildren()){
                    Register register=dataSnapshots.getValue(Register.class);
                    usersList.add(register);        // 使用者清單
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void init(){
        etName=findViewById(R.id.login_etName_id);
        etPass=findViewById(R.id.login_etPass_id);
        btnLogin=findViewById(R.id.login_btn_id);
        btnToReg=findViewById(R.id.toReg_btn_id);
    }

    // 使用者登入
    private void signIn(){
        inputUser=etName.getText().toString().trim();       // 輸入的帳號
        inputPass=etPass.getText().toString().trim();       // 輸入的密碼
        Iterator<Register> it=usersList.iterator();
        Boolean isSuccess=false;
        Register r;
        String user;
        String pw;
        while(it.hasNext()){
            r=it.next();
            user=r.getUser();
            pw=r.getPass();
            if(inputUser.equals(user) && inputPass.equals(pw)){
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                isSuccess=true;
                break;
            }
        }
        if(isSuccess){
            LoginActivity.this.finish();
        }else {
            Toast.makeText(LoginActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
        }
    }
}
