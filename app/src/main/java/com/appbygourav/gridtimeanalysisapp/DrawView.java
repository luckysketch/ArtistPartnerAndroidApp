package com.appbygourav.gridtimeanalysisapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    public double ratio;  //width:height
    public int width_paper;   //width of paper( int mm)
    public int cell_size;   //mm me
    public int line_width;   //stroke width
    int color=Color.BLACK;
    String text,str1,str2;
    //version 2 diagonal flag
    int flag_diagonal=0;
    int flag_normalgrid=1;

    public DrawView(Context context) {
        super(context);
        init(null);
    }

    private void init(@Nullable AttributeSet set) {
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(line_width);
        int fnsize=20;//font size
        if(cell_size>20)
        {
            fnsize=30;
            paint.setTextSize(30f);
        }
        else
            paint.setTextSize(20f);
        super.onDraw(canvas);
        //text=String.valueOf(ratio);
        //canvas.drawText(text,100,150,paint);

        //text=String.valueOf(width_paper);
        //canvas.drawText(text,100,200,paint);
        //text=String.valueOf(cell_size);
        //canvas.drawText(text,100,250,paint);



        int width= getWidth(); //width of custom view
        double w=(double)width;
        int height = getHeight();  //height of custom view
        //double r=1.414/1.0;
        double r=ratio;
        //double c=20.0*(w/210.0);
        double d=(double)cell_size;
        double wop=(double)width_paper;
        double c=d*(w/wop);
        int cell=(int) c;
        double h=r*w;
        int imageh= (int) h; //height of image

        int starti=(height-imageh)/2;

        int t,i;
        if(flag_normalgrid==1)
        {
            i=1;
            while(i*cell<width){
                canvas.drawLine(i * cell, starti+0, i * cell, starti +imageh, paint);
                str1= String.valueOf(i);
                t=i-1;
                if(i!=1)
                    canvas.drawText(str1,t * cell+ 5,starti+fnsize,paint);

                i++;
            }
            t=i-1;
            str1= String.valueOf(i);
            canvas.drawText(str1,t * cell+ 5,starti+fnsize,paint);
            i=1;
            while(i*cell<(imageh)) {
                canvas.drawLine(0,  starti+i * cell, width,  starti+i * cell, paint);
                str2= String.valueOf(i);
                t=i-1;
                canvas.drawText(str2,5,starti+t*cell+fnsize,paint);
                i++;
            }
            t=i-1;
            str2= String.valueOf(i);
            canvas.drawText(str2,5,starti+t*cell+fnsize,paint);
        }

        //new version diogonal grids

       if(flag_diagonal==1){
           // grid lines(\\)
           i=0;  //i for x axis
           int j=0;   //j for y axis
           while(cell*i<=imageh && cell*j<=width) {
               canvas.drawLine(0,cell*j+starti,cell*i,starti,paint);
               i++;
               j++;
           }
           while(cell*j<=imageh){
               canvas.drawLine(0,cell*j+starti,width,cell*j+starti-width,paint);
               j++;
           }

           while(cell*i<=width){
               canvas.drawLine(cell*i-imageh,imageh+starti,cell*i,starti,paint);
               i++;
           }

           int offsetx=cell*j-imageh;
           if(offsetx==cell)
               offsetx=0;
           int offsety=cell*i-width;
           if(offsety==cell)
               offsety=0;

           if(imageh>width){
               i=0;
               while((i*cell+offsetx)<width){
                   int stopy=imageh+starti-(width-i*cell-offsetx);
                   canvas.drawLine(i*cell+offsetx,imageh+starti,width,stopy,paint);
                   i++;
               }
           }
           else{
               i=0;
               while((starti+cell*i+offsety)<(starti+imageh)){
                   int stopx=width-(imageh-cell*i-offsety);
                   canvas.drawLine(stopx,imageh+starti,width,starti+cell*i+offsety,paint);
                   i++;
               }
           }
           // grid lines(//)
           if(imageh>width){
               i=0;
               while(cell*i<width){
                   canvas.drawLine(cell*i,starti,width,starti+width-cell*i,paint);
                   i++;
               }
               i=0;
               while((width+i*cell)<=imageh){
                   canvas.drawLine(0,starti+i*cell,width,starti+width+i*cell,paint);
                   i++;
               }
               while(i*cell<imageh){
                   int stopx=(starti+imageh)-(starti+i*cell);
                   canvas.drawLine(0,starti+cell*i,stopx,starti+imageh,paint);
                   i++;
               }
           }
           else{
               i=0;
               while(cell*i<imageh){
                   int stopx=(starti+imageh)-(starti+i*cell);
                   canvas.drawLine(0,starti+cell*i,stopx,starti+imageh,paint);
                   i++;
               }
               i=0;
               while((imageh+i*cell)<=width){
                   canvas.drawLine(i*cell,starti,imageh+i*cell,starti+imageh,paint);
                   i++;
               }
               while(i*cell<width){
                   int stopy=starti+(width-i*cell);
                   canvas.drawLine(i*cell,starti,width,stopy,paint);
                   i++;
               }
           }
       }



    }
    public void drawAgain(){
        invalidate();
        requestLayout();
    }

}



