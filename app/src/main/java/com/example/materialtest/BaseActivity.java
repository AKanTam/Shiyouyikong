package com.example.materialtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showBackDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /** Give the tip when exit the application. */

    public void showBackDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("是否要退出软件")
                .setPositiveButton("退出",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                ActivityCollector.finishAll();
                                android.os.Process
                                        .killProcess(android.os.Process
                                                .myPid());

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("我手滑了",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog ad = builder.create();
        ad.show();
    }

}
