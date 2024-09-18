package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpSection extends AppCompatActivity {

    TextView ques1,ques2,ques3,ques4,ques5,ques6;
    LinearLayout quesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_faq);
        getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        quesListView = findViewById(R.id.quesListViewId);

        ques1=findViewById(R.id.Faq_Ques1);
        ques2=findViewById(R.id.Faq_Ques2);
        ques3=findViewById(R.id.Faq_Ques3);
        ques4=findViewById(R.id.Faq_Ques4);
        ques5=findViewById(R.id.Faq_Ques5);
        ques6=findViewById(R.id.Faq_Ques6);
        setQuesOnClickListener();
    }
    public void setQuesOnClickListener(){
        ques1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(1);
            }
        });
        ques2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(2);
            }
        });
        ques3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(3);
            }
        });
        ques4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(4);
            }
        });
        ques5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(5);
            }
        });
        ques6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesClickHandler(6);
            }
        });
    }
    private void quesClickHandler(int index){
        int childCount = quesListView.getChildCount();
        LinearLayout clickQuesBox = (LinearLayout)quesListView.getChildAt(index);
        TextView clickedQuesView = (TextView) clickQuesBox.getChildAt(0);
        TextView clickedAnsView = (TextView) clickQuesBox.getChildAt(1);
        if(clickedAnsView.getVisibility()==View.VISIBLE){
            clickedAnsView.setVisibility(View.GONE);
            int default_font_color=getResources().getColor(R.color.default_font_color);
            clickedQuesView.setTextColor(default_font_color);
//            clickedQuesView.setTypeface(null, Typeface.NORMAL);
        }else{
            int redColor = getResources().getColor(R.color.faq_ques_red);
            clickedQuesView.setTextColor(redColor);
//            clickedQuesView.setTypeface(null, Typeface.BOLD);
            clickedAnsView.setVisibility(View.VISIBLE);
            for (int i = 1; i < childCount; i++) {
                if(i!=index){
                    LinearLayout quesBox = (LinearLayout)quesListView.getChildAt(i);
                    TextView quesView = (TextView) quesBox.getChildAt(0);
                    quesView.setTextColor(Color.BLACK);
                    TextView ansView = (TextView) quesBox.getChildAt(1);
                    ansView.setVisibility(View.GONE);
                }
            }
        }
    }
}