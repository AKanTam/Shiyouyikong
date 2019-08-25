package com.example.materialtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalyseActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;//滑动菜单

    private SwipeRefreshLayout swipeRefresh;
    //刷新逻辑
    private MemorandumActivity.MyBaseAdapter myBaseAdapter;
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Toolbar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);//滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_analyse);//默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();//将滑动菜单关闭
                switch (item.getItemId()){
                    case R.id.nav_alarm:
                        Intent intent1 = new Intent(AnalyseActivity.this, AlarmActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_analyse:

                        break;
                    case R.id.nav_countdown:
                        Intent intent3 = new Intent(AnalyseActivity.this, CountdownActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_memorandum:
                        Intent intent4 = new Intent(AnalyseActivity.this, MemorandumActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_house:
                        Intent intent5 = new Intent(AnalyseActivity.this, MainActivity.class);
                        startActivity(intent5);
                        break;
                }
                //此处可以填写相应的逻辑
                return true;
            }
        });//滑动菜单
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {//浮动按钮点击事件
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data refresh", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AnalyseActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                            }//undo点击事件
                        })
                        .show();
            }
        });//浮动按钮逻辑


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

    //Toolbar 菜单点击事件
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();//获得蓝牙适配器
                if (blueadapter == null) {     Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                    // device doesn't support Bluetooth
                }else{
                    if (!blueadapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }}
                   break;
            default:
        }
        return true;
        //Toolbar菜单点击事件
    }

}
