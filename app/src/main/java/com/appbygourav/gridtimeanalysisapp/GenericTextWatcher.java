package com.appbygourav.gridtimeanalysisapp;

import android.text.Editable;
import android.text.TextWatcher;


public class GenericTextWatcher implements TextWatcher {


     DrawView view;
     GridLabelView gridLabelView;
     int flag;
     GenericTextWatcher(DrawView view,GridLabelView gridLabelView,int flag){
        this.view=view;
        this.gridLabelView = gridLabelView;
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
            int inputValue=Integer.parseInt(s.toString());
            if(inputValue==0)
                s.replace(0,s.length(),"1");
            else if(flag==0){
                view.setLine_width(inputValue);
                view.drawAgain();
            }
            else if(flag==1){
                view.setCell_size(inputValue);
                gridLabelView.setCell_size(inputValue);
                gridLabelView.drawAgain();
                view.drawAgain();
            }
        }
        catch(NumberFormatException e){ }
    }
}
