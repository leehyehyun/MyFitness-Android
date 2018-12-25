package com.example.leehyehyun.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompleteActivity extends AppCompatActivity {
    private ArrayList<WorkOut> arrTodayWorkOut;
    private LinearLayout addviewArea;
    private ScrollView addviewScroll;
    private Date selectedDate;
    private final SimpleDateFormat mSimpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private TextView todayTextView;
    private TextView btnDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        arrTodayWorkOut = (ArrayList<WorkOut>) args.getSerializable("arr_today_workout");
        long todayDateLong = intent.getLongExtra("today",0l);
        Date todayDate = new Date(todayDateLong);

        addTodayWorkOutDB(arrTodayWorkOut);
        //db에 저장 insert into (todayDate, arrTodayWorkOut.get(i).getName()) from 테이블명;


        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        todayTextView = findViewById(R.id.today_date);
        addviewArea = findViewById(R.id.addview_area);
        addviewScroll = findViewById(R.id.addview_scroll);
        btnDelete = findViewById(R.id.btn_delete);

        if(GlobalValue.isAdmin){
            btnDelete.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.GONE);
        }

        addview(todayDate);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date leftDate = new Date(selectedDate.getTime()-(long)1000*60*60*24);
                if(getSelectedDBrowCount(leftDate) == 0){
                    Log.v("is-",mSimpleFormatter.format(leftDate)+" : 해당 날짜에 기록이 없음");
                    Toast.makeText(CompleteActivity.this, "기록이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    selectedDate = leftDate;
                    addview(selectedDate);
                }
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todayTextView.getText().toString().equalsIgnoreCase(mSimpleFormatter.format(new Date()))){
                    return;
                }
                selectedDate = new Date(selectedDate.getTime()+(long)1000*60*60*24);
                addview(selectedDate);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedDBrows(selectedDate);
            }
        });


    }

    private void showSelectedDate(Date selectedDate){
        this.selectedDate = selectedDate;
        todayTextView.setText(mSimpleFormatter.format(selectedDate)); // 선택된 날짜 표시하기

    }

    private void addTodayWorkOutDB(ArrayList<WorkOut> arrTodayWorkOut){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());
        mDBHelper.insertWorkoutRecord(arrTodayWorkOut);
    }

    private int getSelectedDBrowCount(Date selectedDate){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());
        int count = mDBHelper.getWokroutRecordCount_ofSelectedDate(selectedDate);
        return count;
    }

    private ArrayList<WorkOut> getDBdata(Date selectedDate){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());
        ArrayList<WorkOut> arr = mDBHelper.getArrWokroutRecord_ofSelectedDate(selectedDate);
        return arr;
    }

    private void deleteSelectedDBrows(Date selectedDate){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());
        mDBHelper.deleteWorkoutRecord(selectedDate);
        addview(selectedDate);
    }

    private void addview(Date selectedDate){
        showSelectedDate(selectedDate);

        ArrayList<WorkOut> arrSelectedDateWorkOut = getDBdata(selectedDate);

        addviewArea.removeAllViews();
        addviewScroll.setVisibility(View.GONE);
        for (int i = 0 ; i < arrSelectedDateWorkOut.size() ; i++){
            View view = getLayoutInflater().inflate(R.layout.item_complete, null, false);
            TextView tv = view.findViewById(R.id.textview2);
            tv.setText(arrSelectedDateWorkOut.get(i).getName());
            addviewArea.addView(view);
        }
        if(arrSelectedDateWorkOut.size() == 0){
            addviewScroll.setVisibility(View.GONE);
        }else{
            addviewScroll.setVisibility(View.VISIBLE);
        }
    }
}
