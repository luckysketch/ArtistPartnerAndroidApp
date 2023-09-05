package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LuckySettings extends AppCompatActivity {

    public static final String  SCREEN_OFF_MODE="SCREEN_MODE_OFF";
    public static final String SHARED_PREF="SHARED_PREF";
    TextView session,time_hour,time_min,project,best_session;

    Switch sw1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lucky_settings);



        sw1=findViewById(R.id.switch1);



        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean(SCREEN_OFF_MODE,sw1.isChecked());
                editor.apply();

            }
        });

        //loading current data from shared pref
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        Boolean b1=sharedPreferences.getBoolean(SCREEN_OFF_MODE,false);

        sw1.setChecked(b1);

        DataBaseHelper dbhelp=new DataBaseHelper(LuckySettings.this);
        List<Integer> list1=dbhelp.totalwork();

        time_hour=findViewById(R.id.setting_total_time_hour);
        time_min=findViewById(R.id.setting_total_time_min);
        session=findViewById(R.id.setting_total_session);
        project=findViewById(R.id.setting_total_project);
        best_session=findViewById(R.id.setting_best_session);
        String str=Integer.toString(list1.get(0));
        session.setText(str);
        int best_session1=list1.get(2)/60;
        str=Integer.toString(best_session1)+" min";
        best_session.setText(str);
        str=Integer.toString(list1.get(3));
        project.setText(str);
        int seconds=list1.get(1);
        int min=seconds/60;
        int hours=min/60;
        min=min%60;
        str=Integer.toString(hours);
        time_hour.setText(str);
        str=Integer.toString(min);
        time_min.setText(str);

    }


}