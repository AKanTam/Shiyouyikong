package com.example.materialtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.materialtest.Alarm.MyDataBaseHelper;
import com.example.materialtest.Alarm.*;
import com.example.materialtest.CountdownActivity;
import com.example.materialtest.MemorandumActivity;
import com.example.materialtest.Countdown.*;
import com.example.materialtest.database.MyDB;
import com.example.materialtest.enity.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;//滑动菜单

    private SwipeRefreshLayout swipeRefresh;
    //刷新逻辑
    //

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Button button1 = findViewById(R.id.main_1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, MemorandumActivity.class);
                startActivity(intent4);

            }
        });
        Button button2 = findViewById(R.id.main_2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, CountdownActivity.class);
                startActivity(intent3);

            }
        });
        Button button3 = findViewById(R.id.main_3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AnalyseActivity.class);
                startActivity(intent2);

            }
        });
        Button button4 = findViewById(R.id.main_4);
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent1);

            }
        });

        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Toolbar
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView =  findViewById(R.id.nav_view);//滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_house);//默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            mDrawerLayout.closeDrawers();//将滑动菜单关闭
            switch (item.getItemId()){
                case R.id.nav_alarm:
                    Intent intent1 = new Intent(MainActivity.this, AlarmActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.nav_analyse:
                    Intent intent2 = new Intent(MainActivity.this, AnalyseActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_countdown:
                    Intent intent3 = new Intent(MainActivity.this, CountdownActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_memorandum:
                    Intent intent4 = new Intent(MainActivity.this, MemorandumActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_house:

                    break;
            }
            //此处可以填写相应的逻辑
            return true;
        }
    });//滑动菜单

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();//本地刷新
            }
        });//下拉刷新逻辑
    }
    //

    //


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);//线程沉睡两秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {//切回主线程
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);//刷新事件结束，并隐藏刷新条
                    }
                });
            }
        }).start();
    }//下拉刷新逻辑



}
