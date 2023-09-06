package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AllProjects extends AppCompatActivity {
    ArrayList<ProjectDetail> allProject;
    DataBaseHelper dbHelp;
    androidx.recyclerview.widget.RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_projects);



         dbHelp=new DataBaseHelper(AllProjects.this);
        allProject= (ArrayList<ProjectDetail>) dbHelp.getAllProjects();

        //new version 2 reverse arraylist
        int i=0;
        int j=allProject.size()-1;
        while(i<j){
            Collections.swap(allProject,i,j);
            i++;
            j--;
        }


        CustomAdaptor adaptor=new CustomAdaptor(allProject,AllProjects.this);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adaptor);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AllProjects.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adaptor.setOnItemClickListener(new CustomAdaptor.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ProjectDetail projectDetail = allProject.get(position);
                openSelectedOption(projectDetail);
            }
        });
    }

    private void openSelectedOption(ProjectDetail projectDetail) {
        String str[]={"Project Overview","Resume","Delete"};
        AlertDialog dig= new AlertDialog.Builder(AllProjects.this)
                .setTitle("Select")
                .setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(str[which]=="Project Overview"){
                            Intent I1=new Intent(AllProjects.this,ProjectOverview.class);
                            I1.putExtra("id_number",projectDetail.getId());
                            startActivity(I1);
                        }
                        else if(str[which]=="Resume"){
                            Intent I1=new Intent(AllProjects.this,LuckyGridview.class);
                            I1.putExtra("id_number",projectDetail.getId());
                            startActivity(I1);
                            finish();
                        }
                        else{
                            deleteProject(projectDetail);
                        }
                    }
                })
                .create();
        dig.show();


    }

    private void deleteProject(ProjectDetail projectDetail) {
        AlertDialog dig=new AlertDialog.Builder(AllProjects.this)
                .setTitle("Warning")
                .setMessage("Are you Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbHelp.deleteProject(projectDetail);
                        allProject= (ArrayList<ProjectDetail>) dbHelp.getAllProjects();

                        CustomAdaptor adaptor=new CustomAdaptor(allProject,AllProjects.this);
                        recyclerView.setAdapter(adaptor);

                       // int total_session=allProject.size();
                        //use it after a while


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