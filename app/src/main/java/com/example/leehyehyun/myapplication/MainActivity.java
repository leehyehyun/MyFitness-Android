package com.example.leehyehyun.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ArrayList<Challenge> arrChallenge = new ArrayList<Challenge>();

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

                if(mainListAdapter.getArrWorkOutList().size() == 0){
                    Toast.makeText(MainActivity.this, "챌린지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent = new Intent(MainActivity.this, WorkOutListActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("arr_workout_list",(Serializable) mainListAdapter.getArrWorkOutList());
                intent.putExtra("bundle",args);
                startActivity(intent);
                for(int i = 0 ; i < mainListAdapter.getCount() ; i++){
                    mainListAdapter.setClear();
                    mainListAdapter.getItem(i).setChecked(false);
                }
                mainListAdapter.notifyDataSetChanged();
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

        list_view = (ListView)findViewById(R.id.list_view);

        mainListAdapter = new MainListAdapter();
        getArrWorkOut();
        getArrWorkOut2();
        getArrWorkOut3();
        for (int i = 0 ; i < arrChallenge.size() ; i++){
            mainListAdapter.addItem(arrChallenge.get(i));
        }
        list_view.setAdapter(mainListAdapter);

    }

    private ArrayList<WorkOut> getArrWorkOut(){
        ArrayList<WorkOut> arrWorkOut = new ArrayList<WorkOut>();
        arrWorkOut.add(new WorkOut("스쿼트", R.drawable.squart));
        arrWorkOut.add(new WorkOut("와이드스쿼트", R.drawable.squart_wide));
        arrWorkOut.add(new WorkOut("스쿼트 위드 레그레이즈", R.drawable.squart_with_leg));
        arrWorkOut.add(new WorkOut("얼터네이팅 런지", R.drawable.altern_lunge));
        arrChallenge.add(new Challenge("하체챌린지", arrWorkOut));
        return arrWorkOut;
    }
    private ArrayList<WorkOut> getArrWorkOut2(){
        ArrayList<WorkOut> arrWorkOut2 = new ArrayList<WorkOut>();
        arrWorkOut2.add(new WorkOut("점핑잭", R.drawable.jumpingjack));
        arrWorkOut2.add(new WorkOut("하이니", R.drawable.high_knees));
        arrWorkOut2.add(new WorkOut("백런지, 하이니", R.drawable.back_lunge_to_high_knee));
        arrChallenge.add(new Challenge("전신유산소", arrWorkOut2));
        return arrWorkOut2;
    }
    private ArrayList<WorkOut> getArrWorkOut3(){
        ArrayList<WorkOut> arrWorkOut3 = new ArrayList<WorkOut>();
        arrWorkOut3.add(new WorkOut("벤트니 크런치", R.drawable.bentni_crunch));
        arrWorkOut3.add(new WorkOut("플러터 킥", R.drawable.flerter_kick));
        arrWorkOut3.add(new WorkOut("사이드 크런치", R.drawable.side_crunch));
        arrWorkOut3.add(new WorkOut("세미 바이시클 크런치", R.drawable.semi_bycicle_crunch));
        arrWorkOut3.add(new WorkOut("시티드 니업", R.drawable.sited_knee_up));
        arrChallenge.add(new Challenge("복근운동챌린지", arrWorkOut3));
        return arrWorkOut3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDED_CHALLENGE && resultCode == RESULT_OK && data != null){
//            intent.putExtra("challenge_name",editChallengeName.getText().toString());
//            intent.putExtra("arr_workout",arrWorkoutList);

            String str = data.getStringExtra("challenge_name");
            ArrayList<WorkOut> arr = (ArrayList<WorkOut>)data.getSerializableExtra("arr_workout");

            // TODO db에 추가하려는 챌린지 정보 넣기
            // TODO db에서 챌린지 정보 읽어와 메인의 리스트 갱신하기

        }
    }
}
