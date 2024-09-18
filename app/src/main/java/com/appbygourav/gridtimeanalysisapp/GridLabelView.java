package com.appbygourav.gridtimeanalysisapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GridLabelView extends View {

    private double ratio;  //width:height
    private int width_paper;   //width of paper( int mm)
    private int cell_size;   //mm
    private int line_width;   //stroke width
    private int color= Color.BLACK;

    private int zoomOffsetFromTop=0;
    private int zoomOffsetFromLeft=0;
    private int paddingLeft=10;
    private int paddingConstant=10;

    private float drawViewTopPos;


    public GridLabelView(Context context) {
        super(context);
        init(null);
    }
    public GridLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GridLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set) {
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLabels(canvas);
    }

    public void drawLabels(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(this.color);
        paint.setStrokeWidth(line_width);

        int viewWidthPx= getWidth(); //width of custom view
        int viewHeightPx = getHeight();  //height of custom view
        int cellHeightPx = (int)((double)cell_size * ((double)viewWidthPx/(double)width_paper));
        int imageHeight= (int) (this.ratio *(double)viewWidthPx); //height of image
        this.drawViewTopPos=(viewHeightPx-imageHeight)/2;

        int fontSize = cellHeightPx/6;
        paint.setTextSize(fontSize);
        int i=1;
        int t = 0;
        String topLabel="";
        String sideLabel="";
        while(i*cellHeightPx<viewWidthPx){
            topLabel= String.valueOf(i);
            t=i-1;
            if(i!=1)
                canvas.drawText(topLabel,t * cellHeightPx+ paddingLeft,drawViewTopPos+fontSize+zoomOffsetFromTop,paint);
            i++;
        }
        t=i-1;
        topLabel= String.valueOf(i);
        canvas.drawText(topLabel,t * cellHeightPx+ paddingLeft,drawViewTopPos+fontSize+zoomOffsetFromTop,paint);
        i=1;
        while(i*cellHeightPx<(imageHeight)) {
            sideLabel= String.valueOf(i);
            t=i-1;
            if(i!=1){
                canvas.drawText(sideLabel,this.paddingLeft+zoomOffsetFromLeft,drawViewTopPos+t*cellHeightPx+fontSize,paint);
            }
            i++;
        }
        t=i-1;
        sideLabel= String.valueOf(i);
        canvas.drawText(sideLabel,this.paddingLeft+zoomOffsetFromLeft,drawViewTopPos+t*cellHeightPx+fontSize,paint);
    }

    public void moveLabelOnZoom(float dx,float maxDx,float dy,float maxDy,float scale){
        float dxDiff = maxDx-dx;
        float dyDiff = maxDy-dy;
        this.setZoomOffsetFromLeft((int)(dxDiff/scale));
        this.paddingLeft=(int)((float)this.paddingConstant/scale);
        if(scale==1){
            this.setZoomOffsetFromTop(0);
        }else{
            float topPositionWithScale = this.drawViewTopPos*scale;
            if(dyDiff-topPositionWithScale>0){
                this.setZoomOffsetFromTop((int)((dyDiff-topPositionWithScale)/scale));
            }
        }
        this.drawAgain();
    }
    public void drawAgain(){
        invalidate();
        requestLayout();
    }
    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getWidth_paper() {
        return width_paper;
    }

    public void setWidth_paper(int width_paper) {
        this.width_paper = width_paper;
    }

    public int getCell_size() {
        return cell_size;
    }

    public void setCell_size(int cell_size) {
        this.cell_size = cell_size;
    }

    public int getLine_width() {
        return line_width;
    }

    public void setLine_width(int line_width) {
        this.line_width = line_width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getZoomOffsetFromTop() {
        return zoomOffsetFromTop;
    }

    public void setZoomOffsetFromTop(int zoomOffsetFromTop) {
        this.zoomOffsetFromTop = zoomOffsetFromTop;
    }

    public int getZoomOffsetFromLeft() {
        return zoomOffsetFromLeft;
    }

    public void setZoomOffsetFromLeft(int zoomOffsetFromLeft) {
        this.zoomOffsetFromLeft = zoomOffsetFromLeft;
    }
}
