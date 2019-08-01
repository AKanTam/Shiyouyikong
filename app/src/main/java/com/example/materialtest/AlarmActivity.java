package com.example.materialtest;

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
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.materialtest.Alarm.*;



public class AlarmActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    private DrawerLayout mDrawerLayout;//滑动菜单

    private SwipeRefreshLayout swipeRefresh;//刷新逻辑

    private ListView listView;//alarm show list
    private ArrayList<String> sList=new ArrayList<>();
    private SimpleCursorAdapter cursorAdapter;
    private DataBaseOperator dbOpeater;
    private SQLiteDatabase wb;
    private Cursor mCursor;//数据库指针
    private final String TAG="AlarmActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //
        listView=(ListView)findViewById(R.id.alarm_listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        dbOpeater = new DataBaseOperator(this);//数据库对象

        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Toolbar
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);//滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        navView.setCheckedItem(R.id.nav_alarm);//默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();//将滑动菜单关闭
                switch (item.getItemId()){
                    case R.id.nav_alarm:

                        break;
                    case R.id.nav_analyse:
                        Intent intent2 = new Intent(AlarmActivity.this, AnalyseActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_countdown:
                        Intent intent3 = new Intent(AlarmActivity.this, CountdownActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_memorandum:
                        Intent intent4 = new Intent(AlarmActivity.this, MemorandumActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_house:
                        Intent intent5 = new Intent(AlarmActivity.this, MainActivity.class);
                        startActivity(intent5);
                        break;
                }
                //此处可以填写相应的逻辑
                return true;
            }
        });//滑动菜单
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {//浮动按钮点击事件
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AlarmActivity.this,AlarmEditActivity.class);
                startActivity(intent);
                //cursorAdapter.notifyDataSetChanged();

            }
        });//浮动按钮逻辑




        //
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();//本地刷新
            }
        });//下拉刷新逻辑
    }





    @Override
    protected void onResume() {
        super.onResume();
        mCursor=dbOpeater.query(MyDataBaseHelper.ALARM_TB_NAME);//获得alarm的table
        String [] colums = {MyDataBaseHelper.COL_TIME,MyDataBaseHelper.COL_ALARM_STATUS,MyDataBaseHelper.COL_ALARM_REPEAT_TIMES};
        int[] layoutsId = {R.id.alarm_time,R.id.alarm_status,R.id.alarm_repeat_times};
        cursorAdapter=new SimpleCursorAdapter(this,R.layout.alarm_item,mCursor,colums,layoutsId, CursorAdapter.FLAG_AUTO_REQUERY);
        listView.setAdapter(cursorAdapter);
    }
    @Override
    protected void onStop() {
        mCursor.close();
        super.onStop();
    }


    public void onClick(View v) {

        }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent=new Intent(this,AlarmEditActivity.class);
        TextView modify_time= (TextView) view.findViewById(R.id.alarm_time);
        intent.putExtra("time",modify_time.getText());
        intent.putExtra("position",position+1);
        Log.d(TAG,"要修改时间为"+modify_time.getText());
        startActivity(intent);


    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DeleteAlarmActivity.class);
        startActivity(intent);
        return false;
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
                Toast.makeText(this, "你现在点击的是蓝牙同步按钮，但是并没有什么卵用，因为我们现在并没有蓝牙模块", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
        //Toolbar菜单点击事件
    }

}
