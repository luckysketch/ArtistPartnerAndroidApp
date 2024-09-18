package com.appbygourav.gridtimeanalysisapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawView extends View {
    private double ratio;  //width:height
    private int width_paper;   //width of paper( int mm)
    private int cell_size;   //mm me
    private int line_width;   //stroke width

    private int color=Color.BLACK;

    private Boolean flag_diagonal=false;
    private Boolean flag_normalgrid=true;

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

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(line_width);

        int viewWidthPx= getWidth(); //width of custom view
        int viewHeightPx = getHeight();  //height of custom view
        int cellHeightPx = (int)((double)cell_size * ((double)viewWidthPx/(double)width_paper));
        int imageHeight= (int) (this.ratio*(double)viewWidthPx); //height of image
        int drawViewTopPos=(viewHeightPx-imageHeight)/2;

        if(this.getFlag_normalgrid()!=null && this.getFlag_normalgrid()){
            drawVerticalGrid(canvas,cellHeightPx,viewWidthPx,drawViewTopPos,imageHeight,paint);
        }
        if(this.getFlag_diagonal()!=null && this.getFlag_diagonal()){
           drawDiagonalGrid(canvas,cellHeightPx,viewWidthPx,drawViewTopPos,imageHeight,paint);
        }
    }

    private void drawVerticalGrid(Canvas canvas,int cellHeightPx,int viewWidthPx,int drawViewTopPos,int imageHeight,Paint paint) {
        int i=1;
        while(i*cellHeightPx<viewWidthPx){
            canvas.drawLine(i * cellHeightPx, drawViewTopPos+0, i * cellHeightPx, drawViewTopPos +imageHeight, paint);
            i++;
        }
        i=1;
        while(i*cellHeightPx<(imageHeight)) {
            canvas.drawLine(0,  drawViewTopPos+i * cellHeightPx, viewWidthPx,  drawViewTopPos+i * cellHeightPx, paint);
            i++;
        }
    }

    private void drawDiagonalGrid(Canvas canvas,int cellHeightPx,int viewWidthPx,int drawViewTopPos,int imageHeight,Paint paint) {
        // grid lines(\\)
        int i=0;  //i for x axis
        int j=0;   //j for y axis
        while(cellHeightPx*i<=imageHeight && cellHeightPx*j<=viewWidthPx) {
            canvas.drawLine(0,cellHeightPx*j+drawViewTopPos,cellHeightPx*i,drawViewTopPos,paint);
            i++;
            j++;
        }
        while(cellHeightPx*j<=imageHeight){
            canvas.drawLine(0,cellHeightPx*j+drawViewTopPos,viewWidthPx,cellHeightPx*j+drawViewTopPos-viewWidthPx,paint);
            j++;
        }
        while(cellHeightPx*i<=viewWidthPx){
            canvas.drawLine(cellHeightPx*i-imageHeight,imageHeight+drawViewTopPos,cellHeightPx*i,drawViewTopPos,paint);
            i++;
        }

        int offsetx=cellHeightPx*j-imageHeight;
        if(offsetx==cellHeightPx)
            offsetx=0;
        int offsety=cellHeightPx*i-viewWidthPx;
        if(offsety==cellHeightPx)
            offsety=0;

        if(imageHeight>viewWidthPx){
            i=0;
            while((i*cellHeightPx+offsetx)<viewWidthPx){
                int stopy=imageHeight+drawViewTopPos-(viewWidthPx-i*cellHeightPx-offsetx);
                canvas.drawLine(i*cellHeightPx+offsetx,imageHeight+drawViewTopPos,viewWidthPx,stopy,paint);
                i++;
            }
        }
        else{
            i=0;
            while((drawViewTopPos+cellHeightPx*i+offsety)<(drawViewTopPos+imageHeight)){
                int stopx=viewWidthPx-(imageHeight-cellHeightPx*i-offsety);
                canvas.drawLine(stopx,imageHeight+drawViewTopPos,viewWidthPx,drawViewTopPos+cellHeightPx*i+offsety,paint);
                i++;
            }
        }
        // grid lines(//)
        if(imageHeight>viewWidthPx){
            i=0;
            while(cellHeightPx*i<viewWidthPx){
                canvas.drawLine(cellHeightPx*i,drawViewTopPos,viewWidthPx,drawViewTopPos+viewWidthPx-cellHeightPx*i,paint);
                i++;
            }
            i=0;
            while((viewWidthPx+i*cellHeightPx)<=imageHeight){
                canvas.drawLine(0,drawViewTopPos+i*cellHeightPx,viewWidthPx,drawViewTopPos+viewWidthPx+i*cellHeightPx,paint);
                i++;
            }
            while(i*cellHeightPx<imageHeight){
                int stopx=(drawViewTopPos+imageHeight)-(drawViewTopPos+i*cellHeightPx);
                canvas.drawLine(0,drawViewTopPos+cellHeightPx*i,stopx,drawViewTopPos+imageHeight,paint);
                i++;
            }
        }
        else{
            i=0;
            while(cellHeightPx*i<imageHeight){
                int stopx=(drawViewTopPos+imageHeight)-(drawViewTopPos+i*cellHeightPx);
                canvas.drawLine(0,drawViewTopPos+cellHeightPx*i,stopx,drawViewTopPos+imageHeight,paint);
                i++;
            }
            i=0;
            while((imageHeight+i*cellHeightPx)<=viewWidthPx){
                canvas.drawLine(i*cellHeightPx,drawViewTopPos,imageHeight+i*cellHeightPx,drawViewTopPos+imageHeight,paint);
                i++;
            }
            while(i*cellHeightPx<viewWidthPx){
                int stopy=drawViewTopPos+(viewWidthPx-i*cellHeightPx);
                canvas.drawLine(i*cellHeightPx,drawViewTopPos,viewWidthPx,stopy,paint);
                i++;
            }
        }
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

    public Boolean getFlag_diagonal() {
        return flag_diagonal;
    }

    public void setFlag_diagonal(Boolean flag_diagonal) {
        this.flag_diagonal = flag_diagonal;
    }

    public Boolean getFlag_normalgrid() {
        return flag_normalgrid;
    }

    public void setFlag_normalgrid(Boolean flag_normalgrid) {
        this.flag_normalgrid = flag_normalgrid;
    }
}



