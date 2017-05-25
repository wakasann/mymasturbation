package com.mymasturbation.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wa on 17-5-24.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "mymasturbation_log.db"; //数据库名称
    private static final int DATABASE_VERSION = 1; //数据库版本
    public static final String TABLE_NAME = "mainlog";

    public static final String PACKAGE_NAME = "com.mymasturbation";//包名
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置


    public DatabaseHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + PACKAGE_NAME
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id integer primary key autoincrement,user_id integer,logdate DATE,start_time DATETIME,end_time DATETIME,created_at DATETIME,update_at DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列
        String sql = "DROP TABLE IF EXISTS "  + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;

    }

    //增加操作
     public long insert(String user_id,String logdate,String start_time,String end_time){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", user_id);
        cv.put("logdate", logdate);
        cv.put("start_time",start_time);
        cv.put("end_time",end_time);
        cv.put("created_at",str);
        cv.put("update_at",str);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    //delete
    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where =  "id = ?";
        String[] whereValue ={ Integer.toString(id) };
        db.delete(TABLE_NAME, where, whereValue);
    }

    //修改操作
    public void update(int id, String logdate,String start_time,String end_time) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd H:m:s");
        String str = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "id = ?";
        String[] whereValue = { Integer.toString(id) };
        ContentValues cv = new ContentValues();
        cv.put("logdate", logdate);
        cv.put("start_time",start_time);
        cv.put("end_time",end_time);
        cv.put("update_at",str);
        db.update(TABLE_NAME, cv, where, whereValue);

    }


}
