package com.example.materialtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.materialtest.database.MyDB;
import com.example.materialtest.enity.Record;
import com.example.materialtest.util.DateFormatType;
import com.example.materialtest.util.MyFormat;



public class MemorandumActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    MyDB myDB;
    private DrawerLayout mDrawerLayout;
    private ListView myListView;
    private MyBaseAdapter myBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorandum);
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        navView.setCheckedItem(R.id.nav_memorandum);//默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();//将滑动菜单关闭
                switch (item.getItemId()) {
                    case R.id.nav_alarm:
                        Intent intent1 = new Intent(MemorandumActivity.this, AlarmActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_analyse:
                        Intent intent2 = new Intent(MemorandumActivity.this, AnalyseActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_countdown:
                        Intent intent3 = new Intent(MemorandumActivity.this, CountdownActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_memorandum:

                        break;
                    case R.id.nav_house:
                        Intent intent5 = new Intent(MemorandumActivity.this, MainActivity.class);
                        startActivity(intent5);
                        break;
                }
                //此处可以填写相应的逻辑
                return true;
            }
        });//滑动菜单



    }

    //初始化控件
    private void init(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        myListView = findViewById(R.id.list_view);

        List<Record> recordList = new ArrayList<>();
        myDB = new MyDB(this);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.query(MyDB.TABLE_NAME_RECORD,null,
                null,null,null,
                null,MyDB.NOTICE_TIME+","+MyDB.RECORD_TIME+" DESC");
        if(cursor.moveToFirst()){
            Record record;
            while (!cursor.isAfterLast()){
                record = new Record();
                record.setId(
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(MyDB.RECORD_ID))));
                record.setTitleName(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TITLE))
                );
                record.setTextBody(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_BODY))
                );
                record.setCreateTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TIME)));
                record.setNoticeTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.NOTICE_TIME)));
                recordList.add(record);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        // 创建一个Adapter的实例
        myBaseAdapter = new MyBaseAdapter(this,recordList,R.layout.memorandum_item);
        myListView.setAdapter(myBaseAdapter);
        // 设置点击监听
        myListView.setOnItemClickListener(this);
        myListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
                Intent intent = new Intent(MemorandumActivity.this, MemorandumEditActivity.class);
                startActivity(intent);
                MemorandumActivity.this.finish();
        }

    //Toolbar点击事件
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
                Toast.makeText(this, "你现在点击的是蓝牙同步按钮，但是并没有什么卵用，因为我们现在并没有蓝牙模块", Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
        //Toolbar菜单点击事件
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MemorandumActivity.this, MemorandumAmendActivity.class);
        Record record = (Record) myListView.getItemAtPosition(position);
        intent.putExtra(MyDB.RECORD_TITLE,record.getTitleName().trim());
        intent.putExtra(MyDB.RECORD_BODY,record.getTextBody().trim());
        intent.putExtra(MyDB.RECORD_TIME,record.getCreateTime().trim());
        intent.putExtra(MyDB.RECORD_ID,record.getId().toString().trim());
        if (record.getNoticeTime()!=null) {
            intent.putExtra(MyDB.NOTICE_TIME, record.getNoticeTime().trim());
        }
        this.startActivity(intent);
        MemorandumActivity.this.finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record) myListView.getItemAtPosition(position);
        showDialog(record,position);
        return true;
    }

    void showDialog(final Record record,final int position){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(MemorandumActivity.this);
        dialog.setTitle("是否删除？");
        String textBody = record.getTextBody();
        dialog.setMessage(
                textBody.length()>150?textBody.substring(0,150)+"...":textBody);
        dialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDB.getWritableDatabase();
                        db.delete(MyDB.TABLE_NAME_RECORD,
                                MyDB.RECORD_ID +"=?",
                                new String[]{String.valueOf(record.getId())});
                        db.close();
                        myBaseAdapter.removeItem(position);
                        myListView.post(new Runnable() {
                            @Override
                            public void run() {
                                myBaseAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }



    /**
     * ListView展示的适配器类
     */
    class MyBaseAdapter extends BaseAdapter{
        private List<Record> recordList;//数据集合
        private Context context;
        private int layoutId;

        MyBaseAdapter(Context context, List<Record> recordList, int layoutId){
            this.context = context;
            this.recordList = recordList;
            this.layoutId = layoutId;
        }

        @Override
        public int getCount() {
            if (recordList!=null&&recordList.size()>0)
                return recordList.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (recordList!=null&&recordList.size()>0)
                return recordList.get(position);
            else
                return null;
        }

        void removeItem(int position){
            this.recordList.remove(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.memorandum_item, parent,
                        false);
                viewHolder  = new ViewHolder();
                viewHolder.titleView = convertView.findViewById(R.id.memorandum_item_title);
                viewHolder.bodyView = convertView.findViewById(R.id.memorandum_item_body);
                viewHolder.timeView = convertView.findViewById(R.id.memorandum_item_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Record record = recordList.get(position);
            String tile = record.getTitleName();
            viewHolder.titleView.setText((position+1)+"."+(tile.length()>7?tile.substring(0,7)+"...":tile));
//            viewHolder.titleView.setText(tile);
            String body = record.getTextBody();
            viewHolder.bodyView.setText(body.length()>13?body.substring(0,12)+"...":body);
//            viewHolder.bodyView.setText(body);
            String createTime = record.getCreateTime();
            Date date = MyFormat.myDateFormat(createTime,DateFormatType.NORMAL_TIME);
            viewHolder.timeView.setText(MyFormat.getTimeStr(date));
            return convertView;
        }
    }

    /**
     * ListView里的组件包装类
     */
    class ViewHolder{
        TextView titleView;
        TextView bodyView;
        TextView timeView;
    }

}


