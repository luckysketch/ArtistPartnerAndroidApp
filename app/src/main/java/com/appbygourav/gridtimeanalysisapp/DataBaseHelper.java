package com.appbygourav.gridtimeanalysisapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String COLUMN_MINUTES = "MINUTES";
    public static final String SESSION_ID = "SESSION_ID";
    Context cont;

    public static final String TABLE1 = "PROJECT_DETAIL";
    public static final String TABLE2 = "SESSION_DETAIL";
    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_WIDTH = "WIDTH";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_REFERENCE_IMG = "REFERENCE_IMG";
    public static final String COLUMN_CREATE_DATE = "CREATE_DATE";
    public static final String COLUMN_LAST_ACTIVITY = "LAST_ACTIVITY";
    public static final String COLUMN_COLOR = "COLOR";
    public static final String COLUMN_STROKE_WIDTH = "STROKE_WIDTH";
    public static final String COLUMN_CELL_SIZE = "CELL_SIZE";
    public static final String COLUMN_ACTIVE_STATUS = "ACTIVE_STATUS";

    public DataBaseHelper( Context context) {
        super(context, "artist.db", null, 1);
        cont = context;

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_string= " CREATE TABLE " + TABLE1 + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_NAME + " TEXT ,"
                + COLUMN_WIDTH + " INTEGER , "
                + COLUMN_HEIGHT + " INTEGER , "
                + COLUMN_REFERENCE_IMG + " TEXT , "
                + COLUMN_CREATE_DATE + " TEXT , "
                + COLUMN_LAST_ACTIVITY + " TEXT, "
                + COLUMN_COLOR + " INTEGER , "
                + COLUMN_STROKE_WIDTH + " INTEGER  , "
                + COLUMN_CELL_SIZE + " INTEGER ,"
                + COLUMN_ACTIVE_STATUS + " INTEGER )";
        db.execSQL(create_string);

        String create_table2=" CREATE TABLE "+ TABLE2 + " ( "
                + SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + PROJECT_ID +" INTEGER , "
                + COLUMN_CREATE_DATE + " TEXT , "
                + COLUMN_MINUTES + " TEXT , CONSTRAINT FK_PROJECT FOREIGN KEY ( "+PROJECT_ID+" ) REFERENCES "
                +TABLE1+ " ( "+COLUMN_ID+" ) ON DELETE CASCADE )";
        db.execSQL(create_table2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addOne(ProjectDetail projectDetail){

        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME,projectDetail.getName());
        cv.put(COLUMN_WIDTH,projectDetail.getWidth());
        cv.put(COLUMN_HEIGHT,projectDetail.getHeight());
        cv.put(COLUMN_REFERENCE_IMG,projectDetail.getReference_img());
        cv.put(COLUMN_CREATE_DATE,projectDetail.getCreate_date());
        cv.put(COLUMN_LAST_ACTIVITY,projectDetail.getLast_activity());
        cv.put(COLUMN_COLOR,projectDetail.getColor());
        cv.put(COLUMN_STROKE_WIDTH,projectDetail.getStroke_width());
        cv.put(COLUMN_CELL_SIZE,projectDetail.getCell_size());
        cv.put(COLUMN_ACTIVE_STATUS,projectDetail.getActive_status());

        SQLiteDatabase db=this.getWritableDatabase();
        long insert = db.insert(TABLE1, null, cv);
        db.close();
        return  insert;
    }
    //using in getting recently created project
    public int current(){
        int cur;
        String query="SELECT MAX( "+COLUMN_ID+" ) FROM " + TABLE1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            cur = cursor.getInt(0);
        }
        else{
            cur=-1;
        }
        db.close();
        cursor.close();
        return cur;
    }
    public int current_session(){
        int cur;
        String query="SELECT MAX( "+SESSION_ID+" ) FROM " + TABLE2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
           // cur = cursor.getInt(0);
            //int index=cursor.getColumnIndex("MAX");
            cur=cursor.getInt(0);
        }
        else{
            cur=-1;
        }
        db.close();
        cursor.close();
        return cur;
    }
    public int lastActiveSessionProjectId(){
       int max_session_id=current_session();
       SQLiteDatabase db=this.getReadableDatabase();
       String sql="SELECT "+PROJECT_ID+" FROM "+ TABLE2+ " WHERE "+SESSION_ID+" = "+max_session_id;
       Cursor cursor=db.rawQuery(sql,null);
       if(cursor.moveToFirst()){
           return cursor.getInt(0);
       }
       else{
           return -1;
       }

    }

    public ProjectDetail getProject(int id){
        ProjectDetail p1=new ProjectDetail();

        String query="SELECT * FROM " + TABLE1 + " WHERE "+COLUMN_ID+" = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
            p1.setId(cursor.getInt(0));
            p1.setName(cursor.getString(1));
            p1.setWidth(cursor.getInt(2));
            p1.setHeight(cursor.getInt(3));
            p1.setReference_img(cursor.getString(4));
            p1.setCreate_date(cursor.getString(5));
            p1.setLast_activity(cursor.getString(6));
            p1.setColor(cursor.getInt(7));
            p1.setStroke_width(cursor.getInt(8));
            p1.setCell_size(cursor.getInt(9));
            p1.setActive_status(cursor.getInt(10));

        db.close();
        cursor.close();
        return p1;
    }
    public List<SessionDetails> getAllSessions(int project_id){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql1="SELECT *FROM "+TABLE2+" WHERE "+PROJECT_ID+" = "+project_id;
        Cursor cursor = db.rawQuery(sql1, null);
        List<SessionDetails> list1=new ArrayList<SessionDetails>();
        if(cursor.moveToFirst()){
            do{
                SessionDetails s1=new SessionDetails();
                s1.date=cursor.getString(2);
                s1.minutes=cursor.getInt(3);
                s1.session_id=cursor.getInt(0);
                list1.add(s1);
            }while(cursor.moveToNext());
        }
        else{
            //didn't get any thing from database
        }
        db.close();
        return list1;
    }
    public List<ProjectDetail> getAllProjects(){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql1="SELECT *FROM "+TABLE1;
        Cursor cursor = db.rawQuery(sql1, null);
        List<ProjectDetail> list1=new ArrayList<ProjectDetail>();
        if(cursor.moveToFirst()){
            do{
                ProjectDetail p1=new ProjectDetail();
                p1.setId(cursor.getInt(0));
                p1.setName(cursor.getString(1));
                p1.setWidth(cursor.getInt(2));
                p1.setHeight(cursor.getInt(3));
                p1.setReference_img(cursor.getString(4));
                p1.setCreate_date(cursor.getString(5));
                p1.setLast_activity(cursor.getString(6));
                p1.setColor(cursor.getInt(7));
                p1.setStroke_width(cursor.getInt(8));
                p1.setCell_size(cursor.getInt(9));
                p1.setActive_status(cursor.getInt(10));
                list1.add(p1);
            }while(cursor.moveToNext());
        }
        else{
            //didn't get any thing from database
        }
        db.close();
        return list1;
    }
    //version 2
    public List<Integer> totalwork(){
        int raw,time;
        SQLiteDatabase db=this.getReadableDatabase();
        List<Integer> list=new ArrayList<>();
        String sql="SELECT COUNT(*),SUM( "+COLUMN_MINUTES+ " ), MAX( "+COLUMN_MINUTES+ " ) FROM "+TABLE2;
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            raw=cursor.getInt(0);
            time=cursor.getInt(1);
            list.add(raw);
            list.add(time);
            list.add(cursor.getInt(2));
        }
        sql="SELECT COUNT(*) FROM "+TABLE1;
        cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            list.add(cursor.getInt(0));
        }
        db.close();
       return list;
    }


    public void  addSession(SessionDetails s1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PROJECT_ID,s1.getId());
        cv.put(COLUMN_CREATE_DATE,s1.getDate());
        cv.put(COLUMN_MINUTES,s1.getMinutes());
        db.insert(TABLE2,null,cv);
        db.close();
    }
    public void sessionUpdate(int id,int minutes){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="UPDATE "+TABLE2+" SET "+COLUMN_MINUTES+" = "+minutes+" WHERE "+SESSION_ID+" = "+id;
        db.execSQL(sql);
        db.close();
    }
    public void deleteSession(SessionDetails s1){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DELETE FROM "+TABLE2+" WHERE "+SESSION_ID+ " = "+s1.getSession_id();
        db.execSQL(sql);
        db.close();
    }
    public void deleteProject(ProjectDetail p1){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DELETE FROM "+TABLE1+" WHERE "+COLUMN_ID+ " = "+p1.getId();
        db.execSQL(sql);
        String sql2="DELETE FROM "+TABLE2+" WHERE "+PROJECT_ID+ " = "+p1.getId();
        db.execSQL(sql2);
        db.close();
    }



    public void projectUpdate(int id,int color,int sw,int cell_size,String last_activity){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql1="UPDATE "+TABLE1+" SET "+COLUMN_COLOR+" = "+color+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql1);
        String sql2="UPDATE "+TABLE1+" SET "+COLUMN_STROKE_WIDTH+" = "+sw+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql2);
        String sql3="UPDATE "+TABLE1+" SET "+COLUMN_CELL_SIZE+" = "+cell_size+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql3);
        String sql4="UPDATE "+TABLE1+" SET "+COLUMN_LAST_ACTIVITY+" = " +" ' "+last_activity+" ' "+"WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql4);
        db.close();
    }

}
