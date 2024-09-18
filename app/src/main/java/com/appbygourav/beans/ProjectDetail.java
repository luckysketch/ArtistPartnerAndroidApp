package com.appbygourav.beans;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectDetail {
    int id,width,height,color,stroke_width,cell_size;
    String name,reference_img,create_date,last_activity,modidate,createdate;
    int active_status;//0 for not completed or 1 for completed

    public ProjectDetail( int width, int height, String name, String reference_img) {
        this.active_status=0;
        this.width = width;
        this.height = height;
        this.color = Color.BLACK;
        this.stroke_width = 1;
        this.cell_size = 20;
        this.name = name;
        this.reference_img = reference_img;
        Date  date=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
        String date_string=formatter.format(date);

        this.create_date = date_string;
        this.last_activity = date_string;
    }

    public ProjectDetail() {
    }

    @Override
    public String toString() {
        return "ProjectDetail{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", color=" + color +
                ", stroke_width=" + stroke_width +
                ", cell_size=" + cell_size +
                ", name='" + name + '\'' +
                ", reference_img='" + reference_img + '\'' +
                ", create_date='" + create_date + '\'' +
                ", last_activity='" + last_activity + '\'' +
                ", active_status=" + active_status +
                '}';
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getActive_status() {
        return active_status;
    }

    public void setActive_status(int active_status) {
        this.active_status = active_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStroke_width() {
        return stroke_width;
    }

    public void setStroke_width(int stroke_width) {
        this.stroke_width = stroke_width;
    }

    public int getCell_size() {
        return cell_size;
    }

    public void setCell_size(int cell_size) {
        this.cell_size = cell_size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference_img() {
        return reference_img;
    }

    public void setReference_img(String reference_img) {
        this.reference_img = reference_img;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLast_activity() {
        return last_activity;
    }

    public void setLast_activity(String last_activity) {
        this.last_activity = last_activity;
    }

    public String getModidate() {
        return modidate;
    }

    public void setModidate(String modidate) {
        this.modidate = modidate;
    }
}
