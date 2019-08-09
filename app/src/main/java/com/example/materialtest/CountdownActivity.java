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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.CountDownTimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


//
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.materialtest.Countdown.RecyclerView.MyAdapter;
import com.example.materialtest.Countdown.RecyclerView.NewDateClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.materialtest.Countdown.RecyclerView.MyAdapter;
import com.example.materialtest.Countdown.RecyclerView.NewDateClass;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import es.dmoral.toasty.Toasty;


//
public class CountdownActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;//滑动菜单

    private SwipeRefreshLayout swipeRefresh;
    //刷新逻辑


    //
    private FloatingActionButton fab;
    public Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<NewDateClass> newDates;

    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        //
        fab = findViewById(R.id.fab);



        loadData();
        buildRecyclerView();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDateDialog();
            }});




        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Toolbar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);//滑动菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_countdown);//默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();//将滑动菜单关闭
                switch (item.getItemId()){
                    case R.id.nav_alarm:
                        Intent intent1 = new Intent(CountdownActivity.this, AlarmActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_analyse:
                        Intent intent2 = new Intent(CountdownActivity.this, AnalyseActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_countdown:

                        break;
                    case R.id.nav_memorandum:
                        Intent intent4 = new Intent(CountdownActivity.this, MemorandumActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_house:
                        Intent intent5 = new Intent(CountdownActivity.this, MainActivity.class);
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
                newDateDialog();

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

    //
    public void buildRecyclerView(){

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(newDates, CountdownActivity.this);
        recyclerView.setAdapter(adapter);
    }

    public void newDateDialog(){

        //creation and display AlertDialog
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CountdownActivity.this);
        View viewDialog = LayoutInflater.from(CountdownActivity.this).inflate(R.layout.new_date_dialog, null);
        dialogBuilder.setView(viewDialog);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        //elements in AlertDialog view
        final EditText editTextTitle = (EditText) alertDialog.findViewById(R.id.edit_text_title);
        final EditText editTextDate = (EditText) alertDialog.findViewById(R.id.edit_text_date);
        final EditText editTextDescription = (EditText) alertDialog.findViewById(R.id.edit_text_description);
        final Button buttonCancel = (Button) alertDialog.findViewById(R.id.button_cancel);
        final Button buttonSet = (Button) alertDialog.findViewById(R.id.button_set);

        chooseDate(editTextDate);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    checkSetButton(editTextDate, editTextTitle, editTextDescription, alertDialog);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void chooseDate(final EditText editTextDate){

        final  Calendar myCalendar = Calendar.getInstance();

        final  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy");
        long currentDate = System.currentTimeMillis();
        String dateStringCalendar = simpleDateFormat.format(currentDate);
        editTextDate.setText(dateStringCalendar);


        final DatePickerDialog.OnDateSetListener  date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                editTextDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        };


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CountdownActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void addNewRecycler(String stringTitle, String stringDate, String stringDescription) {

        NewDateClass date =  new NewDateClass(stringTitle, stringDate, stringDescription);
        newDates.add(0, date);
        adapter.notifyDataSetChanged();

        saveData();
    }

    public void loadData() {

        SharedPreferences mPrefs = getSharedPreferences("objects", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("myJson", null);
        Type type = new TypeToken<ArrayList<NewDateClass>>() {}.getType();
        newDates = gson.fromJson(json, type);

        if(newDates == null){
            newDates = new ArrayList<>();}
    }

    private void saveData(){

        SharedPreferences mPrefs = getSharedPreferences("objects", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newDates);
        prefsEditor.putString("myJson", json);
        prefsEditor.apply();

    }

    private boolean dateIsOk(EditText editTextDate) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date today = Calendar.getInstance().getTime();
        String todayDateS = simpleDateFormat.format(today);
        Date todayDate = simpleDateFormat.parse(todayDateS);
        Date futureDate = simpleDateFormat.parse(editTextDate.getText().toString());

        boolean dateIsOk = false;

        if(todayDate.before(futureDate)){
            dateIsOk = true;
        }else if(todayDate.after(futureDate)){
            dateIsOk = false;
        }else if(todayDate.equals(futureDate)){
            dateIsOk = true;
        }

        return dateIsOk;
    }

    private void checkSetButton(EditText editTextDate,EditText editTextTitle, EditText editTextDescription, AlertDialog alertDialog) throws ParseException {

        if(!editTextTitle.getText().toString().isEmpty() && dateIsOk(editTextDate)){

            final String stringTitle = editTextTitle.getText().toString();
            final String stringDate = editTextDate.getText().toString();
            final String stringDescription = editTextDescription.getText().toString();

            addNewRecycler(stringTitle, stringDate, stringDescription);
            alertDialog.dismiss();

        }else if(editTextTitle.getText().toString().isEmpty() && dateIsOk(editTextDate)){                              //editTextTitle is empty
            Toasty.info(CountdownActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
        }else if(!editTextTitle.getText().toString().isEmpty() && !dateIsOk(editTextDate)){                            //date is wrong
            Toasty.info(CountdownActivity.this, "请设置一个未来的时间 ", Toast.LENGTH_SHORT).show();
        }else if(editTextTitle.getText().toString().isEmpty() && !dateIsOk(editTextDate)){                             //title and date is wrong
            Toasty.error(CountdownActivity.this, "请输入标题和一个未来的时间 ", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }




    //

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
                Toast.makeText(this, "你点击的是蓝牙同步按钮，但是并没有什么卵用，因为我们现在没有蓝牙模块", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
        //Toolbar菜单点击事件
    }

}
