package com.example.leehyehyun.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    static int version = 5;
    private static Context context;
    private static SQLiteDatabase sqldb;
    private static DBHelper instance;

    private final SimpleDateFormat mSimpleFormatter = new SimpleDateFormat("yyyy-MM-dd");


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.enableWriteAheadLogging();
        }
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context, "hye_hometrainng5.db", null, version);
            sqldb = instance.getWritableDatabase();
        }
        return instance;
    }

    public static SQLiteDatabase getSqlDb() {
        return sqldb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS  workout (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date_format TEXT);");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("is-","db onUpgrade / oldVer : "+oldVersion+" / newVer : "+newVersion);
    }

    public void insertDBRow(ArrayList<WorkOut> arrTodayWorkOut){
        for (int i = 0 ; i < arrTodayWorkOut.size() ; i++){
            try{
                sqldb.beginTransaction(); // 하나의 트랜잭션에서 처리하여 빠르게 동작할 수 있도록 한다.
                try{
                    ContentValues value = new ContentValues();
                    Cursor cursor = null;
                    try{
                        String selectedDateFormat = mSimpleFormatter.format(new Date().getTime());
                        //row행 추가 (insert)
                        value.put("name",arrTodayWorkOut.get(i).getName());
                        value.put("date_format",selectedDateFormat);
                        sqldb.insert("workout",null, value);

                    }catch(Exception e){
                        e.printStackTrace();

                    }finally{
                        value.clear();
                        cursor.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    Log.v("is-","insert row (total row count) : "+getWokroutRowCount());
                }catch(Exception e){
                    e.printStackTrace();
                }

                sqldb.setTransactionSuccessful();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                sqldb.endTransaction();
            }
        }
    }

    public void deleteDBRow(Date selectedDate){
        try{
            sqldb.beginTransaction(); // 하나의 트랜잭션에서 처리하여 빠르게 동작할 수 있도록 한다.
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateFormat = mSimpleFormatter.format(selectedDate);
                    cursor = sqldb.rawQuery("SELECT * FROM workout WHERE date_format = '"+selectedDateFormat+"'", null);
                    if(cursor!=null && cursor.moveToFirst()){
                        do{
                            sqldb.delete("workout", "date_format = '" + selectedDateFormat+"'", null);
                        }while(cursor.moveToNext());
                    }
                    Log.v("is-","delete row (total row count) : "+cursor.getCount());
                }catch(Exception e){
                    e.printStackTrace();

                }finally{
                    value.clear();
                    cursor.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            sqldb.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            sqldb.endTransaction();
        }
    }

    public int getWokroutRowCount(){
        int count = 0;
        ContentValues value = new ContentValues();
        Cursor cursor = null;
        try{
            cursor = sqldb.rawQuery("SELECT * FROM workout", null);
            if(cursor != null){
                count = cursor.getCount();
            }
        }catch(Exception e){
            e.printStackTrace();

        }finally{
            value.clear();
            cursor.close();
        }
        return count;
    }

    public int getSelectedDateWokroutRowCount(Date selectedDate){
        int count = 0;
        try{
            sqldb.beginTransaction(); // 하나의 트랜잭션에서 처리하여 빠르게 동작할 수 있도록 한다.
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateFormat = mSimpleFormatter.format(selectedDate.getTime());
                    cursor = sqldb.rawQuery("SELECT * FROM workout WHERE date_format = '"+selectedDateFormat+"'", null);
                    if(cursor != null){
                        count = cursor.getCount();
                    }
                }catch(Exception e){
                    e.printStackTrace();

                }finally{
                    value.clear();
                    cursor.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            sqldb.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            sqldb.endTransaction();
        }
        return count;
    }

    public ArrayList<WorkOut> getArrSelectedDateWorkout(Date selectedDate){
        ArrayList<WorkOut> arrSelectedDateWorkout = new ArrayList<WorkOut>();
        try{
            sqldb.beginTransaction(); // 하나의 트랜잭션에서 처리하여 빠르게 동작할 수 있도록 한다.
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateFormat = mSimpleFormatter.format(selectedDate.getTime());
                    cursor = sqldb.rawQuery("SELECT * FROM workout WHERE date_format = '"+selectedDateFormat+"'", null);
                    if(cursor != null && cursor.moveToFirst()){
                        do{
                            int id = cursor.getInt(0);
                            String workoutName = cursor.getString(1);
                            String dateFormat = cursor.getString(2);
                            arrSelectedDateWorkout.add(new WorkOut(workoutName, dateFormat));
                            Log.v("is-",id+" row : "+workoutName+" / date : "+dateFormat);
                        }while(cursor.moveToNext());
                        Log.v("is-","total row count : "+arrSelectedDateWorkout.size());
                    }else{
                    }
                }catch(Exception e){
                    e.printStackTrace();

                }finally{
                    value.clear();
                    cursor.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            sqldb.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            sqldb.endTransaction();
        }
        return arrSelectedDateWorkout;
    }

}
