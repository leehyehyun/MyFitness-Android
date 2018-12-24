package com.example.leehyehyun.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class AddChallengeActivity extends AppCompatActivity {
    private static final int ADDED_WORKOUT = 125;

    private EditText editChallengeName;
    private ListView list_view;
    private Button btn_add_workout;
    private RelativeLayout hidden_view;

    private ArrayList<WorkOut> arrWorkoutList = new ArrayList<>();

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
                if(editChallengeName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "챌린지 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(arrWorkoutList.size() == 0){
                    Toast.makeText(getApplicationContext(), "운동을 추가해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent = new Intent();
                intent.putExtra("challenge_name",editChallengeName.getText().toString());
                intent.putExtra("arr_workout",arrWorkoutList);
                setResult(RESULT_OK, intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AddChallengeListAdapter mListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editChallengeName = findViewById(R.id.edit_challenge_name);
        list_view = findViewById(R.id.list_view);
        btn_add_workout = findViewById(R.id.btn_add_workout);
        hidden_view = findViewById(R.id.hidden_view);

        mListAdapter = new AddChallengeListAdapter(AddChallengeActivity.this);
        list_view.setAdapter(mListAdapter);

        showHiddenView();

        btn_add_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업띄워서 운동 추가하기
                Intent intent = new Intent(AddChallengeActivity.this, AddWorkoutActivity.class);
                startActivityForResult(intent, ADDED_WORKOUT);

            }
        });

    }

    private void showHiddenView(){
        if(mListAdapter.getCount() == 0){
            hidden_view.setVisibility(View.VISIBLE);
        }else{
            hidden_view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDED_WORKOUT && resultCode == RESULT_OK && data != null) {
            String workoutName = data.getStringExtra("workoutName");
            String imagePath = data.getStringExtra("imagePath");
            mListAdapter.addItem(new WorkOut(workoutName, imagePath, ""));
            showHiddenView();

            arrWorkoutList = mListAdapter.getWorkoutList();
        }

    }




}
