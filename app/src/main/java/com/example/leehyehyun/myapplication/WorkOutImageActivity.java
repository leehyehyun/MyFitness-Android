package com.example.leehyehyun.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

public class WorkOutImageActivity extends AppCompatActivity {
    public WorkOutImageActivity() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        WorkOut workout = (WorkOut) args.getSerializable("workout");

        ImageView imgWorkout = findViewById(R.id.workout_imageview);

        Bitmap bitmap = BitmapFactory.decodeFile(workout.getImagePath());
        imgWorkout.setImageBitmap(bitmap);
    }
}
