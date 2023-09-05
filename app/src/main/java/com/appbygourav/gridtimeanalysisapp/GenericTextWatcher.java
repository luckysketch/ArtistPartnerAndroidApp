package com.appbygourav.gridtimeanalysisapp;

import android.text.Editable;
import android.text.TextWatcher;


public class GenericTextWatcher implements TextWatcher {


     DrawView view;
     int flag;
     GenericTextWatcher(DrawView view,int flag){
        this.view=view;
        this.flag=flag;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        try {
            int num=Integer.parseInt(s.toString());
            if(num==0)
                s.replace(0,s.length(),"1");
            else if(flag==0){
                view.line_width=num;
                view.drawAgain();
            }
            else if(flag==1){
                view.cell_size=num;
                view.drawAgain();
            }
        }
        catch(NumberFormatException e){ }
    }
}
