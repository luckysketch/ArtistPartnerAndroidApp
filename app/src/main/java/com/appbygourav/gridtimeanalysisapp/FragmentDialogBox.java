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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentDialogBox extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener{
    int flag=0;
    int w,h;
    Spinner spinner_orientation,spinner_size;
    Context context;
    EditText width,height,create_project_name;
    com.google.android.material.button.MaterialButton cancel_btn,open_btn;
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
        sizes.add("A5");
        sizes.add("A4");
        sizes.add("A3");
        sizes.add("A2");
        sizes.add("A1");
        sizes.add("CUSTOM");
        ArrayAdapter adapter1=new ArrayAdapter(context, android.R.layout.simple_spinner_item,sizes);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayList<String> oren=new ArrayList<>();
        oren.add("Vertical");
        oren.add("Horizontal");
        ArrayAdapter adapter2=new ArrayAdapter(context, android.R.layout.simple_spinner_item,oren);
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
                            String ws,hs;
                            ws=width.getText().toString();
                            hs=height.getText().toString();
                            if(ws.length()!=0 && hs.length()!=0){
                                String pname=create_project_name.getText().toString();
                                w=Integer.parseInt(width.getText().toString());
                                h=Integer.parseInt(height.getText().toString());
                                if(flag==1){
                                    int temp=w;
                                    w=h;
                                    h=temp;
                                }
                                if(w==0 || h==0){
                                    Toast.makeText(context,"Enter valid Dimensions",Toast.LENGTH_SHORT).show();
                                }
                                else
                                    listener.applyText(w,h,pname);
                            }
                            else
                                Toast.makeText(context,"Enter valid Dimensions",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        return builder.create();
    }
    public interface FragmentListener{
        void applyText(int width,int height,String pname);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       if(parent.getId()==R.id.spinner_size){
           String val=parent.getItemAtPosition(position).toString();
           switch (val){
               case "CUSTOM" :
                   width.setText("0");
                   height.setText("0");
                   break;
               case "A5":
                   width.setText("148");
                   height.setText("210");
                   break;
               case "A4":
                   width.setText("210");
                   height.setText("297");
                   break;
               case "A3":
                   width.setText("297");
                   height.setText("420");

                   break;
               case "A2":
                   width.setText("420");
                   height.setText("594");
                   break;
               case "A1":
                   width.setText("594");
                   height.setText("841");
                   break;
           }
       }
       else if(parent.getId()==R.id.spinner_orientation){
           String res=parent.getItemAtPosition(position).toString();
           if(res.equals("Horizontal")){
               flag=1;
           }
           else
               flag=0;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
