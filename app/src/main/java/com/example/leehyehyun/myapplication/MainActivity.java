package com.example.leehyehyun.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int ADDED_CHALLENGE = 126;

    private ListView list_view;
    private MainListAdapter mainListAdapter;

    private ArrayList<Challenge> arrChallenge;
//    private ArrayList<WorkOut> arrSelectedWorkout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                return true;
            case R.id.workout_start:

                if(mainListAdapter.getSelectedChallenges().size() == 0){
                    Toast.makeText(MainActivity.this, "챌린지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent = new Intent(MainActivity.this, WorkOutListActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("arr_workout_list", getWorkouts_ofSelectedChallenge());
                intent.putExtra("bundle",args);
                startActivity(intent);
//                finish();

                return true;
            case R.id.add_challenge:
                Intent intent2 = new Intent(MainActivity.this, AddChallengeActivity.class);
                startActivityForResult(intent2, ADDED_CHALLENGE);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = findViewById(R.id.list_view);

        mainListAdapter = new MainListAdapter();
        showChallengeList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDED_CHALLENGE && resultCode == RESULT_OK && data != null){

            String challengeName = data.getStringExtra("challenge_name");
            ArrayList<WorkOut> arrWorkout = (ArrayList<WorkOut>)data.getSerializableExtra("arr_workout");

            // db에 추가하려는 챌린지 정보 넣기
            addChallenge_inDB(challengeName, arrWorkout);

            // 챌린지 리스트 갱신
            showChallengeList();
        }
    }

    private void addChallenge_inDB(String challengeName, ArrayList<WorkOut> arrWorkOut){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());

        // challenge 테이블에 먼저 챌린지 추가하기
        mDBHelper.insertChallenge(challengeName);

        // challenge 테이블에 마지막 추가된 챌린지id 읽어오기
        int challenge_id = mDBHelper.getLastInsertId_challengeTable();
        Log.v("is-", "challenge table last insert id : "+challenge_id);

        // workout 테이블에 운동이름, 이미지경로, 챌린지id 넣어서 추가하기
        mDBHelper.insertWorkout(arrWorkOut, challenge_id);

    }

    private void getChallenge_inDB(){
        DBHelper mDBHelper = DBHelper.getInstance(getApplicationContext());
        arrChallenge = mDBHelper.getArrChallenge();
    }

    private void showChallengeList(){
        //초기화 (챌린지 추가시, 중복으로 추가됨 방지)
        mainListAdapter.clearArrChallenge();

        // db에 저장된 챌린지 가져와서 리스트에 노출시키기
        getChallenge_inDB();
        mainListAdapter.setChallengeList(arrChallenge);
        list_view.setAdapter(mainListAdapter);
    }

    // 선택된 챌린지에 해당하는 workout 을 array에 넣기
    private ArrayList<WorkOut> getWorkouts_ofSelectedChallenge(){
        ArrayList<WorkOut> arrSelectedWorkout = new ArrayList<>();
        ArrayList<Challenge> arrSelectedChallenge = mainListAdapter.getSelectedChallenges();
        for (int i = 0 ; i < arrSelectedChallenge.size() ; i++){
            arrSelectedWorkout.addAll(arrSelectedChallenge.get(i).getArrWorkOut());
        }
        return arrSelectedWorkout;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unselectedList();
    }

    private void unselectedList(){
        mainListAdapter.clearSelectedChallenges();
        for(int i = 0 ; i < mainListAdapter.getCount() ; i++){
            mainListAdapter.getItem(i).setChecked(false);
        }
        mainListAdapter.notifyDataSetChanged();
    }
}
