package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appbygourav.DataBaseService.DataBaseHelper;
import com.appbygourav.Service.FirebaseAnalyticsService;
import com.appbygourav.beans.ProjectDetail;


import java.util.ArrayList;

public class AllProjects extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    androidx.recyclerview.widget.RecyclerView recyclerView;

    private Button activeButton;
    private Button finishedButton;

    Drawable activeButtonBackground ;

    private int currentProjectListStatus = 0;
    private TextView noDataText;

    private FirebaseAnalyticsService firebaseAnalyticsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_projects);
        firebaseAnalyticsService = new FirebaseAnalyticsService(this);
        activeButtonBackground = ContextCompat.getDrawable(AllProjects.this, R.drawable.button_type_border);

        dbHelper=new DataBaseHelper(AllProjects.this);

        recyclerView=findViewById(R.id.recycler_view);
        noDataText = findViewById(R.id.noDataTextId);
        setProjectListOnLayout();

        setListenerOnButtons();
    }
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAnalyticsService.logScreenView("AllProject", this.getClass().getSimpleName());
    }

    private void setListenerOnButtons() {
        activeButton = findViewById(R.id.activePrjButton);
        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setBackground(activeButtonBackground);
                finishedButton.setBackgroundColor(Color.TRANSPARENT);
                currentProjectListStatus=0;
                setProjectListOnLayout();
            }
        });
        finishedButton = findViewById(R.id.finishedPrjButton);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finishedButton.setBackgroundTintList(ContextCompat.getColorStateList(AllProjects.this, R.color.gray_shade));
//                activeButton.setBackgroundTintList(ContextCompat.getColorStateList(AllProjects.this, R.color.white));
                finishedButton.setBackground(activeButtonBackground);
                activeButton.setBackgroundColor(Color.TRANSPARENT);
                currentProjectListStatus=1;
                setProjectListOnLayout();
            }
        });
    }

    private void setProjectListOnLayout(){
        ArrayList<ProjectDetail> currentPrjList= (ArrayList<ProjectDetail>) dbHelper.getProjectList(currentProjectListStatus);
        if(currentPrjList.size()>0){
            CustomAdaptor adaptor=new CustomAdaptor(currentPrjList,AllProjects.this);
            recyclerView.setAdapter(adaptor);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AllProjects.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            adaptor.setOnItemClickListener(new CustomAdaptor.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    ProjectDetail projectDetail = currentPrjList.get(position);
                    openSelectedOption(projectDetail);
                }
            });
            recyclerView.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    private void openSelectedOption(ProjectDetail projectDetail) {
        String movingStatusOption;
        if(currentProjectListStatus==0){
            movingStatusOption="move to finished";
        }else{
            movingStatusOption="move to active";
        }
        String str[]={"Project Overview","Resume",movingStatusOption,"Delete"};

        AlertDialog dig= new AlertDialog.Builder(AllProjects.this)
                .setTitle("Select")
                .setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int clickIndex) {
                        if(str[clickIndex]=="Project Overview"){
                            Intent I1=new Intent(AllProjects.this,ProjectOverview.class);
                            I1.putExtra("id_number",projectDetail.getId());
                            startActivity(I1);
                        }
                        else if(str[clickIndex]=="Resume"){
                            Intent I1=new Intent(AllProjects.this,LuckyGridview.class);
                            I1.putExtra("id_number",projectDetail.getId());
                            startActivity(I1);
                            finish();
                        }
                        else if(str[clickIndex]=="Delete"){
                            deleteProject(projectDetail);
                        }else{
                            changeProjectCompletionStatus(projectDetail);
                        }
                    }
                })
                .create();
        dig.show();
    }

    private void changeProjectCompletionStatus(ProjectDetail projectDetail) {
        int moveStatus = currentProjectListStatus==0 ? 1 : 0;
        dbHelper.ChangeProjectStatus(moveStatus,projectDetail.getId());
        setProjectListOnLayout();
    }
    private void deleteProject(ProjectDetail projectDetail) {
        AlertDialog dig=new AlertDialog.Builder(AllProjects.this)
                .setTitle("Warning")
                .setMessage("Are you Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProject(projectDetail);
                        setProjectListOnLayout();
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