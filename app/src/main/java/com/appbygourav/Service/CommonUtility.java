package com.appbygourav.Service;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;

import android.net.Uri;
import android.provider.MediaStore;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CommonUtility {
    public static final String  SCREEN_OFF_MODE="SCREEN_MODE_OFF";
    public static final String  LAST_ACTIVE_PROJECT_ID="LAST_ACTIVE_PROJECT_ID";
    public static final String SHARED_PREF="SHARED_PREF";
    public static final String TOTAL_COUNT_FOR_GRIDVIEW = "TOTAL_COUNT_FOR_GRIDVIEW";
    public static ArrayList<String> STANDARD_PAPER_SIZE = new ArrayList<>();
    public static ArrayList<String> PAPER_ORIENTATION_LIST = new ArrayList<>();
    public static SimpleDateFormat dbDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat oldDbDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    static {
        STANDARD_PAPER_SIZE.add("A5");
        STANDARD_PAPER_SIZE.add("A4");
        STANDARD_PAPER_SIZE.add("A3");
        STANDARD_PAPER_SIZE.add("A2");
        STANDARD_PAPER_SIZE.add("A1");
        PAPER_ORIENTATION_LIST.add("Vertical");
        PAPER_ORIENTATION_LIST.add("Horizontal");
    }
    public static HashMap<String,Integer> getSizeMapByPaper(String paperType){
        HashMap<String,Integer>  resultMap = new HashMap<>();
        if(paperType=="A5"){
            resultMap.put("width",148);
            resultMap.put("height",210);
        }else if(paperType=="A4"){
            resultMap.put("width",210);
            resultMap.put("height",297);
        }else if(paperType=="A3"){
            resultMap.put("width",297);
            resultMap.put("height",420);
        }else if(paperType=="A2"){
            resultMap.put("width",420);
            resultMap.put("height",594);
        }else if(paperType=="A1"){
            resultMap.put("width",594);
            resultMap.put("height",841);
        }
        return resultMap;
    }

    public static String getTimeStringFromSeconds(long totalTime){
        String timeStr="";
        if(totalTime>0){
            long hour = totalTime/3600;
            totalTime = totalTime%3600;
            long minutes = totalTime/60;
            totalTime = totalTime%60;
            long seconds = totalTime%60;
            if(hour>0){
                timeStr = String.valueOf(hour) + " hour ";
            }
            if(minutes>0){
                timeStr = timeStr + " "+ String.valueOf(minutes) + " min ";
            }
            if(hour==0 && minutes==0){
                timeStr = String.valueOf(seconds)+" sec ";
            }
        }
        return timeStr;
    }

    public static String getEnglishDateStrFromOldDbFormat(String dateStr){
        try {
            if(dateStr!=null && dateStr!=""){
                SimpleDateFormat inputFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = inputFormatter.parse(dateStr);
                SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy");
                String outputDate = outputFormatter.format(date);
                return outputDate;
            }
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        return dateStr;
    }
    public static String getDateStringFromDbFormat(String dateStr){
        try {
            if(dateStr!=null && dateStr!=""){
                Date date = dbDateFormatter.parse(dateStr);
                SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy");
                String outputDate = outputFormatter.format(date);
                return outputDate;
            }
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        return dateStr;
    }

    public static String getDateStrForDbUpdation(String dateStr){
        try {
            if(dateStr!=null && dateStr!=""){
                System.out.println("db before date : "+dateStr);
                SimpleDateFormat inputFormatter = new SimpleDateFormat("DD/MM/YYYY");
                Date date = inputFormatter.parse(dateStr);
                System.out.println("date : "+date!=null ? date.toString() : "null");
                if(date!=null){
                    String outputDate = dbDateFormatter.format(date);
                    System.out.println("db outputDate te : "+outputDate);
                    return outputDate;
                }
            }
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        return dateStr;
    }

    public static String getDateStrInDBFormat(Date date){
        if(date!=null){
            return dbDateFormatter.format(date);
        }
        return "";
    }
    public static String getDateStrInOldDBFormat(Date date){
        if(date!=null){
            return oldDbDateFormatter.format(date);
        }
        return "";
    }

    public static Boolean saveBitmapToGallery(Context context, Bitmap bitmap) {
        try {
            ContentValues values = new ContentValues();
            Date currDateTime = new Date();
            String fileName = "IMG_"+currDateTime.toString();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "GridImage"); // Save to a specific folder in Pictures
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                return true;
            }
        } catch (Exception e) {
            FirebaseCrashlyticsService.fireExceptionEvent(e,"LuckyGridView","saveBitmapToGallery","","saveToGallery");
        }
        return false;
    }
}
