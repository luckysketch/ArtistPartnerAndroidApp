package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.appbygourav.DataBaseService.DataBaseHelper;
import com.appbygourav.Service.CommonUtility;
import com.appbygourav.Service.FirebaseAnalyticsService;
import com.appbygourav.beans.WorkBean;


import java.util.List;

public class LuckySettings extends AppCompatActivity {

    public static final String  SCREEN_OFF_MODE="SCREEN_MODE_OFF";
    public static final String SHARED_PREF="SHARED_PREF";
    TextView session,time_hour,time_min,project,best_session,best_project;

    FirebaseAnalyticsService firebaseAnalyticsService;

    Switch sw1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lucky_settings);

        firebaseAnalyticsService = new FirebaseAnalyticsService(this);
        firebaseAnalyticsService.logScreenView("luckySetting", this.getClass().getSimpleName());

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

        DataBaseHelper dhHelper=new DataBaseHelper(LuckySettings.this);
        WorkBean workBean =dhHelper.getWorkBean();

        time_hour=findViewById(R.id.setting_total_time_hour);
        time_min=findViewById(R.id.setting_total_time_min);
        session=findViewById(R.id.setting_total_session);
        project=findViewById(R.id.setting_total_project);
        best_session=findViewById(R.id.setting_best_session);
        best_project=findViewById(R.id.setting_best_project);
        String totalSessionsText=Integer.toString(workBean.getTotalSessions());
        session.setText(totalSessionsText);
        String bestSessionText=CommonUtility.getTimeStringFromSeconds(workBean.getMaxSessionTime());
        best_session.setText(bestSessionText);
        String bestProjectText=CommonUtility.getTimeStringFromSeconds(workBean.getMaxProjectTime());
        best_project.setText(bestProjectText);
        project.setText(Integer.toString(workBean.getTotalProject()));

        long totalTime = workBean.getTotalSeconds();
        long hour = totalTime/3600;
        totalTime = totalTime%3600;
        long minutes = totalTime/60;

        String total_hour=Long.toString(hour);
        time_hour.setText(total_hour);
        String total_min=Long.toString(minutes);
        time_min.setText(total_min);
    }
}