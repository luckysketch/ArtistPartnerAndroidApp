package com.appbygourav.DataBaseService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appbygourav.Service.CommonUtility;
import com.appbygourav.beans.ProjectDetail;
import com.appbygourav.beans.SessionDetails;
import com.appbygourav.beans.WorkBean;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String COLUMN_MINUTES = "MINUTES";
    public static final String SESSION_ID = "SESSION_ID";
    Context cont;

    public static final String PROJECT_DETAIL = "PROJECT_DETAIL";
    public static final String SESSION_DETAIL = "SESSION_DETAIL";
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
    public static final String MODIDATE = "MODIDATE";
    public static final String CREATEDATE = "CREATEDATE";

    public DataBaseHelper( Context context) {
        super(context, "artist.db", null, 2);
        cont = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_string= " CREATE TABLE " + PROJECT_DETAIL + " ( "
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
                + COLUMN_ACTIVE_STATUS + " INTEGER ,"
                + CREATEDATE + " TEXT ,"
                + MODIDATE + " TEXT )";
        db.execSQL(create_string);

        String create_table2=" CREATE TABLE "+ SESSION_DETAIL + " ( "
                + SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + PROJECT_ID +" INTEGER , "
                + COLUMN_CREATE_DATE + " TEXT , "
                + COLUMN_MINUTES + " TEXT, "
                + CREATEDATE + " TEXT ,"
                + MODIDATE + " TEXT ,"
                +" CONSTRAINT FK_PROJECT FOREIGN KEY ( "+PROJECT_ID+" ) REFERENCES "
                +PROJECT_DETAIL+ " ( "+COLUMN_ID+" ) ON DELETE CASCADE )";

        db.execSQL(create_table2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            String  sql1 = "ALTER TABLE "+SESSION_DETAIL+" ADD COLUMN "+CREATEDATE+" TEXT";
            String  sql2 = "ALTER TABLE "+PROJECT_DETAIL+" ADD COLUMN "+CREATEDATE+" TEXT";
            String  sql3 = "ALTER TABLE "+SESSION_DETAIL+" ADD COLUMN "+MODIDATE+" TEXT";
            String  sql4 = "ALTER TABLE "+PROJECT_DETAIL+" ADD COLUMN "+MODIDATE+" TEXT";
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.execSQL(sql3);
            db.execSQL(sql4);
        }
    }

    public long addNewProject(ProjectDetail projectDetail){

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
        cv.put(CREATEDATE,projectDetail.getCreatedate());
        cv.put(MODIDATE,projectDetail.getModidate());

        SQLiteDatabase db=this.getWritableDatabase();
        long insert = db.insert(PROJECT_DETAIL, null, cv);
        db.close();
        return  insert;
    }
    //using in getting recently created project
    public int current(){
        int cur;
        String query="SELECT MAX( "+COLUMN_ID+" ) FROM " + PROJECT_DETAIL;
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

    public ProjectDetail getProject(int id){
        ProjectDetail p1=new ProjectDetail();

        String query="SELECT * FROM " + PROJECT_DETAIL + " WHERE "+COLUMN_ID+" = "+id;
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
//            p1.setCreatedate(cursor.getString(11));
//            p1.setModidate(cursor.getString(12));

        db.close();
        cursor.close();
        return p1;
    }
    public List<SessionDetails> getAllSessions(int project_id){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql1="SELECT *FROM "+SESSION_DETAIL+" WHERE "+PROJECT_ID+" = "+project_id+" ORDER BY "+MODIDATE+" DESC";
        Cursor cursor = db.rawQuery(sql1, null);
        List<SessionDetails> list1=new ArrayList<SessionDetails>();
        if(cursor.moveToFirst()){
            do{
                SessionDetails s1=new SessionDetails();
                s1.setSessionId(cursor.getInt(0));
                s1.setDate(cursor.getString(2));
                s1.setTimeSeconds(cursor.getInt(3));
                s1.setCreatedate(cursor.getString(4));
                s1.setModidate(cursor.getString(5));
                list1.add(s1);
            }while(cursor.moveToNext());
        }
        db.close();
        return list1;
    }
    public List<ProjectDetail> getProjectList(int activeStatus){
        SQLiteDatabase db=this.getReadableDatabase();
//        String sql1="SELECT *FROM "+PROJECT_DETAIL+" WHERE "+COLUMN_ACTIVE_STATUS+" = "+activeStatus+ " ORDER BY "+PROJECT_ID+ " DESC";
        String sql1="SELECT *FROM "+PROJECT_DETAIL+" WHERE "+COLUMN_ACTIVE_STATUS+" = "+activeStatus+ " ORDER BY "+MODIDATE+ " DESC";
        Cursor cursor = db.rawQuery(sql1, null);
        List<ProjectDetail> activeProjectList=new ArrayList<ProjectDetail>();
        if(cursor.moveToFirst()){
            do{
                ProjectDetail p1=getProjectBeanFromCursor(cursor);
                activeProjectList.add(p1);
            }while(cursor.moveToNext());
        }
        db.close();
        return activeProjectList;
    }

    private ProjectDetail getProjectBeanFromCursor(Cursor cursor){
        ProjectDetail projectBean=new ProjectDetail();
        projectBean.setId(cursor.getInt(0));
        projectBean.setName(cursor.getString(1));
        projectBean.setWidth(cursor.getInt(2));
        projectBean.setHeight(cursor.getInt(3));
        projectBean.setReference_img(cursor.getString(4));
        projectBean.setCreate_date(cursor.getString(5));
        projectBean.setLast_activity(cursor.getString(6));
        projectBean.setColor(cursor.getInt(7));
        projectBean.setStroke_width(cursor.getInt(8));
        projectBean.setCell_size(cursor.getInt(9));
        projectBean.setActive_status(cursor.getInt(10));
//        projectBean.setCreatedate(cursor.getString(11));
//        projectBean.setModidate(cursor.getString(12));
        return projectBean;
    }
    public WorkBean getWorkBean(){
        WorkBean workBean = new WorkBean();
        getWorkDoneForSession(workBean);
        getWorkDoneForProject(workBean);
        return workBean;
    }

    private void getWorkDoneForSession(WorkBean workBean){

        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT SUM( "+COLUMN_MINUTES+ " ), COUNT(*), MAX( "+COLUMN_MINUTES +") FROM "+SESSION_DETAIL;
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            workBean.setTotalSeconds(cursor.getLong(0));
            workBean.setTotalSessions(cursor.getInt(1));
            workBean.setMaxSessionTime(cursor.getInt(2));
        }
        db.close();
    }
    private void getWorkDoneForProject(WorkBean workBean){
        long maxTime=0;
        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT "+COLUMN_ID+", SUM( "+COLUMN_MINUTES+ " ) FROM "+SESSION_DETAIL+
                " LEFT JOIN "+PROJECT_DETAIL+" ON "+PROJECT_ID +" = "+COLUMN_ID +" GROUP BY "+COLUMN_ID;
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getLong(1)>maxTime){
                    maxTime = cursor.getLong(1);
                }
            }while(cursor.moveToNext());
        }
        String sql2 = "SELECT COUNT(*) FROM "+PROJECT_DETAIL;
        Cursor cursor2=db.rawQuery(sql2,null);
        if(cursor2.moveToFirst()){
            workBean.setTotalProject(cursor2.getInt(0));
        }
        db.close();
        workBean.setMaxProjectTime(maxTime);
    }

    public long addSession(SessionDetails s1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PROJECT_ID,s1.getProjectId());
        cv.put(COLUMN_CREATE_DATE,s1.getDate());
        cv.put(COLUMN_MINUTES,s1.getTimeSeconds());
        cv.put(CREATEDATE,s1.getCreatedate());
        cv.put(MODIDATE,s1.getModidate());
        long savedSessionId=db.insert(SESSION_DETAIL,null,cv);
        db.close();
        return savedSessionId;
    }
    public void sessionUpdate(int id,int minutes,String modidate){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="UPDATE "+SESSION_DETAIL+" SET "+COLUMN_MINUTES+" = "+minutes+" WHERE "+SESSION_ID+" = "+id;
        String sql2="UPDATE "+SESSION_DETAIL+" SET "+MODIDATE+" = "+"'"+modidate+"'"+" WHERE "+SESSION_ID+" = "+id;
        db.execSQL(sql);
        db.execSQL(sql2);
        db.close();
    }
    public void deleteSession(SessionDetails s1){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DELETE FROM "+SESSION_DETAIL+" WHERE "+SESSION_ID+ " = "+s1.getSessionId();
        db.execSQL(sql);
        db.close();
    }
    public void deleteProject(ProjectDetail p1){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DELETE FROM "+PROJECT_DETAIL+" WHERE "+COLUMN_ID+ " = "+p1.getId();
        db.execSQL(sql);
        String sql2="DELETE FROM "+SESSION_DETAIL+" WHERE "+PROJECT_ID+ " = "+p1.getId();
        db.execSQL(sql2);
        db.close();
    }



    public void projectUpdate(int id,int color,int sw,int cell_size,String lastActivity,String modidate){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql1="UPDATE "+PROJECT_DETAIL+" SET "+COLUMN_COLOR+" = "+color+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql1);
        String sql2="UPDATE "+PROJECT_DETAIL+" SET "+COLUMN_STROKE_WIDTH+" = "+sw+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql2);
        String sql3="UPDATE "+PROJECT_DETAIL+" SET "+COLUMN_CELL_SIZE+" = "+cell_size+" WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql3);
        String sql4="UPDATE "+PROJECT_DETAIL+" SET "+COLUMN_LAST_ACTIVITY+" = " +"'"+lastActivity+"'"+"WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql4);
        String sql5="UPDATE "+PROJECT_DETAIL+" SET "+MODIDATE+" = " +"'"+modidate+"'"+"WHERE "+COLUMN_ID+" = "+id;
        db.execSQL(sql5);
        db.close();
    }

    public void ChangeProjectStatus(int status,int projectId){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql1="UPDATE "+PROJECT_DETAIL+" SET "+COLUMN_ACTIVE_STATUS+" = "+status+" WHERE "+COLUMN_ID+" = "+projectId;
        db.execSQL(sql1);
    }

}
