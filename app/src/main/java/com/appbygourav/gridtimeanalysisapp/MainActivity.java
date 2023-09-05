package com.appbygourav.gridtimeanalysisapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FragmentDialogBox.FragmentListener{

    int paper_width;
    int paper_heigth;
    String project_name="Project_1";



    Button create_btn,last_active_project,all_project,setting_btn1,faq_btn,rate_us_btn;
    TextView path;
    String str;
    Bitmap bitmap;

    File directory;
    ContextWrapper cw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        create_btn=findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        last_active_project=findViewById(R.id.last_active_project);
        last_active_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dbHelp=new DataBaseHelper(MainActivity.this);
                //it giver maximum session id
                int i = dbHelp.lastActiveSessionProjectId();
                if(i!=-1){
                    Intent intent=new Intent(MainActivity.this,LuckyGridview.class);
                    intent.putExtra("id_number",i);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"No recent activity recognised",Toast.LENGTH_SHORT).show();
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
        faq_btn=findViewById(R.id.faq_btn);
        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I1=new Intent(MainActivity.this, FaqsHowToUse.class);
                startActivity(I1);
            }
        });
        rate_us_btn=findViewById(R.id.rate_us_btn);
        rate_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.appbygourav.gridtimeanalysisapp")));

                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.appbygourav.gridtimeanalysisapp")));
                }
            }
        });


        cw=new ContextWrapper(getApplicationContext());
        directory=cw.getDir("imageDir", Context.MODE_PRIVATE);

        //addmob ads
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
            } catch (IOException e) {
            }
            try {
                String str=saveToInternalStorage(bitmap);
                ProjectDetail p1=new ProjectDetail(paper_width, paper_heigth, project_name, str);

                DataBaseHelper dataBaseHelper=new DataBaseHelper(MainActivity.this);
                long success = dataBaseHelper.addOne(p1);
                if(success==-1)
                    Toast.makeText(MainActivity.this,"Something getting wrong",Toast.LENGTH_SHORT).show();
                else{
                    int cur=dataBaseHelper.current();
                    Intent intent=new Intent(MainActivity.this,LuckyGridview.class);
                    intent.putExtra("id_number",cur);
                    startActivity(intent);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
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
    /*private void loadImageFromInternal(String path){
        File f= new File(path);
        Bitmap b= null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            img2.setImageBitmap(b);
        } catch (FileNotFoundException e) {

        }

    }*/


    @Override
    public void applyText(int width, int height, String pname) {
        paper_width=width;
        paper_heigth=height;
        project_name=pname;
        float x=(float)paper_width;
        float y=(float)paper_heigth;

       /* String str=Float.toString(x);
        t1.setText(str);
        String str2=Float.toString(y);
        t2.setText(str2);*/

        //float x=1.0f;
        //float y=1.414f;

        ImagePicker.Companion.with(MainActivity.this)
                .crop(x,y)
                //.cropOval()
                // .galleryOnly()
                 .maxResultSize(2000, 2000)
                .start();
    }
    boolean doubleBackToExitPressedOnce=false;

   /* @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
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
    }*/
}