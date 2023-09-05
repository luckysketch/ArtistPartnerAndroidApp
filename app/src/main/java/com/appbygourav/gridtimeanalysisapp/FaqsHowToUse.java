package com.appbygourav.gridtimeanalysisapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class FaqsHowToUse extends AppCompatActivity {

    Button general_feature,faqs;
    TextView btn1_indicator,btn2_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs_how_to_use);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        general_feature=findViewById(R.id.general_feature);
        faqs=findViewById(R.id.faqs);
        replaceFragment(new HowToUseFragment());
        btn1_indicator=findViewById(R.id.btn1_indicater);
        btn2_indicator=findViewById(R.id.btn2_indicater);

        general_feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HowToUseFragment());
                btn1_indicator.setVisibility(View.VISIBLE);
                btn2_indicator.setVisibility(View.INVISIBLE);
            }
        });
        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FaqFragment());
                btn1_indicator.setVisibility(View.INVISIBLE);
                btn2_indicator.setVisibility(View.VISIBLE);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit();
    }

}