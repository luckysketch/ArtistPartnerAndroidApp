package com.appbygourav.gridtimeanalysisapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.appbygourav.DataBaseService.DataBaseHelper;
import com.appbygourav.Service.CommonUtility;
import com.appbygourav.Service.FirebaseAnalyticsService;
import com.appbygourav.Service.FirebaseCrashlyticsService;
import com.appbygourav.Service.FirebaseCrashlyticsService;
import com.appbygourav.beans.ProjectDetail;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FragmentDialogBox.FragmentListener{

    int paper_width;
    int paper_heigth;
    String project_name="Project_1";

    Button create_btn,last_active_project,all_project,setting_btn1,help_btn,rate_us_btn,share_btn;
    Bitmap bitmap;

    File directory;
    ContextWrapper cw;
    FirebaseAnalyticsService firebaseAnalyticsService;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        FirebaseCrashlyticsService.initializeCrashlyticsCustomKey();
        firebaseAnalyticsService = new FirebaseAnalyticsService(this);
        firebaseAnalyticsService.logScreenView("MainActivity", this.getClass().getSimpleName());

        create_btn=findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAnalyticsService.logCustomEvent("MainActivity","createNew-clicked","");
                openDialog();
            }
        });

        last_active_project=findViewById(R.id.last_active_project);
        last_active_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences(CommonUtility.SHARED_PREF,MODE_PRIVATE);
                int lastActiveProjectId=sharedPreferences.getInt(CommonUtility.LAST_ACTIVE_PROJECT_ID,-1);
                firebaseAnalyticsService.logCustomEvent("MainActivity","recentActivity-clicked",String.valueOf(lastActiveProjectId));
                if(lastActiveProjectId!=-1){
                    Intent intent=new Intent(MainActivity.this,LuckyGridview.class);
                    intent.putExtra("id_number",lastActiveProjectId);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"No recent activity found",Toast.LENGTH_SHORT).show();
                }
            }
        });
        all_project=findViewById(R.id.all_project);
        all_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AllProjects.class);
                startActivity(intent);
            }
        });

        setting_btn1=findViewById(R.id.setting_btn1);
        setting_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I1=new Intent(MainActivity.this, LuckySettings.class);
                startActivity(I1);
            }
        });
        help_btn=findViewById(R.id.help_btn);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I1=new Intent(MainActivity.this, HelpSection.class);
                startActivity(I1);
            }
        });
        rate_us_btn=findViewById(R.id.rate_us_btn);
        rate_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    firebaseAnalyticsService.logCustomEvent("MainActivity","rateUs-clicked","");
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.appbygourav.gridtimeanalysisapp")));

                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.appbygourav.gridtimeanalysisapp")));
                }
            }
        });
        share_btn=findViewById(R.id.share_btn);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    firebaseAnalyticsService.logCustomEvent("MainActivity","share-clicked","");
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String body = "Checkout this grid drawing app\n";
                    String playStoreAppUrl = " https://play.google.com/store/apps/details?id=com.appbygourav.gridtimeanalysisapp";
                    shareIntent.putExtra(Intent.EXTRA_TEXT,body+playStoreAppUrl);
                    startActivity(Intent.createChooser(shareIntent,"Share using"));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.appbygourav.gridtimeanalysisapp")));
                }
            }
        });

        cw=new ContextWrapper(getApplicationContext());
        directory=cw.getDir("imageDir", Context.MODE_PRIVATE);

        new Thread(() -> {
                    initializeMobileAddSdk();
                }).start();
    }

    private void initializeMobileAddSdk(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void openDialog() {
        FragmentDialogBox d2=new FragmentDialogBox();
        d2.show(getSupportFragmentManager(),"example dialog");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(),uri);

                String str=saveToInternalStorage(bitmap);
                ProjectDetail p1=new ProjectDetail(paper_width, paper_heigth, project_name, str);
                Date currDateTime=new Date();
                p1.setCreate_date(CommonUtility.getDateStrInOldDBFormat(currDateTime));
                p1.setCreatedate(CommonUtility.getDateStrInDBFormat(currDateTime));
                p1.setModidate(CommonUtility.getDateStrInDBFormat(currDateTime));

                DataBaseHelper dataBaseHelper=new DataBaseHelper(MainActivity.this);
                long success = dataBaseHelper.addNewProject(p1);
                if(success==-1)
                    Toast.makeText(MainActivity.this,"Something getting wrong",Toast.LENGTH_SHORT).show();
                else{
                    int cur=dataBaseHelper.current();
                    String eventValue = String.valueOf(cur);
                    firebaseAnalyticsService.logCustomEvent("MainActivity","projectCreated",eventValue);
                    Intent intent=new Intent(MainActivity.this,LuckyGridview.class);
                    intent.putExtra("id_number",cur);
                    startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrashlyticsService.fireExceptionEvent(e,"MainActivity","onActivityResult","","");
                Toast.makeText(this, "Failed to load image. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            firebaseAnalyticsService.logCustomEvent("MainActivity","ProjectCreationFailed","Failed to load image");
            Toast.makeText(MainActivity.this,"Something getting wrong",Toast.LENGTH_SHORT).show();
        }
    }

    private String saveToInternalStorage(Bitmap bitmap) throws IOException {

        File my_path=File.createTempFile("img",".png",directory);
        FileOutputStream fos=null;
        try{
            fos=new FileOutputStream(my_path);
            bitmap.compress(Bitmap.CompressFormat.PNG,50,fos);
            return my_path.getAbsolutePath();
        }catch(Exception e){ }
        fos.close();
        return null;
    }

    @Override
    public void applyText(int width, int height, String projectName) {
        paper_width=width;
        paper_heigth=height;
        project_name=projectName;
        float x=(float)paper_width;
        float y=(float)paper_heigth;
        String eventValue = String.valueOf(width)+" | "+String.valueOf(height)+" | "+String.valueOf(projectName);
        firebaseAnalyticsService.logCustomEvent("MainActivity","createProjectOpen-clicked",eventValue);

        ImagePicker.with(MainActivity.this)
                .crop(x,y)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

}