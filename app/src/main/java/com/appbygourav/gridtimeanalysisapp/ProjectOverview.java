package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appbygourav.DataBaseService.DataBaseHelper;
import com.appbygourav.Service.CommonUtility;
import com.appbygourav.beans.ProjectDetail;
import com.appbygourav.beans.SessionDetails;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ProjectOverview extends AppCompatActivity {

    int current_project_id=1;
    ProjectDetail current_project;
    TextView overview_created_date,overview_last_activity,overview_total_sessions,overview_total_hours,overview_total_mintues;

    TextView project_name_overview,overview_paper_size;
    ImageView overview_ref_img;
    ListView session_list_view;

    DataBaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_project_overview);

        Bundle extras=getIntent().getExtras();
        current_project_id=extras.getInt("id_number");
        dbHelp=new DataBaseHelper(ProjectOverview.this);
        current_project=dbHelp.getProject(current_project_id);

        project_name_overview=findViewById(R.id.project_name_overview);
        project_name_overview.setText(current_project.getName());
        overview_ref_img=findViewById(R.id.overview_ref_img);
        loadImageFromInternal(current_project.getReference_img());

        //list view
        session_list_view=findViewById(R.id.session_list_view);
        List<SessionDetails> allSessions = dbHelp.getAllSessions(current_project_id);


        ArrayAdapter adapter=new ArrayAdapter<SessionDetails>(ProjectOverview.this, android.R.layout.simple_list_item_1,allSessions);
        session_list_view.setAdapter(adapter);

        //set the all data on text views
        overview_created_date=findViewById(R.id.overview_created_date);
        overview_last_activity=findViewById(R.id.overview_last_activity);
        overview_total_sessions=findViewById(R.id.overview_total_session);
        overview_total_hours=findViewById(R.id.overview_total_hours);
        overview_total_mintues=findViewById(R.id.overview_total_minutes);
        overview_paper_size=findViewById(R.id.overview_paper_size);


        overview_created_date.setText(CommonUtility.getDateStringFromDbFormat(current_project.getCreate_date()));
        overview_last_activity.setText(CommonUtility.getDateStringFromDbFormat(current_project.getLast_activity()));

        double height=(double)current_project.getHeight()/10.0;
        double width=(double)current_project.getWidth()/10.0;
        overview_paper_size.setText(Double.toString(height)+" X "+Double.toString(width)+" cm");

        int total_session=allSessions.size();
        overview_total_sessions.setText(Integer.toString(total_session));
        setTotalHourAndTime(allSessions);

        session_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionDetails clickedSession=(SessionDetails) parent.getItemAtPosition(position);

                alert("You want to Delete this session",clickedSession);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SessionDetails> allSessions = dbHelp.getAllSessions(current_project_id);
                ArrayAdapter adapter=new ArrayAdapter<SessionDetails>(ProjectOverview.this, android.R.layout.simple_list_item_1,allSessions);
                session_list_view.setAdapter(adapter);

                int total_session=allSessions.size();
                overview_total_sessions.setText(Integer.toString(total_session));
                setTotalHourAndTime(allSessions);

            }
        },1000);
    }

    public void setTotalHourAndTime(List<SessionDetails> sessionList){
        int total_seconds=0;
        for(int i=0;i<sessionList.size();i++)
            total_seconds+=sessionList.get(i).getTimeSeconds();
        int min=total_seconds/60;
        int hour=min/60;
        min=min%60;
        overview_total_hours.setText(Integer.toString(hour));
        overview_total_mintues.setText(Integer.toString(min));
    }

    private void loadImageFromInternal(String path){
        File f= new File(path);
        Bitmap b= null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            overview_ref_img.setImageBitmap(b);
        } catch (FileNotFoundException e) {

        }
    }

    private void alert(String message,SessionDetails s1) {
        AlertDialog dig=new AlertDialog.Builder(ProjectOverview.this)
                .setTitle("Warning")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbHelp.deleteSession(s1);
                        List<SessionDetails> allSessions = dbHelp.getAllSessions(current_project_id);
                        ArrayAdapter adapter=new ArrayAdapter<SessionDetails>(ProjectOverview.this, android.R.layout.simple_list_item_1,allSessions);
                        session_list_view.setAdapter(adapter);

                        int total_session=allSessions.size();
                        overview_total_sessions.setText(Integer.toString(total_session));
                        setTotalHourAndTime(allSessions);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dig.show();
    }
}