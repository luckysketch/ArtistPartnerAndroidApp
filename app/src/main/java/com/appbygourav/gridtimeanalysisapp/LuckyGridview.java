package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appbygourav.DataBaseService.DataBaseHelper;

import com.appbygourav.Service.CommonUtility;
import com.appbygourav.Service.FirebaseAnalyticsService;
import com.appbygourav.beans.ProjectDetail;
import com.appbygourav.beans.SessionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LuckyGridview extends AppCompatActivity {
    private int session_end_or_not=0;
    private boolean screen_off_mode;
    private int current_project_id;
    private int minutes=0;
    private int current_session_id=-1;//no session had created if current session id=-1

    private Chronometer cmeter;
    private boolean running=false;
    private long pause_offset=0;
    private boolean isOpen;

    private DataBaseHelper dataBaseHelper;


    ImageButton home_btn,imgDownloadBtn;
    Button pause_btn,start_btn;
    RelativeLayout complete_grid_layout;
    ProjectDetail current_project;
    DrawView girdDrawView;
    GridLabelView gridLabelView;
    ImageFilterView projectIamge;
    LinearLayout stop_watch_layout;

    public static final String  SCREEN_OFF_MODE="SCREEN_MODE_OFF";
    public static final String SHARED_PREF="SHARED_PREF";

    FloatingActionButton add,edit,grid_show,stop_watch,bw_btn;
    FloatingActionButton diagonal_grids;   //version 2
    Animation  from_bottom,to_bottom, to_right;

    private AdManagerAdView mAdManagerAdView;

    private FirebaseAnalyticsService firebaseAnalyticsService;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAnalyticsService = new FirebaseAnalyticsService(this);
        firebaseAnalyticsService.logScreenView("LuckyGridView", this.getClass().getSimpleName());

        onCreateInitialize();
        setContentView(R.layout.activity_luckygridview);

        //getting id number from previous activity
        Bundle extras=getIntent().getExtras();
        current_project_id=extras.getInt("id_number");
        current_project = dataBaseHelper.getProject(current_project_id);
        FloatingActionButton temp  = findViewById(R.id.add_btn);

        projectIamge= findViewById(R.id.image11);
        loadImageFromInternal(current_project.getReference_img());

        setGridAndLabelView();
        setListnerForFloatingButton();
        setBottomBarButtonListner();

        if(addAndGetTotalCountForGridActivity()>20){
            listenerForBannerAdd();
        }
    }

    private Integer addAndGetTotalCountForGridActivity(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        int totalGridViewCount=sharedPreferences.getInt(CommonUtility.TOTAL_COUNT_FOR_GRIDVIEW,0);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt(CommonUtility.TOTAL_COUNT_FOR_GRIDVIEW,totalGridViewCount+1);
        editor.apply();
        return totalGridViewCount;
    }

    private void setBottomBarButtonListner() {
        home_btn=findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_end_or_not=1;
                Intent I1=new Intent(LuckyGridview.this,MainActivity.class);
                startActivity(I1);
                finish();
            }
        });
        imgDownloadBtn=findViewById(R.id.imgDownloadBtn);
        imgDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomLayout zoomLayout=(ZoomLayout)findViewById(R.id.gridZoomLayoutId);
                zoomLayout.setZoomScaleToZero();
                Handler handler = new Handler(Looper.getMainLooper());
                firebaseAnalyticsService.logCustomEvent("LuckyGridView","downloadImage-clicked",String.valueOf(current_project_id));
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        SaveGridImageInGallery();
                    }
                };
                handler.postDelayed(runnable, 300);
            }
        });

        setChronometerButtonListen();
    }

    private void SaveGridImageInGallery() {
        View viewToSave = findViewById(R.id.gridImageView);
        Bitmap bitmap;
        viewToSave.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(viewToSave.getDrawingCache());
        viewToSave.setDrawingCacheEnabled(false);
        if(CommonUtility.saveBitmapToGallery(LuckyGridview.this,bitmap)){
            Toast.makeText(LuckyGridview.this,"Image Saved",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LuckyGridview.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    private void setChronometerButtonListen() {
        stop_watch_layout=findViewById(R.id.stop_watch_layout);
        cmeter=findViewById(R.id.stopwatch);
        start_btn=findViewById(R.id.start_btn);
        pause_btn=findViewById(R.id.pause_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAnalyticsService.logCustomEvent("LuckyGridView","startTimer-clicked",String.valueOf(current_project_id));
                if(!running && pause_offset==0){
                    start_btn.setText("reset");   //version 2
                    Date currDateTime = new Date();
                    SessionDetails s1=new SessionDetails(current_project_id,minutes,CommonUtility.getDateStrInOldDBFormat(currDateTime));
                    s1.setCreatedate(CommonUtility.getDateStrInDBFormat(currDateTime));
                    s1.setModidate(CommonUtility.getDateStrInDBFormat(currDateTime));
                    long savedSessionId=dataBaseHelper.addSession(s1);
                    current_session_id=(int)savedSessionId;
                    cmeter.setBase(SystemClock.elapsedRealtime());
                    cmeter.start();
                    running=true;
                }
                else if(running){
                    String msg="Timer will reset to 0, Are you sure?";
                    alert(msg);
                }
                else if(pause_offset!=0){
                    String msg="Timer will reset to 0, Are you sure?";
                    alert(msg);
                }
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    pause_btn.setText("resume");
                    cmeter.stop();
                    pause_offset=SystemClock.elapsedRealtime()-cmeter.getBase();
                    running=false;
                }
                else{
                    if(pause_offset!=0){
                        pause_btn.setText("pause");
                        cmeter.setBase(SystemClock.elapsedRealtime()-pause_offset);
                        cmeter.start();
                        running=true;
                    }
                }
            }
        });
    }

    private void setListnerForFloatingButton() {
        add=(FloatingActionButton)findViewById(R.id.add_btn);
        edit=(FloatingActionButton)findViewById((R.id.edit));
        stop_watch=(FloatingActionButton)findViewById((R.id.stop_watch));
        grid_show=(FloatingActionButton)findViewById((R.id.grid_show));
        diagonal_grids=(FloatingActionButton)findViewById(R.id.diagonal_grids);
        bw_btn=findViewById(R.id.bw_btn);

        bw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(projectIamge.getSaturation()!=0){
                    projectIamge.setSaturation(0);
                }
                else {
                    projectIamge.setSaturation(1);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childOpen();
            }
        });
        stop_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stop_watch_layout.getVisibility()==View.VISIBLE){
                    stop_watch_layout.setVisibility(View.GONE);
                }
                else{
                    stop_watch_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        diagonal_grids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girdDrawView.getFlag_diagonal()!=null && girdDrawView.getFlag_diagonal()){
                    girdDrawView.setFlag_diagonal(false);
                }else {
                    girdDrawView.setFlag_diagonal(true);
                }
                girdDrawView.drawAgain();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditLayoutFragment f1=new EditLayoutFragment(girdDrawView,gridLabelView,LuckyGridview.this);
                f1.show(getSupportFragmentManager(),"example2 dialog");
            }
        });
        grid_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(girdDrawView.getFlag_diagonal()!=null && girdDrawView.getFlag_normalgrid()==true){
                    girdDrawView.setFlag_normalgrid(false);
                    gridLabelView.setVisibility(View.INVISIBLE);
                }else{
                    girdDrawView.setFlag_normalgrid(true);
                    gridLabelView.setVisibility(View.VISIBLE);
                }
                girdDrawView.drawAgain();
            }
        });
    }

    private void setGridAndLabelView() {

        int projectImgHeight= current_project.getHeight();
        int projectImgWidth= current_project.getWidth();

        girdDrawView=(DrawView)findViewById(R.id.lineview);
        girdDrawView.setRatio((float)projectImgHeight/(float)projectImgWidth);
        girdDrawView.setWidth_paper(projectImgWidth);
        girdDrawView.setCell_size(current_project.getCell_size());
        girdDrawView.setLine_width(current_project.getStroke_width());
        girdDrawView.setColor(current_project.getColor());
        girdDrawView.drawAgain();

        gridLabelView=(GridLabelView)findViewById(R.id.gridlabelview);
        gridLabelView.setRatio((float)projectImgHeight/(float)projectImgWidth);
        gridLabelView.setWidth_paper(current_project.getWidth());
        gridLabelView.setCell_size(current_project.getCell_size());
        gridLabelView.setLine_width(current_project.getStroke_width());
        gridLabelView.setColor(current_project.getColor());
        gridLabelView.drawAgain();

    }

    private void onCreateInitialize() {

        this.dataBaseHelper=new DataBaseHelper(LuckyGridview.this);

        //getting screen mode off value from shared pref
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        screen_off_mode=sharedPreferences.getBoolean(SCREEN_OFF_MODE,false);

        from_bottom=AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        to_bottom=AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);
        to_right=AnimationUtils.loadAnimation(this,R.anim.to_right_anim);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(running && !screen_off_mode){
            cmeter.setBase(SystemClock.elapsedRealtime()-pause_offset);
            Toast.makeText(LuckyGridview.this,"session resumed",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Date currDate = new Date();
        pause_offset=SystemClock.elapsedRealtime()-cmeter.getBase();
        String timeStr = CommonUtility.getTimeStringFromSeconds(pause_offset/1000);
        if(running && !screen_off_mode)
        {
            if(session_end_or_not==0)
                Toast.makeText(LuckyGridview.this,"session paused",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LuckyGridview.this,timeStr+"added to your project",Toast.LENGTH_SHORT).show();
        }
        else if(running && screen_off_mode){
            if(session_end_or_not==1)
                Toast.makeText(LuckyGridview.this,timeStr+"added to your project",Toast.LENGTH_SHORT).show();
        }
        if(current_session_id!=-1){
            int seconds=(int)pause_offset/1000;
            dataBaseHelper.sessionUpdate(current_session_id,seconds,CommonUtility.getDateStrInDBFormat(currDate));
        }
        if(current_project_id>0){
            //updating current project details
            dataBaseHelper.projectUpdate(current_project_id,girdDrawView.getColor(),girdDrawView.getLine_width(),girdDrawView.getCell_size(),CommonUtility.getDateStrInOldDBFormat(currDate),CommonUtility.getDateStrInDBFormat(currDate));
            //save Recent active Project
            SharedPreferences sharedPreferences=getSharedPreferences(CommonUtility.SHARED_PREF,MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt(CommonUtility.LAST_ACTIVE_PROJECT_ID,current_project_id);
            editor.apply();
        }
    }
    private void alert(String message) {
        AlertDialog dig=new AlertDialog.Builder(LuckyGridview.this)
                .setTitle("Warning")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cmeter.setBase(SystemClock.elapsedRealtime());
                        cmeter.start();
                        running=true;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dig.show();
    }

    public void childOpen(){
        if(!isOpen){
            edit.startAnimation(from_bottom);
            stop_watch.startAnimation(from_bottom);
            grid_show.startAnimation(from_bottom);
            bw_btn.startAnimation(to_right);
            diagonal_grids.startAnimation(to_right);
            isOpen=true;
            edit.show();
            edit.setClickable(true);
            stop_watch.show();
            stop_watch.setClickable(true);
            grid_show.show();
            grid_show.setClickable(true);
            bw_btn.show();
            bw_btn.setClickable(true);
            diagonal_grids.show();
            diagonal_grids.setClickable(true);

        }
        else{
            edit.startAnimation(to_bottom);
            stop_watch.startAnimation(to_bottom);
            grid_show.startAnimation(to_bottom);
            bw_btn.startAnimation(to_bottom);
            diagonal_grids.startAnimation(to_bottom);
            isOpen=false;
            edit.hide();
            edit.setClickable(false);
            stop_watch.hide();
            stop_watch.setClickable(false);
            grid_show.hide();
            grid_show.setClickable(false);
            bw_btn.hide();
            bw_btn.setClickable(false);
            diagonal_grids.hide();
            diagonal_grids.setClickable(false);
        }
    }

    private void loadImageFromInternal(String path){
        File f= new File(path);
        Bitmap b= null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            projectIamge.setImageBitmap(b);
        } catch (FileNotFoundException e) {

        }
    }
    boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this,"Back again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

    private void listenerForBannerAdd() {
        mAdManagerAdView = findViewById(R.id.adManagerAdView);
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        mAdManagerAdView.loadAd(adRequest);
        mAdManagerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                handler.postDelayed(new Runnable() {
                    @Override public void run() {
                        mAdManagerAdView.loadAd(adRequest);
                    }
                }, 40000);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }






}