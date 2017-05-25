package com.mymasturbation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import com.mymasturbation.common.DatabaseHelper;
import com.mymasturbation.common.MyDateHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView;
    private TextView textViewToday;
    private TextView textViewWeek;
    private TextView textViewMonth;

    private long exitTime = 0;

    private DatabaseHelper dbHelper;
    private Cursor mCursor;
    private ListView BooksList;

    private Button btn_once,btn_clock;
    private Toast tst;

    private int user_id = 64;
    private String logdate = "";
    private String start_time;
    private String end_time;
    private Boolean isTimer = false;
    private String home_tst_msg = "";

    private MyDateHelper myDateHelper;

    private int today_count = 0;
    private int week_count = 0;
    private int month_count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.current_time);

        textViewToday = (TextView)findViewById(R.id.today_count);
        textViewWeek = (TextView)findViewById(R.id.week_count);
        textViewMonth = (TextView)findViewById(R.id.month_count);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        String str="当前日期：" + sdf.format(new Date());
        textView.setText(str);
        myDateHelper = new MyDateHelper();
        setUpViews();

        btn_once = (Button) findViewById(R.id.remember_once);
        btn_clock = (Button) findViewById(R.id.remember_clock);
        btn_once.setOnClickListener(this);
        btn_clock.setOnClickListener(this);
    }


    public void onClick(View view){
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.remember_once:
                tst = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);

                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("你确定保存吗？")//设置显示的内容
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                logdate = sdf.format(new Date());
                                SimpleDateFormat sdfs =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                start_time = sdfs.format(new Date());
                                dbHelper.insert(user_id + "",logdate,start_time,start_time);
                                tst.show();
                                
                                //刷新数据
                                getHomeData();

                            }

                        }).setNegativeButton("否",new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                        Log.i("alertdialog"," 按了否");
                    }

                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.remember_clock:
                if(isTimer){

                    tst = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
                    new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题
                            .setMessage("你确定停止计时并保存吗？")//设置显示的内容
                            .setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                    logdate = sdf.format(new Date());
                                    SimpleDateFormat sdfs =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    end_time = sdfs.format(new Date());
                                    dbHelper.insert(user_id + "",logdate,start_time,end_time);
                                    tst.show();

                                    //重新启用记一次按钮
                                    btn_once.setClickable(true);
                                    btn_clock.setText("计时记录一次");
                                    isTimer = false;
                                    //刷新统计数据
                                    getHomeData();

                                }

                            }).setNegativeButton("否",new DialogInterface.OnClickListener() {//添加返回按钮

                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                            tst = Toast.makeText(MainActivity.this, "继续计时中...", Toast.LENGTH_SHORT);
                            tst.show();
                        }

                    }).show();//在按键响应事件中显示此对话框
                }else{
                    //开始记录
                    btn_once.setClickable(false);
                    btn_clock.setText("计时中....");
                    tst = Toast.makeText(this, "开始记时", Toast.LENGTH_SHORT);
                    SimpleDateFormat sdfs =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    start_time = sdfs.format(new Date());
                    tst.show();
                    isTimer = true;
                }

                break;
            default:
                break;
        }
    }

    public void setUpViews(){
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase sd = dbHelper.getWritableDatabase();
        sd.close();

//        dbHelper.insert("64","2017-05-20","2017-05-20 22:55:57","2017-05-20 23:05:46");
//        dbHelper.insert("64","2017-05-21","2017-05-21 03:09:12'","2017-05-21 03:09:12");
//        dbHelper.insert("64","2017-05-21","2017-05-21 16:58:14","2017-05-21 16:58:14");
//        dbHelper.insert("64","2017-05-23","2017-05-23 02:03:00","2017-05-23 02:09:00");
//        dbHelper.insert("64","2017-05-25","2017-05-25 01:22:00","2017-05-25 01:32:00");
        this.getHomeData();

    }

    public void getHomeData(){
        String today = myDateHelper.getTheDate();
        Map weekDay = myDateHelper.getWeekDay();
        Map monthDay = myDateHelper.getMonthDate();

        String today_sql = "SELECT count(*) as count FROM mainlog where user_id="+user_id+" AND logdate='"+today+"'";

        String week_sql  = "SELECT count(*) as count FROM mainlog where user_id="+user_id;
               week_sql  = week_sql + " AND logdate between '"+weekDay.get("mon")+"' AND ";
               week_sql  = week_sql + " '"+weekDay.get("sun")+"'";

        String  month_sql  = "SELECT count(*) as count FROM mainlog where user_id="+user_id;
                month_sql  = month_sql + " AND logdate between '"+monthDay.get("monthF")+"' AND ";
                month_sql  = month_sql + " '"+monthDay.get("monthL")+"'";


        SQLiteDatabase sd = dbHelper.getReadableDatabase();
        mCursor = sd.rawQuery(today_sql,new String[]{});
        while (mCursor.moveToNext()) {
            today_count = mCursor.getInt(mCursor.getColumnIndex("count"));

        }

        mCursor.close();

        mCursor = sd.rawQuery(week_sql,new String[]{});
        while (mCursor.moveToNext()) {
            week_count = mCursor.getInt(mCursor.getColumnIndex("count"));

        }
        mCursor.close();
        mCursor = sd.rawQuery(month_sql,new String[]{});
        while (mCursor.moveToNext()) {
            month_count = mCursor.getInt(mCursor.getColumnIndex("count"));

        }
        mCursor.close();

        sd.close();
        textViewToday.setText(" 今天:" + today_count);
        textViewWeek.setText(" 本周:"  + week_count + "("+weekDay.get("mon")+"-"+weekDay.get("sun")+")");
        textViewMonth.setText(" 本月:" + month_count+ "("+monthDay.get("monthF")+"-"+monthDay.get("monthL")+")");
        Log.i("alertdialog",weekDay.get("mon")+"--"+weekDay.get("sun"));
    }



    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
