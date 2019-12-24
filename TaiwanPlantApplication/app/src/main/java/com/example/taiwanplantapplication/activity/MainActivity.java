package com.example.taiwanplantapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.fragment.AngiospermFragment;
import com.example.taiwanplantapplication.fragment.FernFragment;
import com.example.taiwanplantapplication.fragment.GymnospermFragment;
import com.example.taiwanplantapplication.fragment.OtherFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RadioGroup radioGroup_main;
    FrameLayout frameLayout_main;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_id, new GymnospermFragment()).commit();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
            startActivity(new Intent(MainActivity.this,AddPlantActivity.class));
        } else if (id == R.id.nav_myData_id) {

        } else if (id == R.id.nav_search_id) {

        } else if (id == R.id.nav_camera_id) {

        } else if (id == R.id.nav_setting_id) {

        } else if (id == R.id.nav_login_id) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //初始化元件
    private void init(){


    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Fragment selectFragment=null;
            switch(checkedId){
                case R.id.rb_gymnosperm_id:
                    selectFragment=new GymnospermFragment();
                    break;
                case R.id.rb_angiosperm_id:
                    selectFragment=new AngiospermFragment();
                    break;
                case R.id.rb_fern_id:
                    selectFragment=new FernFragment();
                    break;
                case R.id.rb_other_id:
                    selectFragment=new OtherFragment();
                    break;
            }
            //進行頁面轉換                                         載入到此frameLayout,建立選擇的頁面
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_id,selectFragment).commit();
        }
    }
}
