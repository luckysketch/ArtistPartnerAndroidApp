package com.appbygourav.gridtimeanalysisapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.appbygourav.Service.CommonUtility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentDialogBox extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener{
    int orientationFlag=0;
    Spinner spinner_orientation,spinner_size;
    Context context;
    EditText width,height,create_project_name;
    LinearLayout customPaperSizeView,orientationView;
    String paperSizeType;
    private FragmentListener listener;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            listener=(FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement fragmentListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        context=getActivity();

        ArrayList<String> sizes=new ArrayList<>();
        sizes.addAll(CommonUtility.STANDARD_PAPER_SIZE);
        sizes.add("Other");
        ArrayAdapter adapter1=new ArrayAdapter(context, android.R.layout.simple_spinner_item,sizes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter adapter2=new ArrayAdapter(context, android.R.layout.simple_spinner_item,CommonUtility.PAPER_ORIENTATION_LIST);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_dialogbox1,null);

        spinner_orientation=view.findViewById(R.id.spinner_orientation);
        spinner_size=view.findViewById(R.id.spinner_size);
        spinner_orientation.setAdapter(adapter2);
        spinner_size.setAdapter(adapter1);

        width=view.findViewById(R.id.width);
        height=view.findViewById(R.id.height);
        create_project_name=view.findViewById(R.id.create_project_name);

        spinner_size.setOnItemSelectedListener(this);
        spinner_orientation.setOnItemSelectedListener(this);

        customPaperSizeView = view.findViewById(R.id.customPaperSizeViewId);
        orientationView = view.findViewById(R.id.orientationViewId);

        builder.setView(view)
               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(create_project_name.getText().toString().matches("")){
                            Toast.makeText(context,"please enter project name",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String projectName=create_project_name.getText().toString();
                            String widthString,heightString;
                            if(orientationFlag==0){
                                widthString=width.getText().toString();
                                heightString=height.getText().toString();
                            }else{
                                heightString=width.getText().toString();
                                widthString=height.getText().toString();
                            }
                            if(paperSizeType=="Other" &&  widthString.length()!=0 && heightString.length()!=0){

                                Double widthCm=Double.parseDouble(widthString);
                                Double heightCm=Double.parseDouble(heightString);
                                int widthMm = (int)(widthCm*10);
                                int heightMm = (int)(heightCm*10);
                                if(widthMm==0 || heightMm==0){
                                    Toast.makeText(context,"Enter valid Dimensions",Toast.LENGTH_SHORT).show();
                                }else{
                                    listener.applyText(widthMm,heightMm,projectName);
                                }
                            }else if(paperSizeType=="Other"){
                                Toast.makeText(context,"Enter valid Dimensions",Toast.LENGTH_SHORT).show();
                            }else if(CommonUtility.STANDARD_PAPER_SIZE.contains(paperSizeType)){
                                HashMap<String,Integer> paperSizeMap = CommonUtility.getSizeMapByPaper(paperSizeType);
                                int stdWidthMm = orientationFlag==0 ? paperSizeMap.get("width") : paperSizeMap.get("height");
                                int stdHeightMm = orientationFlag==0 ? paperSizeMap.get("height") : paperSizeMap.get("width");
                                listener.applyText(stdWidthMm,stdHeightMm,projectName);
                            }
                        }
                    }
                });
        return builder.create();
    }
    public interface FragmentListener{
        void applyText(int width,int height,String projectName);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       if(parent.getId()==R.id.spinner_size){
           String val=parent.getItemAtPosition(position).toString();
           switch (val){
               case "Other" :
                   customPaperSizeView.setVisibility(View.VISIBLE);
                   orientationView.setVisibility(View.GONE);
                   paperSizeType="Other";
                   break;
               case "A5":
                   customPaperSizeView.setVisibility(View.GONE);
                   orientationView.setVisibility(View.VISIBLE);
                   paperSizeType="A5";
                   break;
               case "A4":
                   customPaperSizeView.setVisibility(View.GONE);
                   orientationView.setVisibility(View.VISIBLE);
                   paperSizeType="A4";
                   break;
               case "A3":
                   customPaperSizeView.setVisibility(View.GONE);
                   orientationView.setVisibility(View.VISIBLE);
                   paperSizeType="A3";
                   break;
               case "A2":
                   customPaperSizeView.setVisibility(View.GONE);
                   orientationView.setVisibility(View.VISIBLE);
                   paperSizeType="A2";
                   break;
               case "A1":
                   customPaperSizeView.setVisibility(View.GONE);
                   orientationView.setVisibility(View.VISIBLE);
                   paperSizeType="A1";
                   break;
           }
       }
       else if(parent.getId()==R.id.spinner_orientation){
           String res=parent.getItemAtPosition(position).toString();
           if(res.equals("Horizontal")){
               orientationFlag=1;
           }
           else
               orientationFlag=0;
       }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
