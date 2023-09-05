package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;
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
    int session_end_or_not=0;
    boolean screen_off_mode;
    int current_project_id,minutes=0,current_session_id=-1;//no session had created if current session id=-1

    String date_string;

    private Chronometer cmeter;
    ImageButton home_btn,project_overview_btn;
    Button pause_btn,start_btn;
    private boolean running=false;
    private long pause_offset=0;
    RelativeLayout complete_grid_layout;

    ProjectDetail current_project;

    DrawView d1;
    Uri uri;
    ImageFilterView image11;

    public static final String  SCREEN_OFF_MODE="SCREEN_MODE_OFF";
    public static final String SHARED_PREF="SHARED_PREF";

    int mdefaultcolor;

    FloatingActionButton add,edit,grid_show,stop_watch,bw_btn;
    FloatingActionButton diagonal_grids;   //version 2
    Animation  from_bottom,to_bottom, to_right;
    boolean isOpen;
    LinearLayout stop_watch_layout;

    private AdManagerAdView mAdManagerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_luckygridview);


        //addmob add
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
                mAdManagerAdView.loadAd(adRequest);
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

        //getting screen mode off value from shared pref
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        screen_off_mode=sharedPreferences.getBoolean(SCREEN_OFF_MODE,false);

        //getting id number from previous activity
        Bundle extras=getIntent().getExtras();
        current_project_id=extras.getInt("id_number");

        DataBaseHelper db=new DataBaseHelper(LuckyGridview.this);
        current_project = db.getProject(current_project_id);

        SimpleDateFormat formater=new SimpleDateFormat("dd/MM/yyyy");
        Date date=new Date();
        date_string=formater.format(date);


        d1=(DrawView)findViewById(R.id.lineview);

        double h= current_project.height;
        double w= current_project.width;

        d1.ratio=h/w;
        d1.width_paper=current_project.width;
        d1.cell_size=current_project.cell_size;
        d1.line_width=current_project.stroke_width;
        d1.color=current_project.color;

        //setting text on initial edit view



        image11= findViewById(R.id.image11);
        loadImageFromInternal(current_project.reference_img);





        bw_btn=findViewById(R.id.bw_btn);
        bw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image11.getSaturation()!=0){
                    image11.setSaturation(0);
                }
                else{
                    image11.setSaturation(1);
                }

            }
        });

        //setting for floating action button
        //chronometer
        cmeter=findViewById(R.id.stopwatch);
        start_btn=findViewById(R.id.start_btn);
        pause_btn=findViewById(R.id.pause_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!running && pause_offset==0){
                    start_btn.setText("reset");   //version 2
                    DataBaseHelper dbhelp=new DataBaseHelper(LuckyGridview.this);
                    SessionDetails s1=new SessionDetails(current_project_id,minutes,date_string);
                    dbhelp.addSession(s1);
                    current_session_id=dbhelp.current_session();
                    cmeter.setBase(SystemClock.elapsedRealtime());
                    cmeter.start();
                    running=true;
                }
                else if(running){
                    String msg="Timer will reset to 0, Are you sure this?";
                    alert(msg);
                }
                else if(pause_offset!=0){
                    String msg="Timer will reset to 0, Are you sure for this?";
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


        add=(FloatingActionButton)findViewById(R.id.add_btn);
        edit=(FloatingActionButton)findViewById((R.id.edit));
        stop_watch=(FloatingActionButton)findViewById((R.id.stop_watch));
        grid_show=(FloatingActionButton)findViewById((R.id.grid_show));
        diagonal_grids=(FloatingActionButton)findViewById(R.id.diagonal_grids);

        from_bottom=AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        to_bottom=AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);
        to_right=AnimationUtils.loadAnimation(this,R.anim.to_right_anim);
        stop_watch_layout=findViewById(R.id.stop_watch_layout);

        //set the on click listener on add
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
                if(d1.flag_diagonal==1){
                    d1.flag_diagonal=0;
                }
                else
                    d1.flag_diagonal=1;
                d1.drawAgain();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditLayoutFragment f1=new EditLayoutFragment(d1,LuckyGridview.this);
                f1.show(getSupportFragmentManager(),"example2 dialog");
            }
        });

        grid_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(d1.flag_normalgrid==0)
                    d1.flag_normalgrid=1;
                else
                    d1.flag_normalgrid=0;
                d1.drawAgain();
            }
        });

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
        project_overview_btn=findViewById(R.id.project_overview_btn);
        project_overview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_end_or_not=1;
                Intent I1=new Intent(LuckyGridview.this,ProjectOverview.class);
                I1.putExtra("id_number",current_project_id);
                startActivity(I1);
                finish();
            }
        });


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

        if(running && !screen_off_mode)
        {
            pause_offset=SystemClock.elapsedRealtime()-cmeter.getBase();
            if(session_end_or_not==0)
                Toast.makeText(LuckyGridview.this,"session paused",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LuckyGridview.this,"session ended",Toast.LENGTH_SHORT).show();

        }
        else if(running && screen_off_mode){
            pause_offset=SystemClock.elapsedRealtime()-cmeter.getBase();
            if(session_end_or_not==1)
                Toast.makeText(LuckyGridview.this,"session ended",Toast.LENGTH_SHORT).show();
        }


        if(current_session_id!=-1){
            minutes=(int)pause_offset/1000;
            DataBaseHelper db=new DataBaseHelper(LuckyGridview.this);
            db.sessionUpdate(current_session_id,minutes);

            //updating current project details
            db.projectUpdate(current_project_id,d1.color,d1.line_width,d1.cell_size,date_string);

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
            image11.setImageBitmap(b);
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


}