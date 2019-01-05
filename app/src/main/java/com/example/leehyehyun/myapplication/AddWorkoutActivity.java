package com.example.leehyehyun.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddWorkoutActivity extends AppCompatActivity {
    private static final int ADDED_WORKOUT = 125;
    private static final int SELECT_PICTURE = 123;
    private static final int REQ_SAVE = 124;

    private TextView tvImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        final EditText editText = findViewById(R.id.edittext_workout_name);
        tvImagePath = findViewById(R.id.tv_file_name);
        Button button_complete = findViewById(R.id.button_complete);
        Button button_cancel = findViewById(R.id.button_cancel);
        ImageView img_photo = findViewById(R.id.img_photo);

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사진 권한묻고 갤러리 띄우기
                checkSavePermission();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_complete.setTag(tvImagePath);
        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvImagePath = (TextView)v.getTag();
                String workoutName = editText.getText().toString();
                String imagePath = tvImagePath.getText().toString();

                if(workoutName.isEmpty() || imagePath.isEmpty()){
                    Toast.makeText(AddWorkoutActivity.this, "운동이름 입력 및 사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("workoutName",workoutName);
                intent.putExtra("imagePath",imagePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void showGallery(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return;
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) {// no column index
                return; // DO YOUR ERROR HANDLING
            }

            String selectedImagePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            try{
//                Bitmap bitmap = decodeUri(uri);
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                String newSavedImagePath = savePhoto_in_mydevice(bitmap);
                tvImagePath.setText(newSavedImagePath);
                Log.v("is-","uri : "+uri.toString()+" *** newSavedImagePath : "+newSavedImagePath+" *** bitmap : "+bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private String savePhoto_in_mydevice(Bitmap bitmap) throws IOException {
        //특정경로에 이미지를 저장한다.

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myFitness";
        File dirFile = new File(path);
        if(!dirFile.exists()) dirFile.mkdirs();

        String path2 = path +"/mf_"+ System.currentTimeMillis() + ".jpg";

        if (bitmap == null) {
            Log.v("is-","insertPhoto fail");
            return "";
        }
        File imageFile = new File(path2);
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        outputStream.flush();
        outputStream.close();

        //갤러리 갱신(스캔)
        DMediaScanner scanner = new DMediaScanner(getApplicationContext());
        scanner.startScan(path2);

        return path2;
    }

    public void checkSavePermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddWorkoutActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_SAVE);
        } else {
            // 권한있음
            showGallery();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_SAVE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //권한있음
                showGallery();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddWorkoutActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(), "사진 접근 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //권한재시도
                }
            } else {
                Toast.makeText(getApplicationContext(), "사진 접근 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
