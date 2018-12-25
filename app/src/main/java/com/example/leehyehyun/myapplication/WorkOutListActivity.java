package com.example.leehyehyun.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class WorkOutListActivity extends AppCompatActivity {
    private ListView list_view;
    private ArrayList<WorkOut> arrWorkOutList;
    private WorkoutListAdapter mWorkoutListAdapter;

    public WorkOutListActivity() {
    }
    public WorkOutListActivity(ListView list_view, ArrayList<WorkOut> arrWorkOutList) {
        this.list_view = list_view;
        this.arrWorkOutList = arrWorkOutList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
//                Toast.makeText(WorkOutListActivity.this, "운동 종료 / "+mWorkoutListAdapter.getArrTodayWorkoutStr(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WorkOutListActivity.this, CompleteActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("arr_today_workout",(Serializable) mWorkoutListAdapter.getArrTodayWorkout());
                intent.putExtra("bundle",args);
                intent.putExtra("today",new Date().getTime());
                startActivity(intent);
                mWorkoutListAdapter.clearArrTodayWorkout();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        list_view = (ListView)findViewById(R.id.list_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        arrWorkOutList = (ArrayList<WorkOut>) args.getSerializable("arr_workout_list");

        mWorkoutListAdapter = new WorkoutListAdapter();
        mWorkoutListAdapter.setArrWorkOutList(arrWorkOutList);
        list_view.setAdapter(mWorkoutListAdapter);

    }

}
