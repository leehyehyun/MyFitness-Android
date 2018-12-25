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
    static int version = 1;
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
            instance = new DBHelper(context, "hye_hometrainng.db", null, version);
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
            db.execSQL("CREATE TABLE IF NOT EXISTS  challenge (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS  workout (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, img_path TEXT, challenge_id INTEGER);");
            db.execSQL("CREATE TABLE IF NOT EXISTS  workout_record (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date_str TEXT);");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("is-","db onUpgrade / oldVer : "+oldVersion+" / newVer : "+newVersion);
    }

    public void insertWorkoutRecord(ArrayList<WorkOut> arrTodayWorkOut){
        for (int i = 0 ; i < arrTodayWorkOut.size() ; i++){
            try{
                sqldb.beginTransaction();
                try{
                    ContentValues value = new ContentValues();
                    Cursor cursor = null;
                    try{
                        String selectedDateStr = mSimpleFormatter.format(new Date().getTime());
                        //row행 추가 (insert)
                        value.put("name",arrTodayWorkOut.get(i).getName());
                        value.put("date_str",selectedDateStr);
                        sqldb.insert("workout_record",null, value);

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
                    Log.v("is-","insert row (total workout_record count) : "+getWokroutRecordCount());
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

    public void deleteWorkoutRecord(Date selectedDate){
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateStr = mSimpleFormatter.format(selectedDate);
                    cursor = sqldb.rawQuery("SELECT * FROM workout_record WHERE date_str = '"+selectedDateStr+"'", null);
                    if(cursor!=null && cursor.moveToFirst()){
                        do{
                            sqldb.delete("workout_record", "date_str = '" + selectedDateStr+"'", null);
                        }while(cursor.moveToNext());
                    }
                    Log.v("is-","delete row (total workout_record count) : "+cursor.getCount());
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

    public int getWokroutRecordCount(){
        int count = 0;
        ContentValues value = new ContentValues();
        Cursor cursor = null;
        try{
            cursor = sqldb.rawQuery("SELECT * FROM workout_record", null);
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

    public int getWokroutRecordCount_ofSelectedDate(Date selectedDate){
        int count = 0;
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateStr = mSimpleFormatter.format(selectedDate.getTime());
                    cursor = sqldb.rawQuery("SELECT * FROM workout_record WHERE date_str = '"+selectedDateStr+"'", null);
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

    public ArrayList<WorkOut> getArrWokroutRecord_ofSelectedDate(Date selectedDate){
        ArrayList<WorkOut> arrWorkout_ofSelectedDate = new ArrayList<>();
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String selectedDateStr = mSimpleFormatter.format(selectedDate.getTime());
                    cursor = sqldb.rawQuery("SELECT * FROM workout_record WHERE date_str = '"+selectedDateStr+"'", null);
                    if(cursor != null && cursor.moveToFirst()){
                        do{
                            int id = cursor.getInt(0);
                            String workoutName = cursor.getString(1);
                            String dateFormat = cursor.getString(2);
                            arrWorkout_ofSelectedDate.add(new WorkOut(workoutName, dateFormat));
                            Log.v("is-",id+" row : "+workoutName+" / date : "+dateFormat);
                        }while(cursor.moveToNext());
                        Log.v("is-","total workout_record count : "+arrWorkout_ofSelectedDate.size());
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
        return arrWorkout_ofSelectedDate;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 챌린지 테이블 행(row) insert
    public void insertChallenge(String challengeName){
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    //row행 추가 (insert)
                    value.put("name",challengeName);
                    sqldb.insert("challenge",null, value);
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
                Log.v("is-","insert row (total challenge count) : "+getChallengeCount());
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

    // 챌린지 테이블 행(row) 개수
    public int getChallengeCount(){
        int count = 0;
        ContentValues value = new ContentValues();
        Cursor cursor = null;
        try{
            cursor = sqldb.rawQuery("SELECT * FROM challenge", null);
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

    // 챌린지 테이블 행(row) 마지막 id 가져오기
    public int getLastInsertId_challengeTable(){
        int id = -1;
        ContentValues value = new ContentValues();
        Cursor cursor = null;
        try{
            cursor = sqldb.rawQuery("SELECT max(id) FROM challenge", null);
            if(cursor != null){
                cursor.moveToFirst();
                id = cursor.getInt(0);
            }
        }catch(Exception e){
            e.printStackTrace();

        }finally{
            value.clear();
            cursor.close();
        }
        return id;
    }

    public ArrayList<Challenge> getArrChallenge(){
        ArrayList<Challenge> arrChallenge = new ArrayList<>();
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    cursor = sqldb.rawQuery("SELECT * FROM challenge", null);
                    if(cursor != null && cursor.moveToFirst()){
                        do{
                            int id = cursor.getInt(0);
                            String challengeName = cursor.getString(1);
                            arrChallenge.add(new Challenge(id, challengeName));
                            Log.v("is-",id+" row : "+challengeName);
                        }while(cursor.moveToNext());
                        Log.v("is-","total challenge count : "+arrChallenge.size());
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
        return arrChallenge;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // workout 테이블 행(row) insert
    public void insertWorkout(ArrayList<WorkOut> arrWorkOut, int challenge_id){
        for (int i = 0 ; i < arrWorkOut.size() ; i++){
            try{
                sqldb.beginTransaction();
                try{
                    ContentValues value = new ContentValues();
                    Cursor cursor = null;
                    try{
                        //row행 추가 (insert)
                        value.put("name",arrWorkOut.get(i).getName());
                        value.put("img_path",arrWorkOut.get(i).getImagePath());
                        value.put("challenge_id",challenge_id);
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
                    Log.v("is-","insert row (total workout count) : "+getWokroutCount());
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

    // workout 테이블 행(row) 개수
    public int getWokroutCount(){
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

    // 선택된 챌린지의 workout list 가져오기
    public ArrayList<WorkOut> getArrWokrout_ofSelectedChallenge(ArrayList<Challenge> arrSelectedChallenge){
        ArrayList<WorkOut> arrWorkout_ofSelectedChallenge = new ArrayList<>();
        try{
            sqldb.beginTransaction();
            try{
                ContentValues value = new ContentValues();
                Cursor cursor = null;
                try{
                    String whereSentence = "";
                    for(int i = 0 ; i < arrSelectedChallenge.size() ; i++){
                        if(i==0){
                            whereSentence += "WHERE workout.challenge_id = "+arrSelectedChallenge.get(i).getId();
                        }else{
                            whereSentence += " OR workout.challenge_id = "+arrSelectedChallenge.get(i).getId();
                        }
                    }
                    String query = "SELECT * FROM workout LEFT JOIN challenge ON workout.challenge_id = challenge.id "+whereSentence;
                    cursor = sqldb.rawQuery(query, null);
                    if(cursor != null && cursor.moveToFirst()){
                        do{
                            int id = cursor.getInt(0);
                            String workoutName = cursor.getString(1);
                            String imgPath = cursor.getString(2);
                            arrWorkout_ofSelectedChallenge.add(new WorkOut(workoutName, imgPath, ""));
                            Log.v("is-",id+" row : "+workoutName+" / imgPath : "+imgPath);
                        }while(cursor.moveToNext());
                        Log.v("is-","total row count : "+arrWorkout_ofSelectedChallenge.size());
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
        return arrWorkout_ofSelectedChallenge;
    }


}
