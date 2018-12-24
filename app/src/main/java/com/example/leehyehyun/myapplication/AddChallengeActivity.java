package com.example.leehyehyun.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddChallengeActivity extends AppCompatActivity {
    private EditText editChallengeName;
    private ScrollView addviewScroll;
    private LinearLayout addviewArea;

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
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(getApplicationContext(), "챌린지 저장 : "+editChallengeName.getText().toString()+" / arrSize : "+arrWorkOutList.size(), Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editChallengeName = (EditText)findViewById(R.id.edit_challenge_name);
        addviewScroll = (ScrollView)findViewById(R.id.addview_scroll);
        addviewArea = (LinearLayout)findViewById(R.id.addview_area);

        addview(true);

    }

    private ArrayList<WorkOut> arrWorkOutList = new ArrayList<>();
    private void addview(boolean isFirst){
        if(isFirst){
            addviewArea.removeAllViews();
        }
        View view = getLayoutInflater().inflate(R.layout.item_add_challenge, null, false);
        EditText mEditText = (EditText) view.findViewById(R.id.edittext_workout);
        ImageView mImgPhoto= (ImageView) view.findViewById(R.id.img_photo);
        ImageView mImgAdd = (ImageView) view.findViewById(R.id.img_add);
        ImageView mImgRemove = (ImageView) view.findViewById(R.id.img_remove);
        addviewArea.addView(view);
//        if(!mEditText.getText().toString().isEmpty()){
//            arrWorkOutList.add(new WorkOut(mEditText.getText().toString(), -1)); // 이미지는 나중에 넣기. 일단 -1로 고정해서 넣음
//        }

        mImgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addview(false);

            }
        });
        mImgRemove.setTag(view);
        mImgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addviewArea.getChildCount() <= 1){
                    return;
                }
                View view = (View)v.getTag();
                addviewArea.removeView(view);
                Log.v("is-","getChildCount : "+addviewArea.getChildCount());
            }
        });
    }




}
